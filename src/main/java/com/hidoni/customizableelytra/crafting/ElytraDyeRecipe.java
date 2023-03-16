package com.hidoni.customizableelytra.crafting;

import com.google.common.collect.Lists;
import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElytraDyeRecipe extends CustomRecipe {
    public ElytraDyeRecipe(ResourceLocation idIn, CraftingBookCategory categoryIn) {
        super(idIn, categoryIn);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
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
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
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
        if (!elytraItem.getHoverName().equals(Component.translatable(Items.ELYTRA.getDescriptionId()))) {
            customizableElytraItem.setHoverName(elytraItem.getHoverName());
        }
        customizableElytraItem.setDamageValue(elytraItem.getDamageValue());
        customizableElytraItem.setRepairCost(elytraItem.getBaseRepairCost());
        return !elytraItem.isEmpty() && !list.isEmpty() ? DyeableLeatherItem.dyeArmor(customizableElytraItem, list) : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_DYE_RECIPE.get();
    }
}
