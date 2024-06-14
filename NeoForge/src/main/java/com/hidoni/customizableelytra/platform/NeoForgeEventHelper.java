package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IEventHelper;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.function.Consumer;

public class NeoForgeEventHelper implements IEventHelper {
    private static IEventBus eventBus;

    public static void setEventBus(IEventBus eventBus) {
        NeoForgeEventHelper.eventBus = eventBus;
    }

    @Override
    public void registerItemColorEventHandler(Consumer<ItemColorRegistrar> handler) {
        eventBus.addListener((Consumer<RegisterColorHandlersEvent.Item>) item -> handler.accept(item::register));
    }

    @Override
    public void registerCauldronBehaviorEventHandler(Runnable handler) {
        eventBus.addListener((Consumer<FMLCommonSetupEvent>) fmlCommonSetupEvent -> handler.run());
    }

    @Override
    public void registerItemPropertiesEventHandler(Consumer<ItemPropertiesRegistrar> handler) {
        eventBus.addListener((Consumer<FMLClientSetupEvent>) fmlClientSetupEvent -> handler.accept(ItemProperties::registerGeneric));
    }
}
