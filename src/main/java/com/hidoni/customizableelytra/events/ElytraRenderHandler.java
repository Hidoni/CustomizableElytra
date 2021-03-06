package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.caelus.api.RenderElytraEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class ElytraRenderHandler
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onElytraRender(RenderElytraEvent event)
    {
        if (event.canRender())
        {
            // Check if the equipped armor has been modified with Colytra
            ItemStack chestStack = event.getPlayer().getItemStackFromSlot(EquipmentSlotType.CHEST);
            CompoundNBT colytraChestTag = chestStack.getChildTag("colytra:ElytraUpgrade");
            if (colytraChestTag != null)
            {
                ItemStack elytraStack = ItemStack.read(colytraChestTag);
                if (elytraStack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get())
                {
                    event.setRender(false); // disable the rendering set by Colytra, custom layer will handle it from here.
                }
            }
            else if (CustomizableElytra.curiosLoaded)
            {
                Optional<ImmutableTriple<String, Integer, ItemStack>> curio = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.CUSTOMIZABLE_ELYTRA.get(), event.getPlayer());
                if (curio.isPresent())
                {
                    event.setRender(false);
                }
            }
        }
    }
}
