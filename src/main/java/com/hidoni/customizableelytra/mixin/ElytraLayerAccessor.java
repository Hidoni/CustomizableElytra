package com.hidoni.customizableelytra.mixin;

import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ElytraLayer.class)
public interface ElytraLayerAccessor<T extends LivingEntity> {
    @Accessor("WINGS_LOCATION")
    ResourceLocation getDefaultElytraTexture();

    @Accessor("elytraModel")
    ElytraModel<T> getElytraModel();
}
