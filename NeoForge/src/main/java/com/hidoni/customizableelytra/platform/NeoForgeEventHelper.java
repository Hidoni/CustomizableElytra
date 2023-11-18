package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IEventHelper;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.function.Consumer;

public class NeoForgeEventHelper implements IEventHelper {
    @Override
    public void registerItemColorEventHandler(Consumer<ItemColorRegistrar> handler) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<RegisterColorHandlersEvent.Item>) item -> handler.accept(item::register));
    }

    @Override
    public void registerCauldronBehaviorEventHandler(Runnable handler) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<FMLCommonSetupEvent>) fmlCommonSetupEvent -> handler.run());
    }
}
