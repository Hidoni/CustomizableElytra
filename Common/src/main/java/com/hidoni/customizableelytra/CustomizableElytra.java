package com.hidoni.customizableelytra;

import com.hidoni.customizableelytra.platform.Services;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import com.hidoni.customizableelytra.registry.ModItems;
import com.hidoni.customizableelytra.registry.ModRegistries;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;

import java.util.Arrays;

public class CustomizableElytra {

    public static void init() {
        ModRegistries.register();
        Services.EVENT.registerCauldronBehaviorEventHandler(() -> {
            CauldronInteraction clearElytraCustomization = (state, world, pos, player, hand, stack) -> {
                if (!stack.has(ModDataComponents.ELYTRA_CUSTOMIZATION.get())) {
                    return InteractionResult.TRY_WITH_EMPTY_HAND;
                }
                if (!world.isClientSide()) {
                    stack.remove(ModDataComponents.ELYTRA_CUSTOMIZATION.get());
                    player.awardStat(Stats.CLEAN_ARMOR);
                    LayeredCauldronBlock.lowerFillLevel(state, world, pos);
                }
                return InteractionResult.SUCCESS;
            };
            CauldronInteraction.WATER.map().put(Items.ELYTRA, clearElytraCustomization);

            CauldronInteraction clearElytraWingCustomization = (state, world, pos, player, hand, stack) -> {
                final DataComponentType<?>[] elytraWingComponentTypes = {DataComponents.DYED_COLOR, DataComponents.BASE_COLOR, DataComponents.BANNER_PATTERNS, DataComponents.TRIM, ModDataComponents.GLOWING.get(), ModDataComponents.CAPE_HIDDEN.get()};
                if (Arrays.stream(elytraWingComponentTypes).noneMatch(stack::has)) {
                    return InteractionResult.TRY_WITH_EMPTY_HAND;
                }
                if (!world.isClientSide()) {
                    Arrays.stream(elytraWingComponentTypes).forEach(stack::remove);
                    player.awardStat(Stats.CLEAN_ARMOR);
                    LayeredCauldronBlock.lowerFillLevel(state, world, pos);
                }
                return InteractionResult.SUCCESS;
            };
            CauldronInteraction.WATER.map().put(ModItems.ELYTRA_WING.get(), clearElytraWingCustomization);
        });
    }
}
