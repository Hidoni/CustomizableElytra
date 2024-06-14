package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.item.ElytraWingItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import java.util.Objects;

public class ModItems {
    public static final RegistryEntry<Item, ? extends Item> ELYTRA_WING = ModRegistries.ITEM.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "elytra_wing"), () -> new ElytraWingItem(new Item.Properties().durability(Objects.requireNonNull(Items.ELYTRA.components().get(DataComponents.MAX_DAMAGE))).rarity(Rarity.UNCOMMON)));

    public static void register() {
    }
}
