package com.hidoni.customizableelytra.crafting;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

public class ElytraToVanillaRecipe extends CustomRecipe {
    public ElytraToVanillaRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack elytraItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                if (!elytraItem.isEmpty() || !(inventoryItem.getItem() instanceof CustomizableElytraItem)) {
                    return false;
                }
                elytraItem = inventoryItem;
            }
        }
        return !elytraItem.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack elytraItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                if (!elytraItem.isEmpty() || !(inventoryItem.getItem() instanceof CustomizableElytraItem)) {
                    return ItemStack.EMPTY;
                }
                elytraItem = inventoryItem;
            }
        }

        ItemStack vanillaElytraItem = new ItemStack(Items.ELYTRA, 1);
        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(elytraItem), vanillaElytraItem);
        if (!elytraItem.getHoverName().equals(new TranslatableComponent(Items.ELYTRA.getDescriptionId()))) {
            vanillaElytraItem.setHoverName(elytraItem.getHoverName());
        }
        vanillaElytraItem.setDamageValue(elytraItem.getDamageValue());
        vanillaElytraItem.setRepairCost(elytraItem.getBaseRepairCost());
        return !elytraItem.isEmpty() ? vanillaElytraItem : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_TO_VANILLA_RECIPE.get();
    }
}
