package com.hidoni.customizableelytra;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class CustomizableElytraForge {
    public CustomizableElytraForge() {
        CustomizableElytra.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CustomizableElytraForgeClient::init);
    }
}
