package com.hidoni.customizableelytra.mixin;

import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ElytraLayer.class)
public interface ElytraLayerAccessor {
    @Accessor("WINGS_LOCATION")
    ResourceLocation getDefaultElytraTexture();
}
