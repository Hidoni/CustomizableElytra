package com.hidoni.customizableelytra.mixin;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemProperties.class)
public interface ItemPropertiesInvoker {
    @Invoker
    static ClampedItemPropertyFunction invokeRegisterGeneric(ResourceLocation location, ClampedItemPropertyFunction function) {
        throw new RuntimeException("Failed to mixin to ItemProperties!");
    }
}
