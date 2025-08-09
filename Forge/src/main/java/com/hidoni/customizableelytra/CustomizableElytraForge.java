package com.hidoni.customizableelytra;

import com.hidoni.customizableelytra.platform.ForgeEventHelper;
import com.hidoni.customizableelytra.platform.ForgeRegistryHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class CustomizableElytraForge {
    public CustomizableElytraForge(FMLJavaModLoadingContext modLoadingContext) {
        ForgeEventHelper.setBusGroup(modLoadingContext.getModBusGroup());
        ForgeRegistryHelper.setBusGroup(modLoadingContext.getModBusGroup());
        CustomizableElytra.init();

        if (FMLEnvironment.dist.isClient()) {
            CustomizableElytraForgeClient.init();
        }
    }
}
