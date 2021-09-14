package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.renderers.CustomizableElytraLayer;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;

public class ClientEventHandler {
    public static void handleClientLoading(final FMLClientSetupEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        for (PlayerRenderer renderer : minecraft.getEntityRenderDispatcher().getSkinMap().values()) {
            renderer.addLayer(new CustomizableElytraLayer<>(renderer));
        }

        ItemProperties.register(ModItems.CUSTOMIZABLE_ELYTRA.get(), new ResourceLocation("broken_elytra"), new ItemPropertyFunction() {
            @Override
            public float call(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity) {
                return CustomizableElytraItem.isFlyEnabled(stack) ? 0 : 1;
            }
        });
    }
}
