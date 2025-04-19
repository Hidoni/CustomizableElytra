package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", shift = At.Shift.AFTER))
    private void getTooltipLines(Item.TooltipContext context, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> tooltipAdder, CallbackInfo ci) {
        ItemStack itemStackThis = (ItemStack) (Object) this;
        itemStackThis.addToTooltip(ModDataComponents.GLOWING.get(), context, tooltipDisplay, tooltipAdder, tooltipFlag);
        itemStackThis.addToTooltip(ModDataComponents.CAPE_HIDDEN.get(), context, tooltipDisplay, tooltipAdder, tooltipFlag);
        itemStackThis.addToTooltip(ModDataComponents.ELYTRA_CUSTOMIZATION.get(), context, tooltipDisplay, tooltipAdder, tooltipFlag);
    }
}
