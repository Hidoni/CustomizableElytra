package com.hidoni.customizableelytra;

import com.hidoni.customizableelytra.platform.ForgeEventHelper;
import com.hidoni.customizableelytra.platform.ForgeRegistryHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class CustomizableElytraForge {
    public CustomizableElytraForge(FMLJavaModLoadingContext modLoadingContext) {
        ForgeEventHelper.setEventBus(modLoadingContext.getModEventBus());
        ForgeRegistryHelper.setEventBus(modLoadingContext.getModEventBus());
        CustomizableElytra.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CustomizableElytraForgeClient::init);
    }
}
