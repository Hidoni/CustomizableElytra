package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.item.equipment.trim.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SmithingTrimRecipe.class)
public class SmithingTrimRecipeMixin {
    @Shadow @Final
    Holder<TrimPattern> pattern;

    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/SmithingRecipeInput;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void replaceTrimComponentOnElytra(SmithingRecipeInput inv, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack returnStack = cir.getReturnValue();
        if (returnStack.isEmpty() || !ElytraUtils.isElytra(returnStack) || !returnStack.has(DataComponents.TRIM)) {
            return;
        }
        Holder<TrimMaterial> trimMaterial = inv.addition().get(DataComponents.PROVIDES_TRIM_MATERIAL);
        if (trimMaterial == null) {
            return;
        }
        ArmorTrim trim = returnStack.remove(DataComponents.TRIM);
        ElytraCustomization elytraCustomization = CustomizationUtils.getElytraCustomization(returnStack);
        if (elytraCustomization.leftWing().has(DataComponents.TRIM) && elytraCustomization.rightWing().has(DataComponents.TRIM)) {
            ArmorTrim newTrim = new ArmorTrim(trimMaterial, pattern);
            if (Objects.requireNonNull(elytraCustomization.leftWing().get(DataComponents.TRIM)).equals(newTrim) && Objects.requireNonNull(elytraCustomization.rightWing().get(DataComponents.TRIM)).equals(newTrim)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
        }
        elytraCustomization = elytraCustomization.copy();
        elytraCustomization.leftWing().set(DataComponents.TRIM, trim);
        elytraCustomization.rightWing().set(DataComponents.TRIM, trim);
        returnStack.set(ModDataComponents.ELYTRA_CUSTOMIZATION.get(), elytraCustomization);
    }
}
