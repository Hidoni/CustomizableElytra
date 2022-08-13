package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_DYE_RECIPE.get()).save(consumer, "elytra_dye_recipe");
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_BANNER_RECIPE.get()).save(consumer, "elytra_banner_recipe");
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_WING_COMBINATION_RECIPE.get()).save(consumer, "elytra_wing_combination_recipe");
        SpecialRecipeBuilder.special(ModRecipes.SPLIT_TO_WING_RECIPE.get()).save(consumer, "elytra_wing_split_recipe");
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_HIDE_CAPE_RECIPE.get()).save(consumer, "elytra_hide_cape_recipe");
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_WING_GLOW_RECIPE.get()).save(consumer, "elytra_wing_glow_recipe");
    }
}
