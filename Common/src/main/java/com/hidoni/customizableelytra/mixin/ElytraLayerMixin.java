package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.client.CustomizableElytraLayerHelper;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.render.ElytraWingModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraLayer.class)
public abstract class ElytraLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    @Shadow
    @Final
    private ElytraModel<T> elytraModel;
    private ElytraWingModel<T> leftWing;
    private ElytraWingModel<T> rightWing;
    private CustomizableElytraLayerHelper<T> helper;

    public ElytraLayerMixin(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initWingModels(RenderLayerParent<T, M> parent, EntityModelSet entityModelSet, CallbackInfo ci) {
        leftWing = new ElytraWingModel<>(elytraModel, false);
        rightWing = new ElytraWingModel<>(elytraModel, true);
        helper = new CustomizableElytraLayerHelper<>();
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"))
    private void storeRenderArguments(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        helper.setEntity(livingEntity);
        helper.setDefaultBuffer(buffer);
    }

    @ModifyVariable(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("STORE"), ordinal = 0)
    private ItemStack storeElytraStack(ItemStack elytra) {
        helper.setElytra(elytra);
        return elytra;
    }

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ElytraModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    private void renderCustomizedElytraWings(ElytraModel<T> elytraModel, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int overlayTexture, float red, float green, float blue, float alpha) {
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(helper.getElytra());
        if (!customization.isCustomized()) {
            elytraModel.renderToBuffer(poseStack, vertexConsumer, packedLight, overlayTexture, red, green, blue, alpha);
            return;
        }
        getParentModel().copyPropertiesTo(leftWing);
        getParentModel().copyPropertiesTo(rightWing);
        helper.renderWing(leftWing, customization.leftWing(), poseStack, vertexConsumer, packedLight, helper.getElytra().hasFoil());
        helper.renderWing(rightWing, customization.rightWing(), poseStack, vertexConsumer, packedLight, helper.getElytra().hasFoil());
    }
}
