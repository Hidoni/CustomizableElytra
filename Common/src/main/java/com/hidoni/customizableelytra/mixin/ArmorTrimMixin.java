package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.item.ElytraWingItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ArmorTrim.class)
public class ArmorTrimMixin {
    @Inject(method = "setTrim", at = @At(value = "HEAD"), cancellable = true)
    private static void setArmorTrimForElytra(RegistryAccess access, ItemStack stack, ArmorTrim trim, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof CustomizableElytraItem item) {
            item.setArmorTrim(stack, access, trim);
            cir.setReturnValue(true);
        }
        else if (ElytraUtils.isElytra(stack)) {
            ElytraCustomization customization = CustomizationUtils.getElytraCustomization(stack);
            ItemStack leftWing = customization.leftWing();
            ((ElytraWingItem)leftWing.getItem()).setArmorTrim(leftWing, access, trim);
            ItemStack rightWing = customization.rightWing();
            ((ElytraWingItem)rightWing.getItem()).setArmorTrim(rightWing, access, trim);
            customization.saveToElytra(stack);
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getTrim", at = @At(value = "HEAD"), cancellable = true)
    private static void getArmorTrimForElytra(RegistryAccess access, ItemStack stack, boolean bool, CallbackInfoReturnable<Optional<ArmorTrim>> cir) {
        if (stack.getItem() instanceof CustomizableElytraItem customizableElytraItem) {
            cir.setReturnValue(customizableElytraItem.getArmorTrim(stack, access));
        }
        if (ElytraUtils.isElytra(stack)) {
            // We override pretty much every location where getTrim is called with our own logic, except for the smithing recipe
            // We only care that the smithing recipe returns EMPTY if the trim is the same for both wings.
            // This basically returns an empty optional or the trim if it's the same for both wings
            ElytraCustomization customization = CustomizationUtils.getElytraCustomization(stack);
            ItemStack leftWing = customization.leftWing();
            Optional<ArmorTrim> returnTrim = ((CustomizableElytraItem)leftWing.getItem()).getArmorTrim(leftWing, access);
            if (returnTrim.isEmpty()) {
                cir.setReturnValue(returnTrim);
            } else {
                ItemStack rightWing = customization.rightWing();
                if (returnTrim.equals(((CustomizableElytraItem) rightWing.getItem()).getArmorTrim(rightWing, access))) {
                    cir.setReturnValue(returnTrim);
                } else {
                    cir.setReturnValue(Optional.empty());
                }
            }
        }
    }
}
