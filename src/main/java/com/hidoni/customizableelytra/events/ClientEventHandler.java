package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.renderers.CustomizableElytraLayer;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEventHandler {
    public static void handleClientLoading(final FMLClientSetupEvent event) {
        ItemProperties.register(ModItems.CUSTOMIZABLE_ELYTRA.get(), new ResourceLocation("broken_elytra"), (stack, world, entity, i) -> CustomizableElytraItem.isFlyEnabled(stack) ? 0 : 1);
    }
}
