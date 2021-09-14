package com.hidoni.customizableelytra.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.item.Item.Properties;

public class ElytraWingItem extends Item implements IDyeableArmorItem {
    public ElytraWingItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTagElement("display");
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
        CompoundNBT compoundnbt = stack.getTagElement("BlockEntityTag");
        return IDyeableArmorItem.super.hasCustomColor(stack) || compoundnbt != null;
    }

    @Override
    public void clearColor(ItemStack stack) {
        IDyeableArmorItem.super.clearColor(stack);
        stack.getOrCreateTag().remove("HideCapePattern");
        stack.removeTagKey("BlockEntityTag");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CustomizableElytraItem.applyTooltip(tooltip, flagIn, stack.getTag(), true);
    }
}
