package com.hidoni.customizableelytra.recipe;

import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.ElytraWingItem;
import com.hidoni.customizableelytra.registry.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CombineWingsRecipe extends CustomRecipe {
    public CombineWingsRecipe(CraftingBookCategory category) {
        super(category);
    }

    private static void copyWingAttributesToElytra(ItemStack leftWing, ItemStack rightWing, ItemStack elytra) {
        elytra.setDamageValue((leftWing.getDamageValue() + rightWing.getDamageValue()) / 2);
        elytra.setRepairCost((leftWing.getBaseRepairCost() + rightWing.getBaseRepairCost()) / 2);
        elytra.resetHoverName();
        if (leftWing.hasCustomHoverName()) {
            elytra.setHoverName(leftWing.getHoverName());
        } else if (rightWing.hasCustomHoverName()) {
            elytra.setHoverName(rightWing.getHoverName());
        }
        if (!EnchantmentHelper.getEnchantments(leftWing).isEmpty()) {
            EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(leftWing), elytra);
        } else if (!EnchantmentHelper.getEnchantments(rightWing).isEmpty()) {
            EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(rightWing), elytra);
        }
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level level) {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
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
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess access) {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
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
        ElytraCustomization customization = new ElytraCustomization(CustomizationUtils.copyCustomizationTagToNewStack(leftWing), CustomizationUtils.copyCustomizationTagToNewStack(rightWing));
        ItemStack elytra = new ItemStack(Items.ELYTRA);
        copyWingAttributesToElytra(leftWing, rightWing, elytra);
        customization.saveToElytra(elytra);
        return elytra;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.COMBINE_WINGS_RECIPE.get();
    }
}
