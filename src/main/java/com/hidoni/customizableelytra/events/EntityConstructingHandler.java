package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.renderers.CustomizableElytraLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class EntityConstructingHandler
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onEntityConstruct(EntityEvent.EntityConstructing event)
    {
        if (event.getEntity() instanceof ArmorStandEntity) // Add custom elytra to armor stands.
        {
            ArmorStandEntity entity = (ArmorStandEntity) event.getEntity();
            ArmorStandRenderer renderer = (ArmorStandRenderer) Minecraft.getInstance().getRenderManager().getRenderer(entity);
            renderer.addLayer(new CustomizableElytraLayer<>(renderer));
        }
    }
}
