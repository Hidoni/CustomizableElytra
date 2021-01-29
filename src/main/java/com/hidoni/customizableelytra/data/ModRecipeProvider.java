package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider
{
    public ModRecipeProvider(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        CustomRecipeBuilder.customRecipe(ModRecipes.ELYTRA_DYE_RECIPE.get()).build(consumer, "elytra_dye_recipe");
        CustomRecipeBuilder.customRecipe(ModRecipes.ELYTRA_BANNER_RECIPE.get()).build(consumer, "elytra_banner_recipe");
    }
}
