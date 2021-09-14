package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.renderers.CustomizableElytraLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public PlayerRendererMixin(EntityRendererProvider.Context ctx, boolean slim) {
        super(ctx, new PlayerModel<>(ctx.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V")
    private void postConstructor(EntityRendererProvider.Context context, boolean slim, CallbackInfo callbackInfo) {
        this.addLayer(new CustomizableElytraLayer<>(this, context.getModelSet()));
    }
}
