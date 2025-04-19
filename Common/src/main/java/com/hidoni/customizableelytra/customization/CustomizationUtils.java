package com.hidoni.customizableelytra.customization;

import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import com.hidoni.customizableelytra.registry.ModItems;
import net.minecraft.world.item.ItemStack;

public class CustomizationUtils {
    public static ElytraCustomization getElytraCustomization(ItemStack elytra) {
        if (!elytra.has(ModDataComponents.ELYTRA_CUSTOMIZATION.get())) {
            ItemStack emptyWing = new ItemStack(ModItems.ELYTRA_WING.get());
            return new ElytraCustomization(emptyWing, emptyWing);
        }
        return elytra.get(ModDataComponents.ELYTRA_CUSTOMIZATION.get());
    }
}