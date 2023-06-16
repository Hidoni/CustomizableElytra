package com.hidoni.customizableelytra.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface RegistryProvider<T> {
    <I extends T> RegistryEntry<I> register(ResourceLocation location, Supplier<? extends I> entrySupplier);
}
