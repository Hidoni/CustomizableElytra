package com.hidoni.customizableelytra;

import com.hidoni.customizableelytra.platform.Services;
import com.hidoni.customizableelytra.registry.ModItems;
import com.hidoni.customizableelytra.registry.ModRegistries;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;

public class CustomizableElytra {
    public static void init() {
        ModRegistries.register();
        Services.EVENT.registerCauldronBehaviorEventHandler(() -> {
            CauldronInteraction clearElytraCustomization = (state, world, pos, player, hand, stack) -> {
                if (!world.isClientSide) {
                    stack.removeTagKey(Constants.ELYTRA_CUSTOMIZATION_KEY);
                    player.awardStat(Stats.CLEAN_ARMOR);
                    LayeredCauldronBlock.lowerFillLevel(state, world, pos);
                }
                return InteractionResult.sidedSuccess(world.isClientSide);

            };
            CauldronInteraction.WATER.map().put(ModItems.ELYTRA_WING.get(), clearElytraCustomization);
            CauldronInteraction.WATER.map().put(Items.ELYTRA, clearElytraCustomization);
        });
    }
}
