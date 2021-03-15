package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemColorHandler
{
    @SubscribeEvent
    public static void registerItemColor(ColorHandlerEvent.Item event)
    {
        event.getItemColors().register((stack, color) ->
                color > 0 ? -1 : ((IDyeableArmorItem) stack.getItem()).getColor(stack), ModItems.CUSTOMIZABLE_ELYTRA.get());

        event.getItemColors().register((stack, color) ->
                color > 0 ? -1 : ((IDyeableArmorItem) stack.getItem()).getColor(stack), ModItems.ELYTRA_WING.get());
    }
}
