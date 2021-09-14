package com.hidoni.customizableelytra.items;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ElytraWingItem extends Item implements DyeableLeatherItem {
    public ElytraWingItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTagElement("display");
        if (compoundnbt != null) {
            return compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 16777215;
        }
        compoundnbt = stack.getTagElement("BlockEntityTag");
        if (compoundnbt != null) {
            return DyeColor.byId(compoundnbt.getInt("Base")).getColorValue();
        }
        return 16777215;
    }

    @Override
    public boolean hasCustomColor(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTagElement("BlockEntityTag");
        return DyeableLeatherItem.super.hasCustomColor(stack) || compoundnbt != null;
    }

    @Override
    public void clearColor(ItemStack stack) {
        DyeableLeatherItem.super.clearColor(stack);
        stack.getOrCreateTag().remove("HideCapePattern");
        stack.removeTagKey("BlockEntityTag");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CustomizableElytraItem.applyTooltip(tooltip, flagIn, stack.getTag(), true);
    }
}
