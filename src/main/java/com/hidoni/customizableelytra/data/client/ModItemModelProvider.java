package com.hidoni.customizableelytra.data.client;

import com.hidoni.customizableelytra.CustomizableElytra;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider
{
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, CustomizableElytra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        withExistingParent("customizable_elytra", mcLoc("item/elytra")).override().predicate(new ResourceLocation("broken_elytra"), 1).model(new ModelFile.ExistingModelFile(mcLoc("item/broken_elytra"), existingFileHelper)).end();
    }
}
