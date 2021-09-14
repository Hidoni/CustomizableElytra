package com.hidoni.customizableelytra.util;

import net.minecraft.world.item.DyeColor;

public class ColorUtil {
    public static int convertDyeColorToInt(DyeColor dyeColor) {
        float[] colorValues = dyeColor.getTextureDiffuseColors();
        int red = (int) (colorValues[0] * 255) << 16;
        int green = (int) (colorValues[1] * 255) << 8;
        int blue = (int) (colorValues[2] * 255);
        return red | green | blue;
    }
}
