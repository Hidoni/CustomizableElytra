package com.hidoni.customizableelytra.data.client;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.items.CustomizableElytraItem;
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
        add(ModItems.ELYTRA_WING.get(), "Elytra Wing");
        add(CustomizableElytraItem.LEFT_WING_TRANSLATION_KEY, "Left Wing:");
        add(CustomizableElytraItem.RIGHT_WING_TRANSLATION_KEY, "Right Wing:");
        add(CustomizableElytraItem.HIDDEN_CAPE_TRANSLATION_KEY, "Cape Pattern Hidden");
    }
}
