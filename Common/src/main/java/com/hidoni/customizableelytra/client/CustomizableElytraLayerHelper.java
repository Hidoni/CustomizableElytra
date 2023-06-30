package com.hidoni.customizableelytra.client;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.mixin.TextureAtlasAccessor;
import com.hidoni.customizableelytra.render.ElytraWingModel;
import com.hidoni.customizableelytra.render.TextureUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class CustomizableElytraLayerHelper<T extends LivingEntity> {
    private static final ResourceLocation TEXTURE_GRAYSCALE_ELYTRA = new ResourceLocation(Constants.MOD_ID, "textures/entity/elytra.png");
    private static final Function<ArmorTrim, ResourceLocation> elytraTrimLookup = Util.memoize(trim -> trim.pattern().value().assetId().withPath((path) -> "trims/models/elytra/" + path + "_" + trim.material().value().assetName()));
    private static final ResourceLocation VANILLA_WINGS_LOCATION = new ResourceLocation("textures/entity/elytra.png");

    private T entity = null;
    private MultiBufferSource defaultBuffer = null;
    private ItemStack elytra = null;
    private final TextureAtlas bannerPatternAtlas;
    private final TextureAtlas armorTrimAtlas;

    public CustomizableElytraLayerHelper() {
        bannerPatternAtlas = getAtlas(Constants.ELYTRA_BANNER_SHEET);
        armorTrimAtlas = getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public void setDefaultBuffer(MultiBufferSource defaultBuffer) {
        this.defaultBuffer = defaultBuffer;
    }

    public void setElytra(ItemStack elytra) {
        this.elytra = elytra;
    }

    public ItemStack getElytra() {
        return elytra;
    }

    public void renderWing(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, boolean hasFoil) {
        CustomizableElytraItem wingItem = (CustomizableElytraItem) wingStack.getItem();
        if (wingItem.isGlowing(wingStack)) {
            packedLight |= 0xFF;
        }
        if (wingItem.hasCustomColor(wingStack)) {
            renderDyedWing(wingModel, wingStack, poseStack, packedLight, wingItem, getGrayscaleTexture(wingItem.isCapeHidden(wingStack)), hasFoil);
        } else if (wingItem.hasBanner(wingStack)) {
            renderWingBannerPatterns(wingModel, wingStack, poseStack, packedLight, wingItem, getGrayscaleTexture(wingItem.isCapeHidden(wingStack)), hasFoil);
        } else {
            renderBasicWing(wingModel, wingStack, poseStack, packedLight, wingItem, hasFoil);
        }
        if (wingItem.hasArmorTrim(wingStack)) {
            renderWingTrim(wingModel, wingStack, poseStack, packedLight, wingItem, hasFoil);
        }
    }

    private void renderDyedWing(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, int packedLight, CustomizableElytraItem wingItem, ResourceLocation elytraTexture, boolean hasFoil) {
        VertexConsumer elytraVertexConsumer = ItemRenderer.getArmorFoilBuffer(defaultBuffer, RenderType.armorCutoutNoCull(elytraTexture), false, hasFoil);
        float[] rgb = CustomizationUtils.convertIntToRGB(wingItem.getColor(wingStack));
        wingModel.renderToBuffer(poseStack, elytraVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, rgb[0], rgb[1], rgb[2], 1.0F);
    }

    private void renderWingBannerPatterns(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, int packedLight, CustomizableElytraItem wingItem, ResourceLocation elytraTexture, boolean hasFoil) {
        List<Pair<Holder<BannerPattern>, DyeColor>> bannerPatterns = wingItem.getBannerPatterns(wingStack);
        // First render: Enchantment Glint
        wingModel.renderToBuffer(poseStack, ItemRenderer.getFoilBufferDirect(defaultBuffer, RenderType.entityNoOutline(elytraTexture), false, hasFoil), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        float[] baseColor = bannerPatterns.get(0).getSecond().getTextureDiffuseColors();
        // Second render: Base Layer
        wingModel.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(defaultBuffer, RenderType.entityTranslucent(elytraTexture), false, false), packedLight, OverlayTexture.NO_OVERLAY, baseColor[0], baseColor[1], baseColor[2], 1.0F);
        for (int i = 1; i < 17 && i < bannerPatterns.size(); i++) {
            Pair<Holder<BannerPattern>, DyeColor> bannerAndColor = bannerPatterns.get(i);
            float[] colors = bannerAndColor.getSecond().getTextureDiffuseColors();
            Optional<ResourceKey<BannerPattern>> resourceKey = bannerAndColor.getFirst().unwrapKey();
            if (resourceKey.isPresent()) {
                Material bannerMaterial = new Material(Constants.ELYTRA_BANNER_SHEET, getTextureLocation(resourceKey.get()));
                Map<ResourceLocation, TextureAtlasSprite> texturesByName = ((TextureAtlasAccessor) bannerPatternAtlas).getTexturesByName();
                if (texturesByName.get(bannerMaterial.texture()) != null) // Don't render this banner pattern if it's missing, silently hide the pattern
                {
                    // Final renders: Pattern Layers
                    wingModel.renderToBuffer(poseStack, bannerMaterial.buffer(defaultBuffer, RenderType::entityTranslucent), packedLight, OverlayTexture.NO_OVERLAY, colors[0], colors[1], colors[2], 1.0F);
                }
            }
        }
    }

    private void renderBasicWing(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, int packedLight, CustomizableElytraItem wingItem, boolean hasFoil) {
        ResourceLocation elytraTexture = null;
        if (!wingItem.isCapeHidden(wingStack)) {
            elytraTexture = getCapeTexture();
        }
        if (elytraTexture == null) {
            elytraTexture = VANILLA_WINGS_LOCATION;
        }
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(defaultBuffer, RenderType.armorCutoutNoCull(elytraTexture), false, hasFoil);
        wingModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @NotNull
    private static TextureAtlas getAtlas(ResourceLocation location) {
        return Minecraft.getInstance().getModelManager().getAtlas(location);
    }

    private void renderWingTrim(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, int packedLight, CustomizableElytraItem wingItem, boolean hasFoil) {
        Optional<ArmorTrim> armorTrim = wingItem.getArmorTrim(wingStack, entity.level().registryAccess());
        armorTrim.ifPresent((trim) -> {
            ResourceLocation trimLocation = elytraTrimLookup.apply(trim);
            TextureAtlasSprite sprite = armorTrimAtlas.getSprite(trimLocation);
            VertexConsumer consumer = sprite.wrap(ItemRenderer.getFoilBufferDirect(defaultBuffer, Sheets.armorTrimsSheet(), true, hasFoil));
            wingModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        });
    }


    private ResourceLocation getGrayscaleTexture(boolean capeHidden) {
        ResourceLocation elytraTexture = null;
        if (!capeHidden) {
            elytraTexture = getCapeTexture();
        }
        if (elytraTexture != null) {
            return TextureUtils.getGrayscale(elytraTexture);
        }
        return TEXTURE_GRAYSCALE_ELYTRA;
    }

    private ResourceLocation getCapeTexture() {
        if (entity instanceof AbstractClientPlayer clientPlayer) {
            if (clientPlayer.isElytraLoaded() && clientPlayer.getElytraTextureLocation() != null) {
                return clientPlayer.getElytraTextureLocation();
            } else if (clientPlayer.isCapeLoaded() && clientPlayer.getCloakTextureLocation() != null && clientPlayer.isModelPartShown(PlayerModelPart.CAPE)) {
                return clientPlayer.getCloakTextureLocation();
            }
        }
        return null;
    }

    private static ResourceLocation getTextureLocation(ResourceKey<BannerPattern> bannerIn) {
        return new ResourceLocation(Constants.MOD_ID, "entity/elytra_banner/" + bannerIn.location().getPath());
    }
}
