package com.hidoni.customizableelytra.util;

import com.hidoni.customizableelytra.CustomizableElytra;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class ElytraCustomizationUtil {
    public static final ResourceLocation ELYTRA_TEXTURE_ATLAS = new ResourceLocation(CustomizableElytra.MOD_ID, "textures/atlas/elytra_patterns.png");

    public static ElytraCustomizationData getData(ItemStack elytraIn) {
        if (elytraIn.getTagElement("WingInfo") != null) {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Split, new SplitCustomizationHandler(elytraIn));
        }
        return getData(elytraIn.getTag());
    }

    /*
     * Before version 1.5.1, Crafting wings separately only stored the value of a tag, i.e. only the color sub-tag of
     * the display tag, after 1.5.1 this is no longer viable due to the ability to hide cape patterns, and data-fixers
     * don't work with mods, so this function had to be implemented as a fix.
     */
    public static CompoundTag migrateOldSplitWingFormat(CompoundTag wingIn) {
        if (wingIn == null) {
            return new CompoundTag();
        }
        if (wingIn.contains("color", 99)) {
            CompoundTag newNBT = new CompoundTag();
            CompoundTag displayNBT = new CompoundTag();
            displayNBT.putInt("color", wingIn.getInt("color"));
            newNBT.put("display", displayNBT);
            return newNBT;
        } else if (wingIn.contains("Patterns", 9)) {
            CompoundTag newNBT = new CompoundTag();
            CompoundTag blockEntityTagNBT = new CompoundTag();
            blockEntityTagNBT.put("Patterns", wingIn.getList("Patterns", 10));
            blockEntityTagNBT.putInt("Base", wingIn.getInt("Base"));
            newNBT.put("BlockEntityTag", blockEntityTagNBT);
            return newNBT;
        }
        return wingIn;
    }

    public static ElytraCustomizationData getData(CompoundTag wingIn) {
        CompoundTag wingNBT = migrateOldSplitWingFormat(wingIn);
        if (wingNBT.contains("display") && wingNBT.getCompound("display").contains("color", 99)) {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Dye, new DyeCustomizationHandler(wingNBT));
        } else if (wingNBT.contains("BlockEntityTag")) {
            return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.Banner, new BannerCustomizationHandler(wingNBT));
        }
        return new ElytraCustomizationData(ElytraCustomizationData.CustomizationType.None, new CustomizationHandler(wingNBT.getBoolean("HideCapePattern"), wingNBT.getInt("WingLightLevel")));
    }
}
