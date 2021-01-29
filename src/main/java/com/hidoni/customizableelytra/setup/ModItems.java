package com.hidoni.customizableelytra.setup;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;

public class ModItems
{
    public static final RegistryObject<Item> CUSTOMIZABLE_ELYTRA = Registration.ITEMS.register("customizable_elytra", () -> new CustomizableElytraItem((new Item.Properties()).maxDamage(432).rarity(Rarity.UNCOMMON)));

    static void register()
    {

    }
}
