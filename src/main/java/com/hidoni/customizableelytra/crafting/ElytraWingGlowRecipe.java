package com.hidoni.customizableelytra.crafting;

import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class ElytraWingGlowRecipe extends CustomRecipe {
    public ElytraWingGlowRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack elytraItem = ItemStack.EMPTY;
        ItemStack glowInkSacItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == Items.GLOW_INK_SAC) {
                    if (!glowInkSacItem.isEmpty()) {
                        return false;
                    }

                    glowInkSacItem = inventoryItem;
                } else {
                    if (inventoryItem.getItem() != Items.ELYTRA && inventoryItem.getItem() != ModItems.CUSTOMIZABLE_ELYTRA.get() && inventoryItem.getItem() != ModItems.ELYTRA_WING.get()) {
                        return false;
                    }

                    if (!elytraItem.isEmpty()) {
                        return false;
                    }

                    elytraItem = inventoryItem;
                }
            }
        }

        return !elytraItem.isEmpty() && !glowInkSacItem.isEmpty();    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack glowInkSacItem = ItemStack.EMPTY;
        ItemStack elytraItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == Items.GLOW_INK_SAC) {
                    if (!glowInkSacItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    glowInkSacItem = inventoryItem;
                } else if (inventoryItem.getItem() == Items.ELYTRA) {
                    if (!elytraItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    ItemStack customizableElytraItem = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA.get());
                    EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(inventoryItem), customizableElytraItem);
                    if (!inventoryItem.getHoverName().equals(new TranslatableComponent(Items.ELYTRA.getDescriptionId()))) {
                        customizableElytraItem.setHoverName(inventoryItem.getHoverName());
                    }
                    customizableElytraItem.setDamageValue(inventoryItem.getDamageValue());
                    customizableElytraItem.setRepairCost(inventoryItem.getBaseRepairCost());
                    elytraItem = customizableElytraItem;
                } else if (inventoryItem.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get() || inventoryItem.getItem() == ModItems.ELYTRA_WING.get()) {
                    if (!elytraItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    elytraItem = inventoryItem.copy();
                    elytraItem.setCount(1);
                }
            }
        }

        if (!elytraItem.isEmpty()) {
            elytraItem.getOrCreateTag().putInt("WingLightLevel", 1);
        }
        return elytraItem;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_WING_GLOW_RECIPE.get();
    }
}
