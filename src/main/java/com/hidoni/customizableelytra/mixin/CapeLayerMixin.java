package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.util.ElytraInventoryUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public class CapeLayerMixin {
    @Inject(at = @At(value = "HEAD"), method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFFFFF)V", cancellable = true)
    public void preRenderCustomizableElytraCheck(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ItemStack itemstack = ElytraInventoryUtil.tryFindElytra(entitylivingbaseIn);
        if (itemstack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get()) {
            ci.cancel();
        }
    }
}