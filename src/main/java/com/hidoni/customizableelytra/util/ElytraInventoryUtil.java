package com.hidoni.customizableelytra.util;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

public class ElytraInventoryUtil {
    public static ItemStack getColytraSubItem(ItemStack stack) {
        CompoundTag colytraChestTag = stack.getTagElement("colytra:ElytraUpgrade");
        if (colytraChestTag != null) {
            ItemStack elytraStack = ItemStack.of(colytraChestTag);
            if (elytraStack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get()) {
                return elytraStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getCurioElytra(LivingEntity entity) {
        Optional<SlotResult> curio = CuriosApi.getCuriosHelper().findFirstCurio(entity, ModItems.CUSTOMIZABLE_ELYTRA.get());
        if (curio.isPresent()) {
            return curio.get().stack();
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack tryFindElytra(LivingEntity entity) {
        ItemStack elytra = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (elytra.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get()) {
            return elytra;
        }
        if (CustomizableElytra.caelusLoaded) {
            elytra = getColytraSubItem(elytra);
            if (elytra != ItemStack.EMPTY) {
                return elytra;
            }
            if (CustomizableElytra.curiosLoaded) {
                return getCurioElytra(entity);
            }
        }
        return ItemStack.EMPTY;
    }
}
