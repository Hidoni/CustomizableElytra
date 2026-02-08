package com.hidoni.customizableelytra.registry;

import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public interface RegistryProvider<T> {
    <I extends T> RegistryEntry<T, I> register(Identifier identifier, Supplier<? extends I> entrySupplier);
}
