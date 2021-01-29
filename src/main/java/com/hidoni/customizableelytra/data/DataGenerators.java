package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.data.client.ModItemModelProvider;
import com.hidoni.customizableelytra.data.client.ModLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = CustomizableElytra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
    private DataGenerators()
    {

    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        CustomizableElytra.LOGGER.info("Beginning Data Generation Registration!");
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        generator.addProvider(new ModItemModelProvider(generator, fileHelper));
        generator.addProvider(new ModLanguageProvider(generator));
        generator.addProvider(new ModRecipeProvider(generator));
        CustomizableElytra.LOGGER.info("Finished Data Generation Registration!");
    }
}
