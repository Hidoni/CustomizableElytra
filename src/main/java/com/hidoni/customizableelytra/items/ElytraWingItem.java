package com.hidoni.customizableelytra.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ElytraWingItem extends Item implements IDyeableArmorItem {
    public ElytraWingItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getChildTag("display");
        if (compoundnbt != null) {
            return compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 16777215;
        }
        compoundnbt = stack.getChildTag("BlockEntityTag");
        if (compoundnbt != null) {
            return DyeColor.byId(compoundnbt.getInt("Base")).getColorValue();
        }
        return 16777215;
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
        return IDyeableArmorItem.super.hasColor(stack) || compoundnbt != null;
    }

    @Override
    public void removeColor(ItemStack stack) {
        IDyeableArmorItem.super.removeColor(stack);
        stack.getOrCreateTag().remove("HideCapePattern");
        stack.removeChildTag("BlockEntityTag");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getOrCreateTag().getBoolean("HideCapePattern")) {
            tooltip.add(new TranslationTextComponent(CustomizableElytraItem.HIDDEN_CAPE_TRANSLATION_KEY).mergeStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
        }
        BannerItem.appendHoverTextFromTileEntityTag(stack, tooltip);
    }
}
