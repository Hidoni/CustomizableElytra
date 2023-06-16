package com.hidoni.customizableelytra.platform.services;

import com.hidoni.customizableelytra.registry.RegistryProvider;
import net.minecraft.resources.ResourceKey;

public interface IRegistryHelper {
    <T> RegistryProvider<T> getRegistry(ResourceKey<? extends net.minecraft.core.Registry<T>> resourceKey);
}
