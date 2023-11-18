package com.hidoni.customizableelytra.recipe;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.registry.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ElytraDyeRecipe extends CustomRecipe {
    public ElytraDyeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level level) {
        ItemStack customizableStack = ItemStack.EMPTY;
        List<DyeItem> dyes = new ArrayList<>();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (ElytraUtils.isElytra(stack)) {
                if (!customizableStack.isEmpty()) {
                    return false;
                }
                customizableStack = stack;
            } else if (stack.getItem() instanceof DyeItem dyeItem) {
                dyes.add(dyeItem);
            }
        }
        if (customizableStack.isEmpty() || dyes.isEmpty()) {
            return false;
        }
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(customizableStack);
        ItemStack leftWing = customization.leftWing();
        ItemStack rightWing = customization.rightWing();
        CustomizableElytraItem leftWingItem = (CustomizableElytraItem) leftWing.getItem();
        CustomizableElytraItem rightWingItem = (CustomizableElytraItem) rightWing.getItem();
        return canWingBeCustomized(leftWing, leftWingItem) && canWingBeCustomized(rightWing, rightWingItem);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess access) {
        ItemStack customizableStack = ItemStack.EMPTY;
        List<DyeItem> dyes = new ArrayList<>();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (ElytraUtils.isElytra(stack)) {
                if (!customizableStack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                customizableStack = stack.copy();
            } else if (stack.getItem() instanceof DyeItem dyeItem) {
                dyes.add(dyeItem);
            }
        }
        if (customizableStack.isEmpty() || dyes.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(customizableStack);
        ItemStack leftWing = customization.leftWing();
        ItemStack rightWing = customization.rightWing();
        CustomizableElytraItem leftWingItem = (CustomizableElytraItem) leftWing.getItem();
        CustomizableElytraItem rightWingItem = (CustomizableElytraItem) rightWing.getItem();
        if (!canWingBeCustomized(leftWing, leftWingItem) || !canWingBeCustomized(rightWing, rightWingItem)) {
            return ItemStack.EMPTY;
        }
        modifyWing(leftWing, dyes);
        modifyWing(rightWing, dyes);
        customization.saveToElytra(customizableStack);
        return customizableStack;
    }

    private static void modifyWing(ItemStack wingStack, List<DyeItem> modifiers) {
        wingStack.setTag(DyeableLeatherItem.dyeArmor(wingStack, modifiers).getTag());
    }

    private static boolean canWingBeCustomized(ItemStack wingStack, CustomizableElytraItem wingItem) {
        return wingItem.canDye(wingStack);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_DYE_RECIPE.get();
    }
}
