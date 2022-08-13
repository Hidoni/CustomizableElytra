package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.setup.ModItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemColorHandler {
    @SubscribeEvent
    public static void registerItemColor(RegisterColorHandlersEvent.Item event) {
        event.register((stack, color) ->
                ((CustomizableElytraItem) stack.getItem()).getColor(stack, color), ModItems.CUSTOMIZABLE_ELYTRA.get());

        event.register((stack, color) ->
                color > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), ModItems.ELYTRA_WING.get());

        CauldronInteraction.WATER.put(ModItems.CUSTOMIZABLE_ELYTRA.get(), (state, world, pos, player, hand, stack) -> {
            if (!(stack.getItem() instanceof CustomizableElytraItem)) {
                return InteractionResult.PASS;
            } else {
                if (!world.isClientSide) {
                    ItemStack vanillaElytraItemStack = new ItemStack(Items.ELYTRA, 1);
                    EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(stack), vanillaElytraItemStack);
                    if (stack.hasCustomHoverName()) {
                        vanillaElytraItemStack.setHoverName(stack.getHoverName());
                    }
                    vanillaElytraItemStack.setDamageValue(stack.getDamageValue());
                    vanillaElytraItemStack.setRepairCost(stack.getBaseRepairCost());
                    player.setItemInHand(hand, vanillaElytraItemStack);
                    player.awardStat(Stats.CLEAN_ARMOR);
                    LayeredCauldronBlock.lowerFillLevel(state, world, pos);
                }

                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        });
        CauldronInteraction.WATER.put(ModItems.ELYTRA_WING.get(), CauldronInteraction.DYED_ITEM);
    }
}
