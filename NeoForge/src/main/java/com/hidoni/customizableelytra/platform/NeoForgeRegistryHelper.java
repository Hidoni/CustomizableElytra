package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.platform.services.IRegistryHelper;
import com.hidoni.customizableelytra.registry.RegistryEntry;
import com.hidoni.customizableelytra.registry.RegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    @Override
    public <T> RegistryProvider<T> getRegistry(ResourceKey<? extends Registry<T>> resourceKey) {
        DeferredRegister<T> deferredRegister = DeferredRegister.create(resourceKey, Constants.MOD_ID);
        deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus());
        return new RegistryProvider<>() {
            @Override
            public <I extends T> RegistryEntry<I> register(ResourceLocation location, Supplier<? extends I> entrySupplier) {
                RegistryObject<I> registered = deferredRegister.register(location.getPath(), entrySupplier);
                return new RegistryEntry<>() {
                    @Override
                    public ResourceLocation getResourceLocation() {
                        return registered.getId();
                    }

                    @Override
                    public @Nullable ResourceKey<I> getResourceKey() {
                        return registered.getKey();
                    }

                    @Override
                    public Holder<I> getHolder() {
                        return registered.getHolder().orElseThrow(() -> new RuntimeException("No holder present for " + this.getResourceLocation()));
                    }

                    @Override
                    public I get() {
                        return registered.get();
                    }
                };
            }
        };
    }
}
