package com.hidoni.customizableelytra;

import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ElytraUtils {
    public static boolean isElytra(Item item) {
        return item instanceof ElytraItem;
    }

    public static boolean isElytra(ItemStack itemStack) {
        return isElytra(itemStack.getItem());
    }
}
