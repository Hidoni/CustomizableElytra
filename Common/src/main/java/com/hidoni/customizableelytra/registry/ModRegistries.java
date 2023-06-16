package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.platform.Services;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModRegistries {
    public static final RegistryProvider<Item> ITEM = Services.REGISTRY.getRegistry(Registries.ITEM);
    public static final RegistryProvider<RecipeSerializer<?>> RECIPE_SERIALIZER = Services.REGISTRY.getRegistry(Registries.RECIPE_SERIALIZER);

    public static void register() {
        ModItems.register();
        ModRecipes.register();
    }
}
