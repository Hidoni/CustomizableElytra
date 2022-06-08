package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.crafting.DamagedIngredient;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IngredientSerializerHandler {
    @SubscribeEvent
    public static void onSerializerRegistration(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registry.RECIPE_SERIALIZER_REGISTRY)) {
            CraftingHelper.register(new ResourceLocation(CustomizableElytra.MOD_ID, "damaged"), DamagedIngredient.Serializer.INSTANCE);
        }
    }
}