package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.registry.ModRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_MISCELLANEOUS_CUSTOMIZATION_RECIPE.get()).save(output, ModRecipes.ELYTRA_MISCELLANEOUS_CUSTOMIZATION_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(ModRecipes.SPLIT_TO_WINGS_RECIPE.get()).save(output, ModRecipes.SPLIT_TO_WINGS_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(ModRecipes.COMBINE_WINGS_RECIPE.get()).save(output, ModRecipes.COMBINE_WINGS_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_BANNER_RECIPE.get()).save(output, ModRecipes.ELYTRA_BANNER_RECIPE.getResourceLocation().getPath());
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_DYE_RECIPE.get()).save(output, ModRecipes.ELYTRA_DYE_RECIPE.getResourceLocation().getPath());
    }
}
