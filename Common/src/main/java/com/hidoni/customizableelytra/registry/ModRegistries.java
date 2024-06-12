package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.platform.Services;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRegistries {
    public static final RegistryProvider<Item> ITEM = Services.REGISTRY.getRegistry(Registries.ITEM);
    public static final RegistryProvider<RecipeSerializer<?>> RECIPE_SERIALIZER = Services.REGISTRY.getRegistry(Registries.RECIPE_SERIALIZER);
    public static final RegistryProvider<CreativeModeTab> CREATIVE_MODE_TABS = Services.REGISTRY.getRegistry(Registries.CREATIVE_MODE_TAB);
    public static final RegistryProvider<DataComponentType<?>> DATA_COMPONENT_TYPES = Services.REGISTRY.getRegistry(Registries.DATA_COMPONENT_TYPE);

    public static void register() {
        ModItems.register();
        ModRecipes.register();
        ModCreativeModeTabs.register();
        ModDataComponents.register();
    }
}
