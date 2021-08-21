package com.hidoni.customizableelytra.setup;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.items.ElytraWingItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {
    public static final RegistryObject<Item> CUSTOMIZABLE_ELYTRA = Registration.ITEMS.register("customizable_elytra", () -> new CustomizableElytraItem((new Item.Properties()).maxDamage(432).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ELYTRA_WING = Registration.ITEMS.register("elytra_wing", () -> new ElytraWingItem(new Item.Properties().rarity(Rarity.UNCOMMON).group(ItemGroup.MISC)));

    static void register() {

    }
}
