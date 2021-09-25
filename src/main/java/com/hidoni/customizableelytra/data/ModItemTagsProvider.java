package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.CustomizableElytra;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, CustomizableElytra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
