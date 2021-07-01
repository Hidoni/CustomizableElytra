package com.hidoni.customizableelytra.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ElytraCustomizationUtil
{
    public static ElytraCustomizationData getData(ItemStack elytraIn)
    {
        if (elytraIn.getChildTag("WingInfo") != null)
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Split, new SplitCustomizationHandler(elytraIn));
        }
        return getData(elytraIn.getTag());
    }

    /*
    * Before version 1.5.1, Crafting wings separately only stored the value of a tag, i.e. only the color sub-tag of
    * the display tag, after 1.5.1 this is no longer viable due to the ability to hide cape patterns, and data-fixers
    * don't work with mods, so this function had to be implemented as a fix.
    */
    public static CompoundNBT migrateOldSplitWingFormat(CompoundNBT wingIn)
    {
        if (wingIn == null)
        {
            return new CompoundNBT();
        }
        if (wingIn.contains("color", 99))
        {
            CompoundNBT newNBT = new CompoundNBT();
            CompoundNBT displayNBT = new CompoundNBT();
            displayNBT.putInt("color", wingIn.getInt("color"));
            newNBT.put("display", displayNBT);
            return newNBT;
        }
        else if (wingIn.contains("Patterns", 9))
        {
            CompoundNBT newNBT = new CompoundNBT();
            CompoundNBT blockEntityTagNBT = new CompoundNBT();
            blockEntityTagNBT.put("Patterns", wingIn.getList("Patterns", 10));
            blockEntityTagNBT.putInt("Base", wingIn.getInt("Base"));
            newNBT.put("BlockEntityTag", blockEntityTagNBT);
            return newNBT;
        }
        return wingIn;
    }

    public static ElytraCustomizationData getData(CompoundNBT wingIn)
    {
        CompoundNBT wingNBT = migrateOldSplitWingFormat(wingIn);
        if (wingNBT.contains("display"))
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Dye, new DyeCustomizationHandler(wingNBT));
        }
        else if (wingNBT.contains("BlockEntityTag"))
        {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Banner, new BannerCustomizationHandler(wingNBT));
        }
        return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.None, new CustomizationHandler(wingNBT.getBoolean("HideCapePattern")));
    }
}
