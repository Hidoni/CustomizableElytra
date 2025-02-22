package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.customization.CustomizationUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getTooltipLines", at=@At("RETURN"))
    private void getTooltipLines(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        ItemStack itemStackThis = (ItemStack) (Object) this;
        if (!tooltipFlag.isCreative() && itemStackThis.has(DataComponents.HIDE_TOOLTIP)) {
            return;
        }
        cir.getReturnValue().addAll(CustomizationUtils.getElytraTooltipLines(itemStackThis, tooltipContext, tooltipFlag));
    }
}
