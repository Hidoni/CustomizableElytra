package com.hidoni.customizableelytra.integration.curios;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import javax.annotation.Nonnull;
import java.util.UUID;

public class CustomizableElytraCurio implements ICurio {
    public static final AttributeModifier ELYTRA_FLIGHT_MODIFIER =
            new AttributeModifier(UUID.fromString("0d28f673-871c-4cc2-bd9c-0e18b5430de6"),
                    "Customizable Elytra Curio Modifier", 1.0D, AttributeModifier.Operation.ADDITION);

    private final ItemStack stack;

    public CustomizableElytraCurio(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }

    @Override
    public void curioTick(SlotContext slotContext) {
        LivingEntity livingEntity = slotContext.entity();
        int ticks = livingEntity.getFallFlyingTicks();

        if (ticks > 0 && livingEntity.isFallFlying()) {
            this.stack.elytraFlightTick(livingEntity, ticks);
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext) {
        LivingEntity livingEntity = slotContext.entity();
        ICuriosHelper curiosHelper = CuriosApi.getCuriosHelper();
        return !(livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ElytraItem) &&
                curiosHelper.findEquippedCurio(
                        stack -> curiosHelper.getCurio(stack).map(curio -> curio instanceof CustomizableElytraCurio)
                                .orElse(false), livingEntity).isEmpty();
    }

    @Nonnull
    @Override
    public SoundInfo getEquipSound(SlotContext slotContext) {
        return new SoundInfo(SoundEvents.ARMOR_EQUIP_ELYTRA, 1.0F, 1.0F);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext) {
        return true;
    }
}
