package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IEventHelper;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

public class ForgeEventHelper implements IEventHelper {
    @Override
    public void registerItemColorEventHandler(Consumer<ItemColorRegistrar> handler) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<RegisterColorHandlersEvent.Item>) item -> handler.accept(item::register));
    }

    @Override
    public void registerCauldronBehaviorEventHandler(Runnable handler) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<FMLCommonSetupEvent>) fmlCommonSetupEvent -> handler.run());
    }
}
