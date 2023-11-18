package com.hidoni.customizableelytra;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class CustomizableElytraNeoForge {
    public CustomizableElytraNeoForge() {
        CustomizableElytra.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            CustomizableElytraNeoForgeClient.init();
        }
    }
}
