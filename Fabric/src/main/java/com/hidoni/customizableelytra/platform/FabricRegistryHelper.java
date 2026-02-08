package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IRegistryHelper;
import com.hidoni.customizableelytra.registry.RegistryEntry;
import com.hidoni.customizableelytra.registry.RegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {
    @SuppressWarnings("unchecked")
    @Override
    public <T> RegistryProvider<T> getRegistry(ResourceKey<? extends Registry<T>> resourceKey) {
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(resourceKey.identifier()).orElseThrow(() -> new RuntimeException("Registry " + resourceKey + " not found!")).value();
        return new RegistryProvider<>() {
            @Override
            public <I extends T> RegistryEntry<T, I> register(Identifier identifier, Supplier<? extends I> entrySupplier) {
                I registered = Registry.register(registry, identifier, entrySupplier.get());
                return new RegistryEntry<>() {
                    private final ResourceKey<T> resourceKey = ResourceKey.create(registry.key(), identifier);

                    @Override
                    public Identifier getIdentifier() {
                        return identifier;
                    }

                    @Override
                    public @Nullable ResourceKey<T> getResourceKey() {
                        return resourceKey;
                    }

                    @Override
                    public Holder<T> getHolder() {
                        return registry.getOrThrow(this.getResourceKey());
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
