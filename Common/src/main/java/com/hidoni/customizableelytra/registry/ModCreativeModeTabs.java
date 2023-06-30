package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.language.TranslationKeys;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    public static final RegistryEntry<CreativeModeTab> CUSTOMIZABLE_ELYTRA_TAB = ModRegistries.CREATIVE_MODE_TABS.register(new ResourceLocation(Constants.MOD_ID, "creative_tab"), () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ModItems.ELYTRA_WING.get()))
            .title(Component.translatable(TranslationKeys.CREATIVE_TAB_TRANSLATION_KEY))
            .displayItems((itemDisplayParameters, output) -> output.accept(ModItems.ELYTRA_WING.get()))
            .build()
    );

    public static void register() {
    }
}
