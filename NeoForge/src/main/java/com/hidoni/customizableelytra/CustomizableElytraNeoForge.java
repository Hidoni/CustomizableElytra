package com.hidoni.customizableelytra;

import com.hidoni.customizableelytra.platform.NeoForgeEventHelper;
import com.hidoni.customizableelytra.platform.NeoForgeRegistryHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class CustomizableElytraNeoForge {
    public CustomizableElytraNeoForge(IEventBus eventBus) {
        NeoForgeEventHelper.setEventBus(eventBus);
        NeoForgeRegistryHelper.setEventBus(eventBus);
        CustomizableElytra.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            CustomizableElytraNeoForgeClient.init();
        }
    }
}
