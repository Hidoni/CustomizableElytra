package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.data.client.ModItemModelProvider;
import com.hidoni.customizableelytra.data.client.ModLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
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
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        generator.addProvider(true, new ModItemModelProvider(packOutput, fileHelper));
        generator.addProvider(true, new ModLanguageProvider(packOutput));
        generator.addProvider(true, new ModRecipeProvider(packOutput));
        CustomizableElytra.LOGGER.info("Finished Data Generation Registration!");
    }
}
