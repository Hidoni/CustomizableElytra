package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemColorHandler {
    @SubscribeEvent
    public static void registerItemColor(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, color) ->
                ((CustomizableElytraItem) stack.getItem()).getColor(stack, color), ModItems.CUSTOMIZABLE_ELYTRA.get());

        event.getItemColors().register((stack, color) ->
                color > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), ModItems.ELYTRA_WING.get());

        CauldronInteraction.WATER.put(ModItems.CUSTOMIZABLE_ELYTRA.get(), CauldronInteraction.DYED_ITEM);
        CauldronInteraction.WATER.put(ModItems.ELYTRA_WING.get(), CauldronInteraction.DYED_ITEM);
    }
}
