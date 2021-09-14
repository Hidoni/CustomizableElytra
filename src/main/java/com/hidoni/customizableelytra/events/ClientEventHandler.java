package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.renderers.CustomizableElytraLayer;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;

public class ClientEventHandler {
    public static void handleClientLoading(final FMLClientSetupEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        for (PlayerRenderer renderer : minecraft.getEntityRenderDispatcher().getSkinMap().values()) {
            renderer.addLayer(new CustomizableElytraLayer<>(renderer));
        }

        ItemModelsProperties.register(ModItems.CUSTOMIZABLE_ELYTRA.get(), new ResourceLocation("broken_elytra"), new IItemPropertyGetter() {
            @Override
            public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
                return CustomizableElytraItem.isFlyEnabled(stack) ? 0 : 1;
            }
        });
    }
}
