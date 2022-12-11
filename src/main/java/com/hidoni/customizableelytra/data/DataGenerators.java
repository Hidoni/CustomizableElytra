package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.data.client.ModItemModelProvider;
import com.hidoni.customizableelytra.data.client.ModLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CustomizableElytra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators() {

    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        CustomizableElytra.LOGGER.info("Beginning Data Generation Registration!");
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        generator.addProvider(true, new ModItemModelProvider(generator, fileHelper));
        generator.addProvider(true, new ModLanguageProvider(generator));
        generator.addProvider(true, new ModRecipeProvider(generator.getPackOutput()));
        CustomizableElytra.LOGGER.info("Finished Data Generation Registration!");
    }
}
