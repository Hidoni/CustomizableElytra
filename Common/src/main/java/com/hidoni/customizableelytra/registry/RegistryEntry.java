package com.hidoni.customizableelytra.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface RegistryEntry<T, I extends T> extends Supplier<I> {
    Identifier getIdentifier();

    @Nullable ResourceKey<T> getResourceKey();

    Holder<T> getHolder();
}
