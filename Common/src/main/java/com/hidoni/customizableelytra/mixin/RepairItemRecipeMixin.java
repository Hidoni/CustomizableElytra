package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.item.ElytraWingItem;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {
    @Inject(method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z", at=@At("HEAD"), cancellable = true)
    private void preventElytraWingRepair(CraftingInput inv, Level level, CallbackInfoReturnable<Boolean> cir) {
        for (int i = 0; i < inv.size(); i++) {
            if (inv.getItem(i).getItem() instanceof ElytraWingItem) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}
