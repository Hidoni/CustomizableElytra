package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ArmorDyeRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorDyeRecipe.class)
public class ArmorDyeRecipeMixin {
    @Inject(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", at=@At(value = "RETURN"), cancellable = true)
    private void handleCustomizableElytraItemDye(CraftingContainer container, RegistryAccess registryAccess, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = cir.getReturnValue();
        if (stack.getItem() instanceof CustomizableElytraItem customizableElytraItem) {
            if (!customizableElytraItem.canDye(stack)) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        }
    }
}
