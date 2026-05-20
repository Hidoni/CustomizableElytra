package com.hidoni.customizableelytra.recipe;

import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.hidoni.customizableelytra.item.ElytraWingItem;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import com.hidoni.customizableelytra.registry.ModRecipes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CombineWingsRecipe extends CustomRecipe {
    public static final CombineWingsRecipe INSTANCE = new CombineWingsRecipe();
    public static final MapCodec<CombineWingsRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, CombineWingsRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<CombineWingsRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private static void copyWingAttributesToElytra(ItemStack leftWing, ItemStack rightWing, ItemStack elytra) {
        elytra.setDamageValue((leftWing.getDamageValue() + rightWing.getDamageValue()) / 2);
        int repairCost = (leftWing.getOrDefault(DataComponents.REPAIR_COST, 0) + rightWing.getOrDefault(DataComponents.REPAIR_COST, 0)) / 2;
        if (repairCost != 0) {
            elytra.set(DataComponents.REPAIR_COST, repairCost);
        }
        if (leftWing.has(DataComponents.CUSTOM_NAME)) {
            elytra.set(DataComponents.CUSTOM_NAME, leftWing.get(DataComponents.CUSTOM_NAME));
        } else if (rightWing.has(DataComponents.CUSTOM_NAME)) {
            elytra.set(DataComponents.CUSTOM_NAME, rightWing.get(DataComponents.CUSTOM_NAME));
        }
        if (!EnchantmentHelper.getEnchantmentsForCrafting(leftWing).isEmpty()) {
            EnchantmentHelper.setEnchantments(elytra, EnchantmentHelper.getEnchantmentsForCrafting(leftWing));
        } else if (!EnchantmentHelper.getEnchantmentsForCrafting(rightWing).isEmpty()) {
            EnchantmentHelper.setEnchantments(elytra, EnchantmentHelper.getEnchantmentsForCrafting(rightWing));
        }
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level level) {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (!(stack.getItem() instanceof ElytraWingItem)) {
                return false;
            }
            if (!leftWing.isEmpty()) {
                if (!rightWing.isEmpty()) {
                    return false;
                }
                rightWing = stack;
            } else {
                leftWing = stack;
            }
        }
        return !leftWing.isEmpty() && !rightWing.isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv) {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (!(stack.getItem() instanceof ElytraWingItem)) {
                return ItemStack.EMPTY;
            }
            if (!leftWing.isEmpty()) {
                if (!rightWing.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                rightWing = stack;
            } else {
                leftWing = stack;
            }
        }
        ElytraCustomization customization = new ElytraCustomization(leftWing.copyWithCount(1), rightWing.copyWithCount(1));
        ItemStack elytra = new ItemStack(Items.ELYTRA);
        copyWingAttributesToElytra(leftWing, rightWing, elytra);
        elytra.set(ModDataComponents.ELYTRA_CUSTOMIZATION.get(), customization);
        return elytra;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipes.COMBINE_WINGS_RECIPE.get();
    }
}
