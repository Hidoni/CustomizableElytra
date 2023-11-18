package com.hidoni.customizableelytra.recipe;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.registry.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ElytraBannerRecipe extends CustomRecipe {
    public ElytraBannerRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level level) {
        ItemStack customizableStack = ItemStack.EMPTY;
        ItemStack bannerStack = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (ElytraUtils.isElytra(stack) || stack.getItem() instanceof CustomizableElytraItem) {
                if (!customizableStack.isEmpty()) {
                    return false;
                }
                customizableStack = stack;
            } else if (stack.getItem() instanceof BannerItem) {
                if (!bannerStack.isEmpty()) {
                    return false;
                }
                bannerStack = stack;
            }
        }
        if (customizableStack.isEmpty() || bannerStack.isEmpty()) {
            return false;
        }
        if (customizableStack.getItem() instanceof CustomizableElytraItem wingItem && isWingAlreadyCustomized(customizableStack, wingItem)) {
            return false;
        }
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(customizableStack);
        ItemStack leftWing = customization.leftWing();
        ItemStack rightWing = customization.rightWing();
        CustomizableElytraItem leftWingItem = (CustomizableElytraItem) leftWing.getItem();
        CustomizableElytraItem rightWingItem = (CustomizableElytraItem) rightWing.getItem();
        return canWingBeCustomized(leftWing, leftWingItem) && (canWingBeCustomized(rightWing, rightWingItem) || (!isWingAlreadyCustomized(leftWing, leftWingItem) || !isWingAlreadyCustomized(rightWing, rightWingItem)));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess access) {
        ItemStack customizableStack = ItemStack.EMPTY;
        ItemStack bannerStack = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (ElytraUtils.isElytra(stack) || stack.getItem() instanceof CustomizableElytraItem) {
                if (!customizableStack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                customizableStack = stack.copy();
            } else if (stack.getItem() instanceof BannerItem) {
                if (!bannerStack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                bannerStack = stack;
            }
        }
        if (customizableStack.isEmpty() || bannerStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (customizableStack.getItem() instanceof CustomizableElytraItem wingItem) {
            if (isWingAlreadyCustomized(customizableStack, wingItem)) {
                return ItemStack.EMPTY;
            }
            modifyWing(customizableStack, bannerStack, wingItem);
        } else {
            ElytraCustomization customization = CustomizationUtils.getElytraCustomization(customizableStack);
            ItemStack leftWing = customization.leftWing();
            ItemStack rightWing = customization.rightWing();
            CustomizableElytraItem leftWingItem = (CustomizableElytraItem) leftWing.getItem();
            CustomizableElytraItem rightWingItem = (CustomizableElytraItem) rightWing.getItem();
            if (!canWingBeCustomized(leftWing, leftWingItem) || !canWingBeCustomized(rightWing, rightWingItem) || (isWingAlreadyCustomized(leftWing, leftWingItem) && isWingAlreadyCustomized(rightWing, rightWingItem))) {
                return ItemStack.EMPTY;
            }
            modifyWing(leftWing, bannerStack, leftWingItem);
            modifyWing(rightWing, bannerStack, rightWingItem);
            customization.saveToElytra(customizableStack);
        }
        return customizableStack;
    }

    private static void modifyWing(ItemStack wingStack, ItemStack modifier, CustomizableElytraItem wingItem) {
        wingItem.setBanner(wingStack, modifier);
    }

    private static boolean isWingAlreadyCustomized(ItemStack wingStack, CustomizableElytraItem wingItem) {
        return wingItem.hasBanner(wingStack);
    }

    private static boolean canWingBeCustomized(ItemStack wingStack, CustomizableElytraItem wingItem) {
        return wingItem.canApplyBanner(wingStack);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_BANNER_RECIPE.get();
    }
}
