package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IRegistryHelper;
import com.hidoni.customizableelytra.registry.RegistryEntry;
import com.hidoni.customizableelytra.registry.RegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {
    @SuppressWarnings("unchecked")
    @Override
    public <T> RegistryProvider<T> getRegistry(ResourceKey<? extends Registry<T>> resourceKey) {
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(resourceKey.location());
        if (registry == null) {
            throw new RuntimeException("Registry " + resourceKey + " not found!");
        }
        return new RegistryProvider<>() {
            @Override
            public <I extends T> RegistryEntry<I> register(ResourceLocation location, Supplier<? extends I> entrySupplier) {
                I registered = Registry.register(registry, location, entrySupplier.get());
                return new RegistryEntry<>() {
                    private final ResourceKey<I> resourceKey = ResourceKey.create((ResourceKey<? extends Registry<I>>) registry.key(), location);

                    @Override
                    public ResourceLocation getResourceLocation() {
                        return location;
                    }

                    @Override
                    public @Nullable ResourceKey<I> getResourceKey() {
                        return resourceKey;
                    }

                    @Override
                    public Holder<I> getHolder() {
                        return (Holder<I>) registry.getHolderOrThrow((ResourceKey<T>) this.getResourceKey());
                    }

                    @Override
                    public I get() {
                        return registered;
                    }
                };
            }
        };
    }
}
