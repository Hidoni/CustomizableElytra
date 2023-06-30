package com.hidoni.customizableelytra.data.client;

import com.hidoni.customizableelytra.language.Translationkeys;
import com.hidoni.customizableelytra.registry.ModItems;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String modid) {
        super(output, modid, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModItems.ELYTRA_WING.get(), "Elytra Wing");
        add(Translationkeys.LEFT_WING_TRANSLATION_KEY, "Left Wing:");
        add(Translationkeys.RIGHT_WING_TRANSLATION_KEY, "Right Wing:");
        add(Translationkeys.HIDDEN_CAPE_TRANSLATION_KEY, "Cape Pattern Hidden");
        add(Translationkeys.GLOWING_WING_TRANSLATION_KEY, "Glowing");
        for (DyeColor dye : DyeColor.values()) {
            add("block.minecraft.banner." + BannerPatterns.BASE.location().getPath() + '.' + dye.getName(), I18n.get("item.minecraft.firework_star." + dye.getName()) + " Base");
        }
        add(Translationkeys.CREATIVE_TAB_TRANSLATION_KEY, "Customizable Elytra");
    }
}
