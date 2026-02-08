package com.hidoni.customizableelytra;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    public static final String MOD_ID = "customizableelytra";
    public static final String MOD_NAME = "Customizable Elytra";

    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final Identifier ELYTRA_BANNER_SHEET = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/atlas/elytra_patterns.png");
    public static final Identifier ELYTRA_BANNER_ATLAS = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "elytra_patterns");
}