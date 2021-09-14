package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.caelus.api.CaelusApi;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, CustomizableElytra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(CaelusApi.ELYTRA).add(ModItems.CUSTOMIZABLE_ELYTRA.get());
    }
}
