package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IEventHelper;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.function.Consumer;

public class ForgeEventHelper implements IEventHelper {
    private static IEventBus eventBus;

    public static void setEventBus(IEventBus eventBus) {
        ForgeEventHelper.eventBus = eventBus;
    }

    @Override
    public void registerItemTintSourcesEventHandler(Consumer<ItemTintSourceRegistrar> handler) {
        handler.accept(ItemTintSources.ID_MAPPER::put);
    }

    @Override
    public void registerCauldronBehaviorEventHandler(Runnable handler) {
        eventBus.addListener((Consumer<FMLCommonSetupEvent>) fmlCommonSetupEvent -> handler.run());
    }

    @Override
    public void registerSelectItemModelPropertiesEventHandler(Consumer<SelectItemModelPropertiesRegistrar> handler) {
        handler.accept(SelectItemModelProperties.ID_MAPPER::put);
    }
}
