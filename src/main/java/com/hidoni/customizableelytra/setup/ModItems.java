package com.hidoni.customizableelytra.setup;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.items.ElytraWingItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final RegistryObject<Item> CUSTOMIZABLE_ELYTRA = Registration.ITEMS.register("customizable_elytra", () -> new CustomizableElytraItem((new Item.Properties()).durability(Items.ELYTRA.getMaxDamage(null)).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ELYTRA_WING = Registration.ITEMS.register("elytra_wing", () -> new ElytraWingItem(new Item.Properties().durability(Items.ELYTRA.getMaxDamage(null)).setNoRepair().rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_MISC)));

    static void register() {
    }
}
