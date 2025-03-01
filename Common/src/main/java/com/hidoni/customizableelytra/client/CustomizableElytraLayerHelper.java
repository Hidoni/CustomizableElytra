package com.hidoni.customizableelytra.client;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.mixin.TextureAtlasAccessor;
import com.hidoni.customizableelytra.render.ElytraWingModel;
import com.hidoni.customizableelytra.render.TextureUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class CustomizableElytraLayerHelper<T extends LivingEntity> {
    private static final ResourceLocation TEXTURE_GRAYSCALE_ELYTRA = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/elytra.png");
    private static final Function<ArmorTrim, ResourceLocation> elytraTrimLookup = Util.memoize(trim -> trim.pattern().value().assetId().withPath((path) -> "trims/models/elytra/" + path + "_" + trim.material().value().assetName()));
    private static final ResourceLocation VANILLA_WINGS_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");

    private final TextureAtlas bannerPatternAtlas;
    private final TextureAtlas armorTrimAtlas;

    public CustomizableElytraLayerHelper() {
        bannerPatternAtlas = getAtlas(Constants.ELYTRA_BANNER_SHEET);
        armorTrimAtlas = getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    public void renderWing(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, VertexConsumer vertexConsumer, MultiBufferSource buffer, int packedLight, boolean hasFoil, LivingEntity entity) {
        CustomizableElytraItem wingItem = (CustomizableElytraItem) wingStack.getItem();
        if (wingItem.isGlowing(wingStack)) {
            packedLight |= 0xFF;
        }
        if (wingItem.isDyed(wingStack)) {
            renderDyedWing(wingModel, wingStack, poseStack, buffer, packedLight, wingItem, getGrayscaleTexture(entity, wingItem.isCapeHidden(wingStack)), hasFoil);
        } else if (wingItem.hasBanner(wingStack)) {
            renderWingBannerPatterns(wingModel, wingStack, poseStack, buffer, packedLight, wingItem, getGrayscaleTexture(entity, wingItem.isCapeHidden(wingStack)), hasFoil);
        } else {
            renderBasicWing(wingModel, wingStack, poseStack, buffer, packedLight, wingItem, hasFoil, entity);
        }
        if (wingItem.hasArmorTrim(wingStack)) {
            renderWingTrim(wingModel, wingStack, poseStack, buffer, packedLight, wingItem, hasFoil);
        }
    }

    private void renderDyedWing(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CustomizableElytraItem wingItem, ResourceLocation elytraTexture, boolean hasFoil) {
        VertexConsumer elytraVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(elytraTexture), hasFoil);
        wingModel.renderToBuffer(poseStack, elytraVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, wingItem.getColor(wingStack));
    }

    private void renderWingBannerPatterns(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CustomizableElytraItem wingItem, ResourceLocation elytraTexture, boolean hasFoil) {
        BannerPatternLayers bannerPatterns = wingItem.getBannerPatterns(wingStack);
        // First render: Enchantment Glint
        wingModel.renderToBuffer(poseStack, ItemRenderer.getFoilBufferDirect(buffer, RenderType.entityNoOutline(elytraTexture), false, hasFoil), packedLight, OverlayTexture.NO_OVERLAY);
        // Second render: Base Layer
        wingModel.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(buffer, RenderType.entityTranslucent(elytraTexture), false, false), packedLight, OverlayTexture.NO_OVERLAY, wingItem.getBaseColor(wingStack).getTextureDiffuseColor());
        for (int i = 0; i < bannerPatterns.layers().size(); i++) {
            BannerPatternLayers.Layer bannerAndColor = bannerPatterns.layers().get(i);
            Optional<ResourceKey<BannerPattern>> resourceKey = bannerAndColor.pattern().unwrapKey();
            if (resourceKey.isPresent()) {
                Material bannerMaterial = new Material(Constants.ELYTRA_BANNER_SHEET, getTextureLocation(resourceKey.get()));
                Map<ResourceLocation, TextureAtlasSprite> texturesByName = ((TextureAtlasAccessor) bannerPatternAtlas).getTexturesByName();
                if (texturesByName.get(bannerMaterial.texture()) != null) // Don't render this banner pattern if it's missing, silently hide the pattern
                {
                    // Final renders: Pattern Layers
                    wingModel.renderToBuffer(poseStack, bannerMaterial.buffer(buffer, RenderType::entityTranslucent), packedLight, OverlayTexture.NO_OVERLAY, bannerAndColor.color().getTextureDiffuseColor());
                }
            }
        }
    }

    private void renderBasicWing(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CustomizableElytraItem wingItem, boolean hasFoil, LivingEntity entity) {
        ResourceLocation elytraTexture = null;
        if (!wingItem.isCapeHidden(wingStack)) {
            elytraTexture = getCapeTexture(entity);
        }
        if (elytraTexture == null) {
            elytraTexture = VANILLA_WINGS_LOCATION;
        }
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(elytraTexture), hasFoil);
        wingModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
    }

    @NotNull
    private static TextureAtlas getAtlas(ResourceLocation location) {
        return Minecraft.getInstance().getModelManager().getAtlas(location);
    }

    private void renderWingTrim(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CustomizableElytraItem wingItem, boolean hasFoil) {
        Optional<ArmorTrim> armorTrim = wingItem.getArmorTrim(wingStack);
        armorTrim.ifPresent((trim) -> {
            ResourceLocation trimLocation = elytraTrimLookup.apply(trim);
            TextureAtlasSprite sprite = armorTrimAtlas.getSprite(trimLocation);
            VertexConsumer consumer = sprite.wrap(ItemRenderer.getFoilBufferDirect(buffer, Sheets.armorTrimsSheet(trim.pattern().value().decal()), true, hasFoil));
            wingModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        });
    }


    private ResourceLocation getGrayscaleTexture(LivingEntity entity, boolean capeHidden) {
        ResourceLocation elytraTexture = null;
        if (!capeHidden) {
            elytraTexture = getCapeTexture(entity);
        }
        if (elytraTexture != null) {
            return TextureUtils.getGrayscale(elytraTexture);
        }
        return TEXTURE_GRAYSCALE_ELYTRA;
    }

    private ResourceLocation getCapeTexture(LivingEntity entity) {
        if (entity instanceof AbstractClientPlayer clientPlayer) {
            PlayerSkin skin = clientPlayer.getSkin();
            if (skin.elytraTexture() != null) {
                return skin.elytraTexture();
            } else if (skin.capeTexture() != null && clientPlayer.isModelPartShown(PlayerModelPart.CAPE)) {
                return skin.capeTexture();
            }
        }
        return null;
    }

    private static ResourceLocation getTextureLocation(ResourceKey<BannerPattern> bannerIn) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entity/elytra_banner/" + bannerIn.location().getPath());
    }
}
