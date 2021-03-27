package com.hidoni.customizableelytra.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ElytraCustomizationUtil
{
    public static ElytraCustomizationData getData(ItemStack elytraIn)
    {
        if (elytraIn.getChildTag("display") != null)
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Dye, new DyeCustomizationHandler(elytraIn));
        }
        else if (elytraIn.getChildTag("BlockEntityTag") != null)
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Banner, new BannerCustomizationHandler(elytraIn));
        }
        else if (elytraIn.getChildTag("WingInfo") != null)
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Split, new SplitCustomizationHandler(elytraIn));
        }
        return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.None, new CustomizationHandler());
    }

    public static ElytraCustomizationData getData(CompoundNBT wingIn)
    {
        if (wingIn == null)
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.None, new CustomizationHandler());
        }
        if (wingIn.contains("color"))
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Dye, new DyeCustomizationHandler(wingIn));
        }
        else if (wingIn.contains("Patterns"))
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Banner, new BannerCustomizationHandler(wingIn));
        }
        return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.None, new CustomizationHandler());
    }
}
