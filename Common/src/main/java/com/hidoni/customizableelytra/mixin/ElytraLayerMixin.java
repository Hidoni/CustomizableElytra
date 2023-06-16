package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.render.ElytraWingModel;
import com.hidoni.customizableelytra.render.TextureUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Mixin(ElytraLayer.class)
public abstract class ElytraLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation TEXTURE_GRAYSCALE_ELYTRA = new ResourceLocation(Constants.MOD_ID, "textures/entity/elytra.png");
    private static final Function<ArmorTrim, ResourceLocation> elytraTrimLookup = Util.memoize(trim -> trim.pattern().value().assetId().withPath((path) -> "trims/models/elytra/" + path + "_" + trim.material().value().assetName()));
    @Shadow
    @Final
    private ElytraModel<T> elytraModel;
    @Shadow @Final private static ResourceLocation WINGS_LOCATION;
    private T entityRef = null;
    private MultiBufferSource bufferRef = null;
    private ElytraWingModel<T> leftWing;
    private ElytraWingModel<T> rightWing;
    private TextureAtlas bannerPatternAtlas;
    private TextureAtlas armorTrimAtlas;

    public ElytraLayerMixin(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initWingModels(RenderLayerParent<T, M> parent, EntityModelSet entityModelSet, CallbackInfo ci) {
        leftWing = new ElytraWingModel<>(elytraModel, false);
        rightWing = new ElytraWingModel<>(elytraModel, true);
        bannerPatternAtlas = getAtlas(Constants.ELYTRA_BANNER_SHEET);
        armorTrimAtlas = getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"))
    private void storeElytraForRender(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        entityRef = livingEntity;
        bufferRef = buffer;
    }

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ElytraModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    private void renderCustomizedElytraWings(ElytraModel<T> elytraModel, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int overlayTexture, float red, float green, float blue, float alpha) {
        ItemStack elytraStack = entityRef.getItemBySlot(EquipmentSlot.CHEST);
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(elytraStack);
        if (!customization.isCustomized()) {
            elytraModel.renderToBuffer(poseStack, vertexConsumer, packedLight, overlayTexture, red, green, blue, alpha);
            return;
        }
        getParentModel().copyPropertiesTo(leftWing);
        getParentModel().copyPropertiesTo(rightWing);
        renderWing(leftWing, customization.leftWing(), poseStack, vertexConsumer, packedLight, elytraStack.hasFoil());
        renderWing(rightWing, customization.rightWing(), poseStack, vertexConsumer, packedLight, elytraStack.hasFoil());
    }

    private void renderWing(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, boolean hasFoil) {
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
        VertexConsumer elytraVertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferRef, RenderType.armorCutoutNoCull(elytraTexture), false, hasFoil);
        float[] rgb = CustomizationUtils.convertIntToRGB(wingItem.getColor(wingStack));
        wingModel.renderToBuffer(poseStack, elytraVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, rgb[0], rgb[1], rgb[2], 1.0F);
    }

    private void renderWingBannerPatterns(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, int packedLight, CustomizableElytraItem wingItem, ResourceLocation elytraTexture, boolean hasFoil) {
        List<Pair<Holder<BannerPattern>, DyeColor>> bannerPatterns = wingItem.getBannerPatterns(wingStack);
        // First render: Enchantment Glint
        wingModel.renderToBuffer(poseStack, ItemRenderer.getFoilBufferDirect(bufferRef, RenderType.entityNoOutline(elytraTexture), false, hasFoil), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        float[] baseColor = bannerPatterns.get(0).getSecond().getTextureDiffuseColors();
        // Second render: Base Layer
        wingModel.renderToBuffer(poseStack, ItemRenderer.getFoilBuffer(bufferRef, RenderType.entityTranslucent(elytraTexture), false, false), packedLight, OverlayTexture.NO_OVERLAY, baseColor[0], baseColor[1], baseColor[2], 1.0F);
        for (int i = 1; i < 17 && i < bannerPatterns.size(); i++) {
            Pair<Holder<BannerPattern>, DyeColor> bannerAndColor = bannerPatterns.get(i);
            float[] colors = bannerAndColor.getSecond().getTextureDiffuseColors();
            Optional<ResourceKey<BannerPattern>> resourceKey = bannerAndColor.getFirst().unwrapKey();
            if (resourceKey.isPresent()) {
                Material bannerMaterial = new Material(Constants.ELYTRA_BANNER_SHEET, getTextureLocation(resourceKey.get()));
                Map<ResourceLocation, TextureAtlasSprite> texturesByName = ((TextureAtlasAccessor)bannerPatternAtlas).getTexturesByName();
                if (texturesByName.get(bannerMaterial.texture()) != null) // Don't render this banner pattern if it's missing, silently hide the pattern
                {
                    // Final renders: Pattern Layers
                    wingModel.renderToBuffer(poseStack, bannerMaterial.buffer(bufferRef, RenderType::entityTranslucent), packedLight, OverlayTexture.NO_OVERLAY, colors[0], colors[1], colors[2], 1.0F);
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
            elytraTexture = WINGS_LOCATION;
        }
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(bufferRef, RenderType.armorCutoutNoCull(elytraTexture), false, hasFoil);
        wingModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @NotNull
    private static TextureAtlas getAtlas(ResourceLocation location) {
        return Minecraft.getInstance().getModelManager().getAtlas(location);
    }

    private void renderWingTrim(ElytraWingModel<T> wingModel, ItemStack wingStack, PoseStack poseStack, int packedLight, CustomizableElytraItem wingItem, boolean hasFoil) {
        Optional<ArmorTrim> armorTrim = wingItem.getArmorTrim(wingStack, entityRef.level().registryAccess());
        armorTrim.ifPresent((trim) -> {
            ResourceLocation trimLocation = elytraTrimLookup.apply(trim);
            TextureAtlasSprite sprite = armorTrimAtlas.getSprite(trimLocation);
            VertexConsumer consumer = sprite.wrap(ItemRenderer.getFoilBufferDirect(bufferRef, Sheets.armorTrimsSheet(), true, hasFoil));
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
        if (entityRef instanceof AbstractClientPlayer clientPlayer) {
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
