package com.hidoni.customizableelytra.recipe;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.registry.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SplitToWingsRecipe extends CustomRecipe {
    public SplitToWingsRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    private ItemStack getElytraItem(@NotNull CraftingContainer inv) {
        ItemStack elytraItem = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item.isEmpty()) {
                continue;
            }
            if (!ElytraUtils.isElytra(item)) {
                return ItemStack.EMPTY;
            }
            if (!elytraItem.isEmpty()) {
                return ItemStack.EMPTY;
            }
            elytraItem = item;
        }
        return elytraItem;
    }

    private static void copyElytraAttributesToWing(ItemStack wing, ItemStack elytra) {
        wing.setDamageValue(elytra.getDamageValue());
        wing.setRepairCost(elytra.getBaseRepairCost());
        wing.resetHoverName();
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level level) {
        return !getElytraItem(inv).isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess registryAccess) {
        ItemStack elytraItem = getElytraItem(inv);
        if (elytraItem.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(elytraItem);
        ItemStack leftWing = customization.leftWing();
        copyElytraAttributesToWing(leftWing, elytraItem);
        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(elytraItem), leftWing);
        if (elytraItem.hasCustomHoverName()) {
            leftWing.setHoverName(elytraItem.getHoverName());
        }
        return leftWing;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingContainer inv) {
        NonNullList<ItemStack> remainingItems = super.getRemainingItems(inv);
        for (int i = 0; i < remainingItems.size(); i++) {
            ItemStack elytraItem = inv.getItem(i);
            if (!ElytraUtils.isElytra(elytraItem) || remainingItems.get(i) != ItemStack.EMPTY) {
                continue;
            }
            ElytraCustomization customization = CustomizationUtils.getElytraCustomization(elytraItem);
            ItemStack rightWing = customization.rightWing();
            copyElytraAttributesToWing(rightWing, elytraItem);
            remainingItems.set(i, rightWing);
        }
        return remainingItems;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.SPLIT_TO_WINGS_RECIPE.get();
    }
}
