package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.item.equipment.trim.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;

@Mixin(SmithingTrimRecipe.class)
public class SmithingTrimRecipeMixin {
    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/SmithingRecipeInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"), cancellable = true)
    private void replaceTrimComponentOnElytra(SmithingRecipeInput inv, HolderLookup.Provider lookupProvider, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack returnStack = cir.getReturnValue();
        if (returnStack.isEmpty() || !ElytraUtils.isElytra(returnStack) || !returnStack.has(DataComponents.TRIM)) {
            return;
        }
        Optional<Holder.Reference<TrimPattern>> trimPattern = TrimPatterns.getFromTemplate(lookupProvider, inv.getItem(0));
        Optional<Holder.Reference<TrimMaterial>> trimMaterial = TrimMaterials.getFromIngredient(lookupProvider, inv.getItem(2));
        if (trimPattern.isEmpty() || trimMaterial.isEmpty()) {
            return;
        }
        ArmorTrim trim = returnStack.remove(DataComponents.TRIM);
        ElytraCustomization elytraCustomization = CustomizationUtils.getElytraCustomization(returnStack);
        if (elytraCustomization.leftWing().has(DataComponents.TRIM) && elytraCustomization.rightWing().has(DataComponents.TRIM)) {
            if (Objects.requireNonNull(elytraCustomization.leftWing().get(DataComponents.TRIM)).hasPatternAndMaterial(trimPattern.get(), trimMaterial.get()) && Objects.requireNonNull(elytraCustomization.rightWing().get(DataComponents.TRIM)).hasPatternAndMaterial(trimPattern.get(), trimMaterial.get())) {
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
