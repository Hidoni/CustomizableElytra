package com.hidoni.customizableelytra.customization;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record ElytraCustomization(ItemStack leftWing, ItemStack rightWing) {
    public boolean isCustomized() {
        return ((CustomizableElytraItem) leftWing.getItem()).isCustomized(leftWing) || ((CustomizableElytraItem) rightWing.getItem()).isCustomized(rightWing);
    }

    public CompoundTag asElytraTag() {
        CompoundTag tag = new CompoundTag();
        if (!leftWing.isEmpty()) {
            tag.put(Constants.ELYTRA_LEFT_WING_CUSTOMIZATION_KEY, leftWing.save(new CompoundTag()));
        }
        if (!rightWing.isEmpty()) {
            tag.put(Constants.ELYTRA_RIGHT_WING_CUSTOMIZATION_KEY, rightWing.save(new CompoundTag()));
        }
        return tag;
    }

    public void saveToElytra(ItemStack elytraStack) {
        elytraStack.getOrCreateTag().put(Constants.ELYTRA_CUSTOMIZATION_KEY, this.asElytraTag());
    }
}
