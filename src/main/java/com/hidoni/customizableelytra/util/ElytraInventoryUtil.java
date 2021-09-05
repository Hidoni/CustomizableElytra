package com.hidoni.customizableelytra.util;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class ElytraInventoryUtil {
    public static ItemStack getColytraSubItem(ItemStack stack) {
        CompoundNBT colytraChestTag = stack.getChildTag("colytra:ElytraUpgrade");
        if (colytraChestTag != null) {
            ItemStack elytraStack = ItemStack.read(colytraChestTag);
            if (elytraStack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get()) {
                return elytraStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getCurioElytra(LivingEntity entity) {
        Optional<ImmutableTriple<String, Integer, ItemStack>> curio = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.CUSTOMIZABLE_ELYTRA.get(), entity);
        if (curio.isPresent()) {
            return curio.get().getRight();
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack tryFindElytra(LivingEntity entity) {
        ItemStack elytra = entity.getItemStackFromSlot(EquipmentSlotType.CHEST);
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
