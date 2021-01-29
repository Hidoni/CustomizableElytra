package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.renderers.CustomizableElytraLayer;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;

public class ClientEventHandler
{
    public static void handleClientLoading(final FMLClientSetupEvent event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        PlayerRenderer playerRenderer = minecraft.getRenderManager().getSkinMap().get("slim");
        playerRenderer.addLayer(new CustomizableElytraLayer<>(playerRenderer));
        playerRenderer = minecraft.getRenderManager().getSkinMap().get("default");
        playerRenderer.addLayer(new CustomizableElytraLayer<>(playerRenderer));
        ArmorStandEntity armorStandEntity = EntityType.ARMOR_STAND.create(Minecraft.getInstance().world);
        LivingRenderer livingRenderer = (LivingRenderer) minecraft.getRenderManager().getRenderer(armorStandEntity);
        livingRenderer.addLayer(new CustomizableElytraLayer<>(livingRenderer));
        armorStandEntity.remove();

        ItemModelsProperties.registerProperty(ModItems.CUSTOMIZABLE_ELYTRA.get(), new ResourceLocation("broken_elytra"), new IItemPropertyGetter()
        {
            @Override
            public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity)
            {
                return CustomizableElytraItem.isUsable(stack) ? 0 : 1;
            }
        });
    }
}
