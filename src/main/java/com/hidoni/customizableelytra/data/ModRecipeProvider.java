package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.crafting.DamagedIngredient;
import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.data.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(ModItems.ELYTRA_WING.get(), 2)
                .requires(new DamagedIngredient(new Ingredient.ItemValue(new ItemStack(Items.ELYTRA)), 0))
                .unlockedBy("has_item", has(Items.ELYTRA))
                .group("elytra_wing_recipes")
                .save(consumer, new ResourceLocation(CustomizableElytra.MOD_ID, "elytra_wing_vanilla"));

        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_DYE_RECIPE.get()).save(consumer, "elytra_dye_recipe");
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_BANNER_RECIPE.get()).save(consumer, "elytra_banner_recipe");
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_WING_COMBINATION_RECIPE.get()).save(consumer, "elytra_wing_combination_recipe");
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_TO_VANILLA_RECIPE.get()).save(consumer, "elytra_to_vanilla_recipe");
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_HIDE_CAPE_RECIPE.get()).save(consumer, "elytra_hide_cape_recipe");
        SpecialRecipeBuilder.special(ModRecipes.ELYTRA_WING_GLOW_RECIPE.get()).save(consumer, "elytra_wing_glow_recipe");
    }
}
