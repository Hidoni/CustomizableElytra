package com.hidoni.customizableelytra.data;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.crafting.DamagedIngredient;
import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.data.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.caelus.api.CaelusApi;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(ModItems.ELYTRA_WING.get(), 2)
                .requires(new DamagedIngredient(new Ingredient.TagList(CaelusApi.ELYTRA), 0))
                .unlockedBy("has_item", has(CaelusApi.ELYTRA))
                .group("elytra_wing_recipes")
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(ModItems.ELYTRA_WING.get(), 2)
                .requires(new DamagedIngredient(new Ingredient.SingleItemList(new ItemStack(Items.ELYTRA)), 0))
                .unlockedBy("has_item", has(Items.ELYTRA))
                .group("elytra_wing_recipes")
                .save(consumer, new ResourceLocation(CustomizableElytra.MOD_ID, "elytra_wing_vanilla"));

        CustomRecipeBuilder.special(ModRecipes.ELYTRA_DYE_RECIPE.get()).save(consumer, "elytra_dye_recipe");
        CustomRecipeBuilder.special(ModRecipes.ELYTRA_BANNER_RECIPE.get()).save(consumer, "elytra_banner_recipe");
        CustomRecipeBuilder.special(ModRecipes.ELYTRA_WING_COMBINATION_RECIPE.get()).save(consumer, "elytra_wing_combination_recipe");
        CustomRecipeBuilder.special(ModRecipes.ELYTRA_TO_VANILLA_RECIPE.get()).save(consumer, "elytra_to_vanilla_recipe");
        CustomRecipeBuilder.special(ModRecipes.ELYTRA_HIDE_CAPE_RECIPE.get()).save(consumer, "elytra_hide_cape_recipe");
    }
}
