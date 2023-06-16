package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Redirect(method = "getTooltipLines", at=@At(value = "INVOKE", target = "Lnet/minecraft/world/item/armortrim/ArmorTrim;appendUpgradeHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/RegistryAccess;Ljava/util/List;)V"))
    private void overrideArmorTrimTooltipLinesForCustomizableElytra(ItemStack stack, RegistryAccess access, List<Component> components) {
        if (ElytraUtils.isElytra(stack) || stack.getItem() instanceof CustomizableElytraItem) {
            return;
        }
        ArmorTrim.appendUpgradeHoverText(stack, access, components);
    }
}
