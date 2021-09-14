package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.renderers.CustomizableElytraLayer;
import net.minecraft.client.model.ArmorStandArmorModel;
import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandRenderer.class)
public abstract class ArmorStandRendererMixin extends LivingEntityRenderer<ArmorStand, ArmorStandArmorModel> {
    public ArmorStandRendererMixin(EntityRendererProvider.Context ctx) {
        super(ctx, new ArmorStandModel(ctx.bakeLayer(ModelLayers.ARMOR_STAND)), 0.0F);
    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V")
    private void postConstructor(EntityRendererProvider.Context context, CallbackInfo callbackInfo) {
        this.addLayer(new CustomizableElytraLayer<>(this, context.getModelSet()));
    }
}
