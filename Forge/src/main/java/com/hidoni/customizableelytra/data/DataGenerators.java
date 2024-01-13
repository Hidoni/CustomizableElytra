package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.data.client.ModItemModelProvider;
import com.hidoni.customizableelytra.data.client.ModLanguageProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators() {

    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(true, new ModItemModelProvider(generator.getPackOutput(), Constants.MOD_ID, event.getExistingFileHelper()));
        generator.addProvider(true, new ModLanguageProvider(generator.getPackOutput(), Constants.MOD_ID));
        generator.addProvider(true, new ModRecipeProvider(generator.getPackOutput()));
        ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(generator.getPackOutput(), event.getLookupProvider(), Constants.MOD_ID, event.getExistingFileHelper());
        generator.addProvider(true, modBlockTagsProvider);
        generator.addProvider(true, new ModItemTagsProvider(generator.getPackOutput(), event.getLookupProvider(), modBlockTagsProvider.contentsGetter(), Constants.MOD_ID, event.getExistingFileHelper()));
    }
}

