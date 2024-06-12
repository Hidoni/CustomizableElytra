package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IEventHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.item.ItemProperties;

import java.util.function.Consumer;

public class FabricEventHelper implements IEventHelper {
    @Override
    public void registerItemColorEventHandler(Consumer<ItemColorRegistrar> handler) {
        handler.accept(ColorProviderRegistry.ITEM::register);
    }

    @Override
    public void registerCauldronBehaviorEventHandler(Runnable handler) {
        handler.run();
    }

    @Override
    public void registerItemPropertiesEventHandler(Consumer<ItemPropertiesRegistrar> handler) {
        handler.accept(ItemProperties::registerGeneric);
    }
}
