package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ArmorDyeRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ArmorDyeRecipe.class)
public class ArmorDyeRecipeMixin {
    @Redirect(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", at=@At(value = "INVOKE", target = "Lnet/minecraft/world/item/DyeableLeatherItem;dyeArmor(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack handleCustomizableElytraItemDye(ItemStack stack, List<DyeItem> dyeItems) {
        if (stack.getItem() instanceof CustomizableElytraItem customizableElytraItem) {
            if (!customizableElytraItem.canDye(stack)) {
                return ItemStack.EMPTY;
            }
        }
        return DyeableLeatherItem.dyeArmor(stack, dyeItems);
    }
}
