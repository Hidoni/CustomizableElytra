package com.hidoni.customizableelytra;

import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.mixin.ItemPropertiesInvoker;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;

public class CustomizableElytraClient {
    public static void init() {
        // These have to be generic as we have two of them but an item may only have one specialized property...
        ItemPropertiesInvoker.invokeRegisterGeneric(Constants.ELYTRA_LEFT_WING_TRIM_TYPE_PREDICATE, getWingTrimPropertyFunction(false));
        ItemPropertiesInvoker.invokeRegisterGeneric(Constants.ELYTRA_RIGHT_WING_TRIM_TYPE_PREDICATE, getWingTrimPropertyFunction(true));
    }

    public static ClampedItemPropertyFunction getWingTrimPropertyFunction(boolean rightWing) {
        return (itemStack, clientLevel, livingEntity, i) -> {
            if (!ElytraUtils.isElytra(itemStack)) {
                return Float.NEGATIVE_INFINITY;
            }
            if (clientLevel == null) {
                return 0.0F;
            }
            ElytraCustomization customization = CustomizationUtils.getElytraCustomization(itemStack);
            ItemStack wingStack = rightWing ? customization.rightWing() : customization.leftWing();
            return  (Float) ((CustomizableElytraItem)wingStack.getItem()).getArmorTrim(wingStack, clientLevel.registryAccess()).map(ArmorTrim::material).map(Holder::value).map(TrimMaterial::itemModelIndex).orElse(0.0F);
        };
    }
}
