package com.hidoni.customizableelytra.setup;

import com.hidoni.customizableelytra.crafting.*;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;

public class ModRecipes {
    public static final RegistryObject<SimpleRecipeSerializer<ElytraDyeRecipe>> ELYTRA_DYE_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_dye_recipe", () -> new SimpleRecipeSerializer<>(ElytraDyeRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<ElytraBannerRecipe>> ELYTRA_BANNER_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_banner_recipe", () -> new SimpleRecipeSerializer<>(ElytraBannerRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<ElytraWingCombinationRecipe>> ELYTRA_WING_COMBINATION_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_wing_combination_recipe", () -> new SimpleRecipeSerializer<>(ElytraWingCombinationRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<ElytraToVanillaRecipe>> ELYTRA_TO_VANILLA_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_to_vanilla_recipe", () -> new SimpleRecipeSerializer<>(ElytraToVanillaRecipe::new));
    public static final RegistryObject<SimpleRecipeSerializer<ElytraHideCapeRecipe>> ELYTRA_HIDE_CAPE_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_hide_cape_recipe", () -> new SimpleRecipeSerializer<>(ElytraHideCapeRecipe::new));

    static void register() {

    }
}
