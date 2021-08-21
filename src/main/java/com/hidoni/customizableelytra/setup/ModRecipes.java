package com.hidoni.customizableelytra.setup;

import com.hidoni.customizableelytra.crafting.*;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;

public class ModRecipes {
    public static final RegistryObject<SpecialRecipeSerializer<ElytraDyeRecipe>> ELYTRA_DYE_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_dye_recipe", () -> new SpecialRecipeSerializer<>(ElytraDyeRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<ElytraBannerRecipe>> ELYTRA_BANNER_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_banner_recipe", () -> new SpecialRecipeSerializer<>(ElytraBannerRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<ElytraWingCombinationRecipe>> ELYTRA_WING_COMBINATION_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_wing_combination_recipe", () -> new SpecialRecipeSerializer<>(ElytraWingCombinationRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<ElytraToVanillaRecipe>> ELYTRA_TO_VANILLA_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_to_vanilla_recipe", () -> new SpecialRecipeSerializer<>(ElytraToVanillaRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<ElytraHideCapeRecipe>> ELYTRA_HIDE_CAPE_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_hide_cape_recipe", () -> new SpecialRecipeSerializer<>(ElytraHideCapeRecipe::new));

    static void register() {

    }
}
