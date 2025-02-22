package com.hidoni.customizableelytra;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public class ElytraUtils {
    public static boolean isElytra(ItemStack itemStack) {
        return itemStack.has(DataComponents.GLIDER);
    }
}
