package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.data.client.ModItemModelGenerators;
import com.hidoni.customizableelytra.data.client.ModLanguageProvider;
import com.hidoni.customizableelytra.data.client.ModModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators() {

    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(true, new ModModelProvider(generator.getPackOutput()));
        generator.addProvider(true, new ModLanguageProvider(generator.getPackOutput(), Constants.MOD_ID));
        generator.addProvider(true, new ModRecipeProvider.Runner(generator.getPackOutput(), event.getLookupProvider()));
        ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(generator.getPackOutput(), event.getLookupProvider(), Constants.MOD_ID, event.getExistingFileHelper());
        generator.addProvider(true, modBlockTagsProvider);
        generator.addProvider(true, new ModItemTagsProvider(generator.getPackOutput(), event.getLookupProvider(), modBlockTagsProvider.contentsGetter(), Constants.MOD_ID, event.getExistingFileHelper()));
    }
}

