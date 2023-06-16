package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.item.ElytraWingItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class ModItems {
    public static final RegistryEntry<Item> ELYTRA_WING = ModRegistries.ITEM.register(new ResourceLocation(Constants.MOD_ID, "elytra_wing"), () -> new ElytraWingItem(new Item.Properties().durability(Items.ELYTRA.getMaxDamage()).rarity(Rarity.UNCOMMON)));

    public static void register() {
    }
}
