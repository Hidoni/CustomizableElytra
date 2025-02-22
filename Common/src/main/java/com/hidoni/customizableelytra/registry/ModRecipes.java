package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.recipe.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {
    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<ElytraMiscellaneousCustomizationRecipe>> ELYTRA_MISCELLANEOUS_CUSTOMIZATION_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "elytra_miscellaneous_customization_recipe"), () -> new CustomRecipe.Serializer<>(ElytraMiscellaneousCustomizationRecipe::new));
    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<ElytraBannerRecipe>> ELYTRA_BANNER_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "elytra_banner_recipe"), () -> new CustomRecipe.Serializer<>(ElytraBannerRecipe::new));
    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<ElytraDyeRecipe>> ELYTRA_DYE_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "elytra_dye_recipe"), () -> new CustomRecipe.Serializer<>(ElytraDyeRecipe::new));
    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<SplitToWingsRecipe>> SPLIT_TO_WINGS_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "elytra_split_to_wings_recipe"), () -> new CustomRecipe.Serializer<>(SplitToWingsRecipe::new));
    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<CombineWingsRecipe>> COMBINE_WINGS_RECIPE = ModRegistries.RECIPE_SERIALIZER.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "combine_wings_recipe"), () -> new CustomRecipe.Serializer<>(CombineWingsRecipe::new));
    public static void register() {
    }
}
