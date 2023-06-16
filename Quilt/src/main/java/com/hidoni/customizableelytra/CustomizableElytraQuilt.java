package com.hidoni.customizableelytra;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class CustomizableElytraQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        CustomizableElytra.init();
    }
}
