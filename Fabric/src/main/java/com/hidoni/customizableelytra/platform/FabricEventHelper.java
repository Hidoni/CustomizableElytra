package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IEventHelper;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;

import java.util.function.Consumer;

public class FabricEventHelper implements IEventHelper {
    @Override
    public void registerItemTintSourcesEventHandler(Consumer<ItemTintSourceRegistrar> handler) {
        handler.accept(ItemTintSources.ID_MAPPER::put);
    }

    @Override
    public void registerCauldronBehaviorEventHandler(Runnable handler) {
        handler.run();
    }

    @Override
    public void registerSelectItemModelPropertiesEventHandler(Consumer<SelectItemModelPropertiesRegistrar> handler) {
        handler.accept(SelectItemModelProperties.ID_MAPPER::put);
    }
}
