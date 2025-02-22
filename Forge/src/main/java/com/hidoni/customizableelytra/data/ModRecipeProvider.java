package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.recipe.*;
import com.hidoni.customizableelytra.registry.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput recipeOutput) {
        super(lookupProvider, recipeOutput);
    }

    @Override
    protected void buildRecipes() {
        SpecialRecipeBuilder.special(ElytraMiscellaneousCustomizationRecipe::new).save(output, ModRecipes.ELYTRA_MISCELLANEOUS_CUSTOMIZATION_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(SplitToWingsRecipe::new).save(output, ModRecipes.SPLIT_TO_WINGS_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(CombineWingsRecipe::new).save(output, ModRecipes.COMBINE_WINGS_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(ElytraBannerRecipe::new).save(output, ModRecipes.ELYTRA_BANNER_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(ElytraDyeRecipe::new).save(output, ModRecipes.ELYTRA_DYE_RECIPE.getResourceLocation().getPath());
    }

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(packOutput, lookupProvider);
        }
        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }
        @Override
        public @NotNull String getName() {
            return Constants.MOD_NAME + ModRecipeProvider.class.getSimpleName();
        }
    }
}
