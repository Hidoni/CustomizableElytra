package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.item.ElytraWingItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {
    public static final Identifier ELYTRA_WING_RESOURCE_LOCATION = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "elytra_wing");
    public static final RegistryEntry<Item, ? extends Item> ELYTRA_WING = ModRegistries.ITEM.register(ELYTRA_WING_RESOURCE_LOCATION, () -> new ElytraWingItem(new Item.Properties().durability(432).rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, ELYTRA_WING_RESOURCE_LOCATION))));

    public static void register() {
    }
}
