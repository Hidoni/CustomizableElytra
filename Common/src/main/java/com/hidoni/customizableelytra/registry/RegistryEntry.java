package com.hidoni.customizableelytra.registry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface RegistryEntry<T> extends Supplier<T> {
    ResourceLocation getResourceLocation();

    @Nullable ResourceKey<T> getResourceKey();

    Holder<T> getHolder();
}
