package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IEventHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

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
}
