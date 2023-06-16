package com.hidoni.customizableelytra.recipe;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.registry.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ElytraMiscellaneousCustomizationRecipe extends CustomRecipe {
    private static final Ingredient CUSTOMIZATION_INGREDIENT = Ingredient.of(Items.GLOW_INK_SAC, Items.PAPER);

    public ElytraMiscellaneousCustomizationRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level level) {
        ItemStack customizableStack = ItemStack.EMPTY;
        ItemStack modifierStack = ItemStack.EMPTY;
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
            } else if (CUSTOMIZATION_INGREDIENT.test(stack)) {
                if (!modifierStack.isEmpty()) {
                    return false;
                }
                modifierStack = stack;
            }
        }
        if (customizableStack.isEmpty() || modifierStack.isEmpty()) {
            return false;
        }
        if (customizableStack.getItem() instanceof CustomizableElytraItem wingItem && isWingAlreadyCustomized(customizableStack, modifierStack, wingItem)) {
            return false;
        }
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(customizableStack);
        ItemStack leftWing = customization.leftWing();
        ItemStack rightWing = customization.rightWing();
        CustomizableElytraItem leftWingItem = (CustomizableElytraItem) leftWing.getItem();
        CustomizableElytraItem rightWingItem = (CustomizableElytraItem) rightWing.getItem();
        return !isWingAlreadyCustomized(leftWing, modifierStack, leftWingItem) || !isWingAlreadyCustomized(rightWing, modifierStack, rightWingItem);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, @NotNull RegistryAccess access) {
        ItemStack customizableStack = ItemStack.EMPTY;
        ItemStack modifierStack = ItemStack.EMPTY;
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
            } else if (CUSTOMIZATION_INGREDIENT.test(stack)) {
                if (!modifierStack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                modifierStack = stack;
            }
        }
        if (customizableStack.isEmpty() || modifierStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (customizableStack.getItem() instanceof CustomizableElytraItem wingItem) {
            if (isWingAlreadyCustomized(customizableStack, modifierStack, wingItem)) {
                return ItemStack.EMPTY;
            }
            modifyWing(customizableStack, modifierStack, wingItem);
        } else {
            ElytraCustomization customization = CustomizationUtils.getElytraCustomization(customizableStack);
            ItemStack leftWing = customization.leftWing();
            ItemStack rightWing = customization.rightWing();
            CustomizableElytraItem leftWingItem = (CustomizableElytraItem) leftWing.getItem();
            CustomizableElytraItem rightWingItem = (CustomizableElytraItem) rightWing.getItem();
            if (isWingAlreadyCustomized(leftWing, modifierStack, leftWingItem) && isWingAlreadyCustomized(rightWing, modifierStack, rightWingItem)) {
                return ItemStack.EMPTY;
            }
            modifyWing(leftWing, modifierStack, (CustomizableElytraItem) leftWing.getItem());
            modifyWing(rightWing, modifierStack, (CustomizableElytraItem) rightWing.getItem());
            customization.saveToElytra(customizableStack);
        }
        return customizableStack;
    }

    private static void modifyWing(ItemStack wingStack, ItemStack modifier, CustomizableElytraItem wingItem) {
        if (modifier.is(Items.GLOW_INK_SAC)) {
            wingItem.setGlowing(wingStack, true);
        } else {
            wingItem.setCapeHidden(wingStack, true);
        }
    }

    private static boolean isWingAlreadyCustomized(ItemStack wingStack, ItemStack modifier, CustomizableElytraItem wingItem) {
        if (modifier.is(Items.GLOW_INK_SAC)) {
            return wingItem.isGlowing(wingStack);
        } else {
            return wingItem.isCapeHidden(wingStack);
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_MISCELLANEOUS_CUSTOMIZATION_RECIPE.get();
    }
}
