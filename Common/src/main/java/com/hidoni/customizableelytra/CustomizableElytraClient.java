package com.hidoni.customizableelytra;

import com.hidoni.customizableelytra.client.CustomizableElytraDyeItemTintSource;
import com.hidoni.customizableelytra.client.CustomizableElytraTrimMaterialProperty;
import com.hidoni.customizableelytra.platform.Services;
import net.minecraft.resources.ResourceLocation;

public class CustomizableElytraClient {
    public static void init() {
        Services.EVENT.registerSelectItemModelPropertiesEventHandler((itemProperties) -> {
            itemProperties.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "trim"), CustomizableElytraTrimMaterialProperty.TYPE);
        });
        Services.EVENT.registerItemTintSourcesEventHandler((itemTintSources -> {
            itemTintSources.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "dye"), CustomizableElytraDyeItemTintSource.MAP_CODEC);
        }));
    }
}
