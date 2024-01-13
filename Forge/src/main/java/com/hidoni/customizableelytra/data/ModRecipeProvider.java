package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.recipe.*;
import com.hidoni.customizableelytra.registry.ModRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import org.jetbrains.annotations.NotNull;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        SpecialRecipeBuilder.special(ElytraMiscellaneousCustomizationRecipe::new).save(output, ModRecipes.ELYTRA_MISCELLANEOUS_CUSTOMIZATION_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(SplitToWingsRecipe::new).save(output, ModRecipes.SPLIT_TO_WINGS_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(CombineWingsRecipe::new).save(output, ModRecipes.COMBINE_WINGS_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(ElytraBannerRecipe::new).save(output, ModRecipes.ELYTRA_BANNER_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(ElytraDyeRecipe::new).save(output, ModRecipes.ELYTRA_DYE_RECIPE.getResourceLocation().getPath());
    }
}
