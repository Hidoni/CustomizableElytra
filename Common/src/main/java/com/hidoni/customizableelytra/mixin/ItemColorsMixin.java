package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.registry.ModItems;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemColors.class)
public class ItemColorsMixin {
    @Inject(method = "createDefault", at = @At("TAIL"))
    private static void addCustomizableElytraItemColorHandler(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir) {
        ItemColors returnValue = cir.getReturnValue();
        returnValue.register((stack, index) -> index == 0 ? ((CustomizableElytraItem) stack.getItem()).getColor(stack) : -1, ModItems.ELYTRA_WING.get());

        returnValue.register((stack, index) -> {
            ElytraCustomization customization = CustomizationUtils.getElytraCustomization(stack);
            ItemStack leftWing = customization.leftWing();
            if (index == 0) {
                return ((CustomizableElytraItem) leftWing.getItem()).getColor(leftWing);
            } else if (index == 1) {
                ItemStack rightWing = customization.rightWing();
                return ((CustomizableElytraItem) rightWing.getItem()).getColor(rightWing);
            }
            return -1;
        }, Items.ELYTRA);

        // This really shouldn't be here, but Forge only lets me reference items after registries are set and I know they are set by this point...
        CauldronInteraction clearElytraCustomization = (state, world, pos, player, hand, stack) -> {
            if (!world.isClientSide) {
                stack.removeTagKey(Constants.ELYTRA_CUSTOMIZATION_KEY);
                player.awardStat(Stats.CLEAN_ARMOR);
                LayeredCauldronBlock.lowerFillLevel(state, world, pos);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);

        };
        CauldronInteraction.WATER.put(ModItems.ELYTRA_WING.get(), clearElytraCustomization);
        CauldronInteraction.WATER.put(Items.ELYTRA, clearElytraCustomization);
    }
}
