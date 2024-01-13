package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.recipe.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRecipes {
    public static final RegistryEntry<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<ElytraMiscellaneousCustomizationRecipe>> ELYTRA_MISCELLANEOUS_CUSTOMIZATION_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(new ResourceLocation(Constants.MOD_ID, "elytra_miscellaneous_customization_recipe"), () -> new SimpleCraftingRecipeSerializer<>(ElytraMiscellaneousCustomizationRecipe::new));
    public static final RegistryEntry<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<ElytraBannerRecipe>> ELYTRA_BANNER_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(new ResourceLocation(Constants.MOD_ID, "elytra_banner_recipe"), () -> new SimpleCraftingRecipeSerializer<>(ElytraBannerRecipe::new));
    public static final RegistryEntry<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<ElytraDyeRecipe>> ELYTRA_DYE_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(new ResourceLocation(Constants.MOD_ID, "elytra_dye_recipe"), () -> new SimpleCraftingRecipeSerializer<>(ElytraDyeRecipe::new));
    public static final RegistryEntry<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<SplitToWingsRecipe>> SPLIT_TO_WINGS_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(new ResourceLocation(Constants.MOD_ID, "elytra_split_to_wings_recipe"), () -> new SimpleCraftingRecipeSerializer<>(SplitToWingsRecipe::new));
    public static final RegistryEntry<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<CombineWingsRecipe>> COMBINE_WINGS_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(new ResourceLocation(Constants.MOD_ID, "combine_wings_recipe"), () -> new SimpleCraftingRecipeSerializer<>(CombineWingsRecipe::new));
    public static void register() {
    }
}
