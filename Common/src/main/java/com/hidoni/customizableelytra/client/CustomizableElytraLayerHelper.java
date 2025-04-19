package com.hidoni.customizableelytra.client;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.mixin.ElytraModelAccessor;
import com.hidoni.customizableelytra.mixin.TextureAtlasAccessor;
import com.hidoni.customizableelytra.render.TextureUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class CustomizableElytraLayerHelper {
    private static final TextureAtlas bannerPatternAtlas = getAtlas(Constants.ELYTRA_BANNER_SHEET);
    private static final TextureAtlas armorTrimAtlas = getAtlas(Sheets.ARMOR_TRIMS_SHEET);

    private static final ResourceLocation TEXTURE_GRAYSCALE_ELYTRA = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/elytra.png");
    private static final Function<ArmorTrim, TextureAtlasSprite> elytraTrimLookup = Util.memoize(trim -> armorTrimAtlas.getSprite(trim.layerAssetId("trims/models/elytra", EquipmentAssets.ELYTRA)));
    private static final ResourceLocation VANILLA_WINGS_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/equipment/wings/elytra.png");

    public static void render(ElytraModel elytraModel, ItemStack elytraStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, @Nullable ResourceLocation playerTexture) {
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(elytraStack);
        renderWing(((ElytraModelAccessor) elytraModel).getLeftWing(), customization.leftWing(), poseStack, multiBufferSource, packedLight, elytraStack.hasFoil(), ((CustomizableElytraItem) customization.leftWing().getItem()).isCapeHidden(customization.leftWing()) ? null : playerTexture);
        renderWing(((ElytraModelAccessor) elytraModel).getRightWing(), customization.rightWing(), poseStack, multiBufferSource, packedLight, elytraStack.hasFoil(), ((CustomizableElytraItem) customization.rightWing().getItem()).isCapeHidden(customization.rightWing()) ? null : playerTexture);
    }

    private static void renderWing(ModelPart wingModel, ItemStack wingStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, boolean hasFoil, @Nullable ResourceLocation playerTexture) {
        CustomizableElytraItem wingItem = (CustomizableElytraItem) wingStack.getItem();
        if (wingItem.isGlowing(wingStack)) {
            packedLight |= 0xFF;
        }
        if (wingItem.isDyed(wingStack)) {
            renderDyedWing(wingModel, wingStack, poseStack, multiBufferSource, packedLight, wingItem, getGrayscaleTexture(playerTexture), hasFoil);
        } else if (wingItem.hasBanner(wingStack)) {
            renderWingBannerPatterns(wingModel, wingStack, poseStack, multiBufferSource, packedLight, wingItem, getGrayscaleTexture(playerTexture), hasFoil);
        } else {
            renderBasicWing(wingModel, wingStack, poseStack, multiBufferSource, packedLight, wingItem, hasFoil, playerTexture);
        }
        if (wingItem.hasArmorTrim(wingStack)) {
            renderWingTrim(wingModel, wingStack, poseStack, multiBufferSource, packedLight, wingItem, hasFoil);
        }
    }

    private static void renderDyedWing(ModelPart wingModel, ItemStack wingStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, CustomizableElytraItem wingItem, ResourceLocation elytraTexture, boolean hasFoil) {
        VertexConsumer elytraVertexConsumer = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(elytraTexture), hasFoil);
        wingModel.render(poseStack, elytraVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, wingItem.getColor(wingStack));
    }

    private static void renderWingBannerPatterns(ModelPart wingModel, ItemStack wingStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, CustomizableElytraItem wingItem, ResourceLocation elytraTexture, boolean hasFoil) {
        BannerPatternLayers bannerPatterns = wingItem.getBannerPatterns(wingStack);
        // First render: Enchantment Glint
        wingModel.render(poseStack, ItemRenderer.getFoilBuffer(multiBufferSource, RenderType.entityNoOutline(elytraTexture), false, hasFoil), packedLight, OverlayTexture.NO_OVERLAY);
        // Second render: Base Layer
        wingModel.render(poseStack, ItemRenderer.getFoilBuffer(multiBufferSource, RenderType.entityTranslucent(elytraTexture), false, false), packedLight, OverlayTexture.NO_OVERLAY, wingItem.getBaseColor(wingStack).getTextureDiffuseColor());
        for (int i = 0; i < bannerPatterns.layers().size(); i++) {
            BannerPatternLayers.Layer bannerAndColor = bannerPatterns.layers().get(i);
            Optional<ResourceKey<BannerPattern>> resourceKey = bannerAndColor.pattern().unwrapKey();
            if (resourceKey.isPresent()) {
                Material bannerMaterial = new Material(Constants.ELYTRA_BANNER_SHEET, getTextureLocation(resourceKey.get()));
                Map<ResourceLocation, TextureAtlasSprite> texturesByName = ((TextureAtlasAccessor) bannerPatternAtlas).getTexturesByName();
                if (texturesByName.get(bannerMaterial.texture()) != null) // Don't render this banner pattern if it's missing, silently hide the pattern
                {
                    // Final renders: Pattern Layers
                    wingModel.render(poseStack, bannerMaterial.buffer(multiBufferSource, RenderType::entityTranslucent), packedLight, OverlayTexture.NO_OVERLAY, bannerAndColor.color().getTextureDiffuseColor());
                }
            }
        }
    }

    private static void renderBasicWing(ModelPart wingModel, ItemStack wingStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, CustomizableElytraItem wingItem, boolean hasFoil, @Nullable ResourceLocation playerTexture) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(playerTexture == null ? VANILLA_WINGS_LOCATION : playerTexture), hasFoil);
        wingModel.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
    }

    @NotNull
    private static TextureAtlas getAtlas(ResourceLocation location) {
        return Minecraft.getInstance().getModelManager().getAtlas(location);
    }

    private static void renderWingTrim(ModelPart wingModel, ItemStack wingStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, CustomizableElytraItem wingItem, boolean hasFoil) {
        Optional<ArmorTrim> armorTrim = wingItem.getArmorTrim(wingStack);
        armorTrim.ifPresent((trim) -> {
            TextureAtlasSprite sprite = elytraTrimLookup.apply(trim);
            VertexConsumer consumer = sprite.wrap(ItemRenderer.getFoilBuffer(multiBufferSource, Sheets.armorTrimsSheet(trim.pattern().value().decal()), true, hasFoil));
            wingModel.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        });
    }


    private static ResourceLocation getGrayscaleTexture(@Nullable ResourceLocation playerTexture) {
        if (playerTexture != null) {
            return TextureUtils.getGrayscale(playerTexture);
        }
        return TEXTURE_GRAYSCALE_ELYTRA;
    }

    private static ResourceLocation getTextureLocation(ResourceKey<BannerPattern> bannerIn) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entity/elytra_banner/" + bannerIn.location().getPath());
    }
}
