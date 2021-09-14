package com.hidoni.customizableelytra.crafting;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ElytraToVanillaRecipe extends SpecialRecipe {
    public ElytraToVanillaRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
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
    public ItemStack assemble(CraftingInventory inv) {
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
        if (!elytraItem.getHoverName().equals(new TranslationTextComponent(Items.ELYTRA.getDescriptionId()))) {
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
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_TO_VANILLA_RECIPE.get();
    }
}
