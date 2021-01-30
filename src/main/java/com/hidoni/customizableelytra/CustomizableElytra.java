package com.hidoni.customizableelytra;

import com.hidoni.customizableelytra.config.Config;
import com.hidoni.customizableelytra.events.ClientEventHandler;
import com.hidoni.customizableelytra.events.ElytraRenderHandler;
import com.hidoni.customizableelytra.setup.Registration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CustomizableElytra.MOD_ID)
public class CustomizableElytra
{
    public static final String MOD_ID = "customizableelytra";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean caelusLoaded = false;

    public CustomizableElytra() {
        Registration.register();

        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventHandler::handleClientLoading);

        Config.init();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        if (ModList.get().isLoaded("caelus"))
        {
            caelusLoaded = true;
            MinecraftForge.EVENT_BUS.register(new ElytraRenderHandler());
        }
    }
}
