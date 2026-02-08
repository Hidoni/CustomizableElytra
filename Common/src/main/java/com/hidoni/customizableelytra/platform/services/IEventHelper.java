package com.hidoni.customizableelytra.platform.services;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

public interface IEventHelper {
    void registerItemTintSourcesEventHandler(Consumer<ItemTintSourceRegistrar> handler);

    void registerCauldronBehaviorEventHandler(Runnable handler);

    void registerSelectItemModelPropertiesEventHandler(Consumer<SelectItemModelPropertiesRegistrar> handler);

    interface ItemTintSourceRegistrar {
        void register(Identifier identifier, MapCodec<? extends ItemTintSource> itemTintSource);
    }

    interface SelectItemModelPropertiesRegistrar {
        void register(Identifier identifier, SelectItemModelProperty.Type<?, ?> type);
    }
}
