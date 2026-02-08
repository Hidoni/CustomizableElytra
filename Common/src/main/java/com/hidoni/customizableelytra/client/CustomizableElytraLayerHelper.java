package com.hidoni.customizableelytra.client;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.mixin.ElytraModelAccessor;
import com.hidoni.customizableelytra.mixin.TextureAtlasAccessor;
import com.hidoni.customizableelytra.render.TextureUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.model.Material;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
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
    private static final TextureAtlas bannerPatternAtlas = getAtlas(Constants.ELYTRA_BANNER_ATLAS);
    private static final TextureAtlas armorTrimAtlas = getAtlas(AtlasIds.ARMOR_TRIMS);

    private static final Identifier TEXTURE_GRAYSCALE_ELYTRA = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/elytra.png");
    private static final Function<ArmorTrim, TextureAtlasSprite> elytraTrimLookup = Util.memoize(trim -> armorTrimAtlas.getSprite(trim.layerAssetId("trims/models/elytra", EquipmentAssets.ELYTRA)));
    private static final Identifier VANILLA_WINGS_LOCATION = Identifier.withDefaultNamespace("textures/entity/equipment/wings/elytra.png");

    public static <S extends HumanoidRenderState> void render(EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAsset, Model<? super S> elytraModel, S renderState, ItemStack elytraStack, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, Identifier playerTexture, int outlineColor, int key) {
        elytraModel.setupAnim(renderState);
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(elytraStack);
        renderWing(layerType, equipmentAsset, ((ElytraModelAccessor) elytraModel).getLeftWing(), renderState, customization.leftWing(), poseStack, nodeCollector, packedLight, elytraStack.hasFoil(), ((CustomizableElytraItem) customization.leftWing().getItem()).isCapeHidden(customization.leftWing()) ? null : playerTexture, outlineColor, key);
        renderWing(layerType, equipmentAsset, ((ElytraModelAccessor) elytraModel).getRightWing(), renderState, customization.rightWing(), poseStack, nodeCollector, packedLight, elytraStack.hasFoil(), ((CustomizableElytraItem) customization.rightWing().getItem()).isCapeHidden(customization.rightWing()) ? null : playerTexture, outlineColor, key);
    }

    private static <S extends HumanoidRenderState> void renderWing(EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAsset, ModelPart wingModel, S renderState, ItemStack wingStack, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, boolean hasFoil, @Nullable Identifier playerTexture, int outlineColor, int key) {
        CustomizableElytraItem wingItem = (CustomizableElytraItem) wingStack.getItem();
        if (wingItem.isGlowing(wingStack)) {
            packedLight |= 0xFF;
        }
        if (wingItem.isDyed(wingStack)) {
            key = renderDyedWing(wingModel, wingStack, poseStack, nodeCollector, packedLight, wingItem, getGrayscaleTexture(playerTexture), hasFoil, outlineColor, key);
        } else if (wingItem.hasBanner(wingStack)) {
            key = renderWingBannerPatterns(wingModel, wingStack, poseStack, nodeCollector, packedLight, wingItem, getGrayscaleTexture(playerTexture), hasFoil, outlineColor, key);
        } else {
            key = renderBasicWing(wingModel, poseStack, nodeCollector, packedLight, playerTexture, hasFoil, outlineColor, key);
        }
        if (wingItem.hasArmorTrim(wingStack)) {
            renderWingTrim(wingModel, wingStack, poseStack, nodeCollector, packedLight, wingItem, hasFoil, outlineColor, key);
        }
    }

    private static int renderDyedWing(ModelPart wingModel, ItemStack wingStack, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, CustomizableElytraItem wingItem, Identifier elytraTexture, boolean hasFoil, int outlineColor, int key) {
        nodeCollector.order(key++).submitModelPart(wingModel, poseStack, RenderTypes.armorCutoutNoCull(elytraTexture), packedLight, OverlayTexture.NO_OVERLAY, null, false, hasFoil, wingItem.getColor(wingStack), null, outlineColor);
        if (hasFoil) {
            nodeCollector.order(key++).submitModelPart(wingModel, poseStack, RenderTypes.armorEntityGlint(), packedLight, OverlayTexture.NO_OVERLAY, null, false, true, wingItem.getColor(wingStack), null, outlineColor);
        }
        return key;
    }

    private static int renderWingBannerPatterns(ModelPart wingModel, ItemStack wingStack, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, CustomizableElytraItem wingItem, Identifier elytraTexture, boolean hasFoil, int outlineColor, int key) {
        BannerPatternLayers bannerPatterns = wingItem.getBannerPatterns(wingStack);
        // First render: Enchantment Glint
        if (hasFoil) {
            nodeCollector.order(key++).submitModelPart(wingModel, poseStack, RenderTypes.armorEntityGlint(), packedLight, OverlayTexture.NO_OVERLAY, null, false, true, -1, null, outlineColor);
        }
        // Second render: Base Layer
        nodeCollector.order(key++).submitModelPart(wingModel, poseStack, RenderTypes.armorTranslucent(elytraTexture), packedLight, OverlayTexture.NO_OVERLAY, null, false, hasFoil, wingItem.getBaseColor(wingStack).getTextureDiffuseColor(), null, outlineColor);
        for (int i = 0; i < bannerPatterns.layers().size(); i++) {
            BannerPatternLayers.Layer bannerAndColor = bannerPatterns.layers().get(i);
            Optional<ResourceKey<BannerPattern>> resourceKey = bannerAndColor.pattern().unwrapKey();
            if (resourceKey.isPresent()) {
                Material bannerMaterial = new Material(Constants.ELYTRA_BANNER_SHEET, getTextureIdentifier(resourceKey.get()));
                Map<Identifier, TextureAtlasSprite> texturesByName = ((TextureAtlasAccessor) bannerPatternAtlas).getTexturesByName();
                if (texturesByName.get(bannerMaterial.texture()) != null) // Don't render this banner pattern if it's missing, silently hide the pattern
                {
                    // Final renders: Pattern Layers
                    nodeCollector.order(key++).submitModelPart(wingModel, poseStack, bannerMaterial.renderType(RenderTypes::armorTranslucent), packedLight, OverlayTexture.NO_OVERLAY, bannerPatternAtlas.getSprite(bannerMaterial.texture()), false, hasFoil, bannerAndColor.color().getTextureDiffuseColor(), null, outlineColor);
                }
            }
        }
        return key;
    }

    private static int renderBasicWing(ModelPart wingModel, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, @Nullable Identifier playerTexture, boolean hasFoil, int outlineColor, int key) {
        Identifier elytraTexture = playerTexture == null ? VANILLA_WINGS_LOCATION : playerTexture;
        nodeCollector.order(key++).submitModelPart(wingModel, poseStack, RenderTypes.armorCutoutNoCull(elytraTexture), packedLight, OverlayTexture.NO_OVERLAY, null, false, hasFoil, -1, null, outlineColor);
        if (hasFoil) {
            nodeCollector.order(key++).submitModelPart(wingModel, poseStack, RenderTypes.armorEntityGlint(), packedLight, OverlayTexture.NO_OVERLAY, null, false, true, -1, null, outlineColor);
        }
        return key;
    }

    @NotNull
    private static TextureAtlas getAtlas(Identifier identifier) {
        return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(identifier);
    }

    private static <S extends HumanoidRenderState> void renderWingTrim(ModelPart wingModel, ItemStack wingStack, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, CustomizableElytraItem wingItem, boolean hasFoil, int outlineColor, int key) {
        Optional<ArmorTrim> armorTrim = wingItem.getArmorTrim(wingStack);
        armorTrim.ifPresent((trim) -> {
            TextureAtlasSprite sprite = elytraTrimLookup.apply(trim);
            RenderType renderType = Sheets.armorTrimsSheet(trim.pattern().value().decal());
            nodeCollector.order(key).submitModelPart(wingModel, poseStack, renderType, packedLight, OverlayTexture.NO_OVERLAY, sprite, false, hasFoil, -1, null, outlineColor);
        });
    }


    private static Identifier getGrayscaleTexture(@Nullable Identifier playerTexture) {
        if (playerTexture != null) {
            return TextureUtils.getGrayscale(playerTexture);
        }
        return TEXTURE_GRAYSCALE_ELYTRA;
    }

    private static Identifier getTextureIdentifier(ResourceKey<BannerPattern> bannerIn) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "entity/elytra_banner/" + bannerIn.identifier().getPath());
    }
}
