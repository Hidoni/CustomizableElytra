package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.recipe.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {
    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<ElytraMiscellaneousCustomizationRecipe>> ELYTRA_MISCELLANEOUS_CUSTOMIZATION_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "elytra_miscellaneous_customization_recipe"), () -> ElytraMiscellaneousCustomizationRecipe.SERIALIZER);
    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<ElytraBannerRecipe>> ELYTRA_BANNER_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "elytra_banner_recipe"), () -> ElytraBannerRecipe.SERIALIZER);
    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<ElytraDyeRecipe>> ELYTRA_DYE_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "elytra_dye_recipe"), () -> ElytraDyeRecipe.SERIALIZER);
    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<SplitToWingsRecipe>> SPLIT_TO_WINGS_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "elytra_split_to_wings_recipe"), () -> SplitToWingsRecipe.SERIALIZER);
    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<CombineWingsRecipe>> COMBINE_WINGS_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "combine_wings_recipe"), () -> CombineWingsRecipe.SERIALIZER);

    public static void register() {
    }
}
