package com.hidoni.customizableelytra.crafting;

import com.google.common.collect.Lists;
import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ElytraDyeRecipe extends SpecialRecipe {
    public ElytraDyeRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack elytraItem = ItemStack.EMPTY;
        List<ItemStack> list = Lists.newArrayList();

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == Items.ELYTRA) {
                    if (!elytraItem.isEmpty()) {
                        return false;
                    }

                    elytraItem = inventoryItem;
                } else {
                    if (!(inventoryItem.getItem() instanceof DyeItem)) {
                        return false;
                    }

                    list.add(inventoryItem);
                }
            }
        }

        return !elytraItem.isEmpty() && !list.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInventory inv) {
        List<DyeItem> list = Lists.newArrayList();
        ItemStack elytraItem = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                Item item = inventoryItem.getItem();
                if (item == Items.ELYTRA) {
                    if (!elytraItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    elytraItem = inventoryItem.copy();
                } else {
                    if (!(item instanceof DyeItem)) {
                        return ItemStack.EMPTY;
                    }

                    list.add((DyeItem) item);
                }
            }
        }
        ItemStack customizableElytraItem = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA.get(), elytraItem.getCount());
        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(elytraItem), customizableElytraItem);
        if (!elytraItem.getHoverName().equals(new TranslationTextComponent(Items.ELYTRA.getDescriptionId()))) {
            customizableElytraItem.setHoverName(elytraItem.getHoverName());
        }
        customizableElytraItem.setDamageValue(elytraItem.getDamageValue());
        customizableElytraItem.setRepairCost(elytraItem.getBaseRepairCost());
        return !elytraItem.isEmpty() && !list.isEmpty() ? IDyeableArmorItem.dyeArmor(customizableElytraItem, list) : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_DYE_RECIPE.get();
    }
}
