package com.hidoni.customizableelytra.setup;

import com.hidoni.customizableelytra.crafting.ElytraBannerRecipe;
import com.hidoni.customizableelytra.crafting.ElytraDyeRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;

public class ModRecipes
{
    public static final RegistryObject<SpecialRecipeSerializer<ElytraDyeRecipe>> ELYTRA_DYE_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_dye_recipe", () -> new SpecialRecipeSerializer<>(ElytraDyeRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<ElytraBannerRecipe>> ELYTRA_BANNER_RECIPE = Registration.RECIPE_SERIALIZERS.register("elytra_banner_recipe", () -> new SpecialRecipeSerializer<>(ElytraBannerRecipe::new));

    static void register()
    {

    }
}
