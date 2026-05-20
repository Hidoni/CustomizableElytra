package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.DyeRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DyeRecipe.class)
public class DyeRecipeMixin {
    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;", at=@At(value = "RETURN"), cancellable = true)
    private void handleCustomizableElytraItemDye(CraftingInput input, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = cir.getReturnValue();
        if (stack.getItem() instanceof CustomizableElytraItem customizableElytraItem) {
            if (!customizableElytraItem.canDye(stack)) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        }
    }
}
