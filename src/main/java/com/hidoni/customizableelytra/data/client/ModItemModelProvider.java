package com.hidoni.customizableelytra.data.client;

import com.hidoni.customizableelytra.CustomizableElytra;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, CustomizableElytra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        getBuilder("broken_customizable_elytra").parent(itemGenerated).texture("layer0", "item/broken_elytra_left").texture("layer1", "item/broken_elytra_right");
        getBuilder("customizable_elytra").parent(itemGenerated).texture("layer0", "item/elytra_left").texture("layer1", "item/elytra_right").override().predicate(new ResourceLocation("broken_elytra"), 1).model(new ModelFile.UncheckedModelFile(new ResourceLocation(CustomizableElytra.MOD_ID, "item/broken_customizable_elytra"))).end();
        getBuilder("elytra_wing").parent(itemGenerated).texture("layer0", "item/elytra_wing");
    }
}
