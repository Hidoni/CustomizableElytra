package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.caelus.api.CaelusApi;

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
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.ELYTRA_WING.get(), 2)
                .addIngredient(CaelusApi.ELYTRA)
                .addCriterion("has_item", hasItem(CaelusApi.ELYTRA))
                .setGroup("elytra_wing_recipes")
                .build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.ELYTRA_WING.get(), 2)
                .addIngredient(Items.ELYTRA)
                .addCriterion("has_item", hasItem(Items.ELYTRA))
                .setGroup("elytra_wing_recipes")
                .build(consumer, new ResourceLocation(CustomizableElytra.MOD_ID, "elytra_wing_vanilla"));

        CustomRecipeBuilder.customRecipe(ModRecipes.ELYTRA_DYE_RECIPE.get()).build(consumer, "elytra_dye_recipe");
        CustomRecipeBuilder.customRecipe(ModRecipes.ELYTRA_BANNER_RECIPE.get()).build(consumer, "elytra_banner_recipe");
        CustomRecipeBuilder.customRecipe(ModRecipes.ELYTRA_WING_COMBINATION_RECIPE.get()).build(consumer, "elytra_wing_combination_recipe");
    }
}
