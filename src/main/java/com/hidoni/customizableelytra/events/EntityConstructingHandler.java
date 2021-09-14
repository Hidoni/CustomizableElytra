package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.renderers.CustomizableElytraLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityConstructingHandler {
    private static boolean addedYet = false;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onEntityConstruct(EntityEvent.EntityConstructing event) {
        if (!addedYet && event.getEntity() instanceof ArmorStandEntity) // Add custom elytra to armor stands.
        {
            ArmorStandEntity entity = (ArmorStandEntity) event.getEntity();
            ArmorStandRenderer renderer = (ArmorStandRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
            renderer.addLayer(new CustomizableElytraLayer<>(renderer));
            addedYet = true;
        }
    }
}
