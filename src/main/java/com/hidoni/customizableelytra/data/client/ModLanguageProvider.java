package com.hidoni.customizableelytra.data.client;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider
{
    public ModLanguageProvider(DataGenerator gen)
    {
        super(gen, CustomizableElytra.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        // add(ModItems.CUSTOMIZABLE_ELYTRA.get(), "Elytra");
    }
}
