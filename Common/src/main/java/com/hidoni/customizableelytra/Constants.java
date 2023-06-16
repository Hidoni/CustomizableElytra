package com.hidoni.customizableelytra;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    public static final String MOD_ID = "customizableelytra";
    public static final String MOD_NAME = "Customizable Elytra";

    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final ResourceLocation ELYTRA_BANNER_SHEET = new ResourceLocation(Constants.MOD_ID, "textures/atlas/elytra_patterns.png");
    public static final ResourceLocation ELYTRA_BANNER_ATLAS = new ResourceLocation(Constants.MOD_ID, "elytra_patterns");
    public static final String ELYTRA_CUSTOMIZATION_KEY = Constants.MOD_ID + ":customization";
    public static final String ELYTRA_RIGHT_WING_CUSTOMIZATION_KEY = "right";
    public static final String ELYTRA_LEFT_WING_CUSTOMIZATION_KEY = "left";
    public static final ResourceLocation ELYTRA_LEFT_WING_TRIM_TYPE_PREDICATE = new ResourceLocation(Constants.MOD_ID, "left_wing_trim_type");
    public static final ResourceLocation ELYTRA_RIGHT_WING_TRIM_TYPE_PREDICATE = new ResourceLocation(Constants.MOD_ID, "right_wing_trim_type");
}