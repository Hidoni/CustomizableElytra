package com.hidoni.customizableelytra.platform.services;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public interface IEventHelper {
    void registerItemColorEventHandler(Consumer<ItemColorRegistrar> handler);

    void registerCauldronBehaviorEventHandler(Runnable handler);

    interface ItemColorRegistrar {
        void register(ItemColor provider, ItemLike... items);
    }
}
