package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.client.CustomizableElytraLayerHelper;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.render.ElytraWingModel;
import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraLayer.class)
public abstract class ElytraLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    @Shadow
    @Final
    private ElytraModel<T> elytraModel;
    @Unique
    private ElytraWingModel<T> leftWing;
    @Unique
    private ElytraWingModel<T> rightWing;
    @Unique
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

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ElytraModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"))
    private void renderCustomizedElytraWings(ElytraModel<T> elytraModel, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int overlayTexture,
                                             @Local(argsOnly = true) MultiBufferSource buffer, @Local(argsOnly = true) LivingEntity entity, @Local ItemStack elytra) {
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(elytra);
        if (!customization.isCustomized()) {
            elytraModel.renderToBuffer(poseStack, vertexConsumer, packedLight, overlayTexture);
            return;
        }
        getParentModel().copyPropertiesTo(leftWing);
        getParentModel().copyPropertiesTo(rightWing);
        helper.renderWing(leftWing, customization.leftWing(), poseStack, vertexConsumer, buffer, packedLight, elytra.hasFoil(), entity);
        helper.renderWing(rightWing, customization.rightWing(), poseStack, vertexConsumer, buffer, packedLight, elytra.hasFoil(), entity);
    }
}
