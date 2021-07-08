package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.crafting.DamagedIngredient;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IngredientSerializerHandler
{
    @SubscribeEvent
    public static void onSerializerRegistration(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        CraftingHelper.register(new ResourceLocation(CustomizableElytra.MOD_ID, "damaged"), DamagedIngredient.Serializer.INSTANCE);
    }
}