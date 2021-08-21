package com.hidoni.customizableelytra;

import com.hidoni.customizableelytra.config.Config;
import com.hidoni.customizableelytra.events.ClientEventHandler;
import com.hidoni.customizableelytra.events.ElytraRenderHandler;
import com.hidoni.customizableelytra.events.EntityConstructingHandler;
import com.hidoni.customizableelytra.setup.Registration;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CustomizableElytra.MOD_ID)
public class CustomizableElytra {
    public static final String MOD_ID = "customizableelytra";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean caelusLoaded = false;
    public static boolean curiosLoaded = false;
    public static boolean aetherLoaded = false;

    public CustomizableElytra() {
        Registration.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientLoadingEvent);

        Config.init();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        if (ModList.get().isLoaded("caelus")) {
            caelusLoaded = true;
            MinecraftForge.EVENT_BUS.register(new ElytraRenderHandler());
        } else {
            // If Caelus isn't loaded, this tag needs to be created to prevent crashing
            ForgeTagHandler.createOptionalTag(ForgeRegistries.ITEMS, new ResourceLocation("forge", "elytra"));
        }
        curiosLoaded = ModList.get().isLoaded("curios");
        MinecraftForge.EVENT_BUS.register(new EntityConstructingHandler());
        aetherLoaded = ModList.get().isLoaded("aether");
    }

    private void clientLoadingEvent(final FMLClientSetupEvent event) {
        ClientEventHandler.handleClientLoading(event);
    }
}
