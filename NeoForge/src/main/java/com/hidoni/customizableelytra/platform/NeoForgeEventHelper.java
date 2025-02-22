package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IEventHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

import java.util.function.Consumer;

public class NeoForgeEventHelper implements IEventHelper {
    private static IEventBus eventBus;

    public static void setEventBus(IEventBus eventBus) {
        NeoForgeEventHelper.eventBus = eventBus;
    }

    @Override
    public void registerItemTintSourcesEventHandler(Consumer<ItemTintSourceRegistrar> handler) {
        eventBus.addListener((Consumer<RegisterColorHandlersEvent.ItemTintSources>) item -> handler.accept(item::register));
    }

    @Override
    public void registerCauldronBehaviorEventHandler(Runnable handler) {
        eventBus.addListener((Consumer<FMLCommonSetupEvent>) fmlCommonSetupEvent -> handler.run());
    }

    @Override
    public void registerSelectItemModelPropertiesEventHandler(Consumer<SelectItemModelPropertiesRegistrar> handler) {
        eventBus.addListener((Consumer<RegisterSelectItemModelPropertyEvent>) registerSelectItemModelPropertyEvent -> handler.accept(registerSelectItemModelPropertyEvent::register));
    }
}
