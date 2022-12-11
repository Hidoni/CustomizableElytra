package com.hidoni.customizableelytra.setup;

import com.hidoni.customizableelytra.crafting.*;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final RegistryObject<SimpleCraftingRecipeSerializer<ElytraDyeRecipe>> ELYTRA_DYE_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_dye_recipe", () -> new SimpleCraftingRecipeSerializer<>(ElytraDyeRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<ElytraBannerRecipe>> ELYTRA_BANNER_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_banner_recipe", () -> new SimpleCraftingRecipeSerializer<>(ElytraBannerRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<ElytraWingCombinationRecipe>> ELYTRA_WING_COMBINATION_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_wing_combination_recipe", () -> new SimpleCraftingRecipeSerializer<>(ElytraWingCombinationRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<SplitToWingRecipe>> SPLIT_TO_WING_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_wing_split_recipe", () -> new SimpleCraftingRecipeSerializer<>(SplitToWingRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<ElytraHideCapeRecipe>> ELYTRA_HIDE_CAPE_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_hide_cape_recipe", () -> new SimpleCraftingRecipeSerializer<>(ElytraHideCapeRecipe::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<ElytraWingGlowRecipe>> ELYTRA_WING_GLOW_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_wing_glow_recipe", () -> new SimpleCraftingRecipeSerializer<>(ElytraWingGlowRecipe::new));

    static void register() {

    }
}
