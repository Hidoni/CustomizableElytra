package com.hidoni.customizableelytra.data.client;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator gen) {
        super(gen, CustomizableElytra.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModItems.ELYTRA_WING.get(), "Elytra Wing");
        add(CustomizableElytraItem.LEFT_WING_TRANSLATION_KEY, "Left Wing:");
        add(CustomizableElytraItem.RIGHT_WING_TRANSLATION_KEY, "Right Wing:");
        add(CustomizableElytraItem.HIDDEN_CAPE_TRANSLATION_KEY, "Cape Pattern Hidden");
        for (DyeColor dye : DyeColor.values()) {
            add("block.minecraft.banner." + BannerPattern.BASE.getFilename() + '.' + dye.getName(), I18n.get("item.minecraft.firework_star." + dye.getName()) + " Base");
        }
    }
}
