package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.client.CustomizableElytraLayerHelper;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.render.ElytraWingModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "com.illusivesoulworks.elytraslot.client.ElytraSlotLayer")
public abstract class ElytraSlotLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    @Shadow
    @Final
    private ElytraModel<T> elytraModel;
    private ElytraWingModel<T> leftWing;
    private ElytraWingModel<T> rightWing;
    private CustomizableElytraLayerHelper<T> helper;

    public ElytraSlotLayerMixin(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initWingModels(RenderLayerParent<T, M> parent, EntityModelSet entityModelSet, CallbackInfo ci) {
        leftWing = new ElytraWingModel<>(elytraModel, false);
        rightWing = new ElytraWingModel<>(elytraModel, true);
        helper = new CustomizableElytraLayerHelper<>();
    }

    // Elytra Slot runs everything in a lambda, so to make life easy we target all methods to inject (should still only be one match!)
    @WrapOperation(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    private static Item storeElytraStack(ItemStack elytra, Operation<Item> operation, @Share("elytra") LocalRef<ItemStack> stack) {
        stack.set(elytra);
        return operation.call(elytra);
    }

    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ElytraModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"))
    private void renderCustomizedElytraWings(ElytraModel<T> elytraModel, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int overlayTexture, int argb,
                                             @Local(argsOnly = true) MultiBufferSource buffer, @Local(argsOnly = true) LivingEntity entity, @Share("elytra") LocalRef<ItemStack> elytra) {
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(elytra.get());
        if (!customization.isCustomized()) {
            elytraModel.renderToBuffer(poseStack, vertexConsumer, packedLight, overlayTexture, argb);
            return;
        }
        getParentModel().copyPropertiesTo(leftWing);
        getParentModel().copyPropertiesTo(rightWing);
        helper.renderWing(leftWing, customization.leftWing(), poseStack, vertexConsumer, buffer, packedLight, elytra.get().hasFoil(), entity);
        helper.renderWing(rightWing, customization.rightWing(), poseStack, vertexConsumer, buffer, packedLight, elytra.get().hasFoil(), entity);
    }
}
