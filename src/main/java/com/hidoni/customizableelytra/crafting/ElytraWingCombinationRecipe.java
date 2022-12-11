package com.hidoni.customizableelytra.crafting;

import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.Map;

public class ElytraWingCombinationRecipe extends CustomRecipe {
    public ElytraWingCombinationRecipe(ResourceLocation idIn, CraftingBookCategory categoryIn) {
        super(idIn, categoryIn);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;
        boolean isEnchanted = false;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == ModItems.ELYTRA_WING.get()) {
                    if (leftWing == ItemStack.EMPTY) {
                        leftWing = inventoryItem;
                        isEnchanted = !EnchantmentHelper.getEnchantments(inventoryItem).isEmpty();
                    } else if (rightWing == ItemStack.EMPTY && (!isEnchanted || EnchantmentHelper.getEnchantments(inventoryItem).isEmpty())) {
                        rightWing = inventoryItem;
                    } else { // We've already found two items.
                        return false;
                    }
                }
            }
        }
        return !leftWing.isEmpty() && !rightWing.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;
        boolean isEnchanted = false;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == ModItems.ELYTRA_WING.get()) {
                    if (leftWing == ItemStack.EMPTY) {
                        leftWing = inventoryItem;
                        isEnchanted = !EnchantmentHelper.getEnchantments(inventoryItem).isEmpty();
                    } else if (rightWing == ItemStack.EMPTY && (!isEnchanted || EnchantmentHelper.getEnchantments(inventoryItem).isEmpty())) {
                        rightWing = inventoryItem;
                    } else { // We've already found two items.
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        ItemStack customizedElytra = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA.get());
        customizedElytra.setDamageValue((leftWing.getDamageValue() + rightWing.getDamageValue()) / 2);
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(leftWing);
        if (enchantments.isEmpty()) {
            enchantments = EnchantmentHelper.getEnchantments(rightWing);
        }
        EnchantmentHelper.setEnchantments(enchantments, customizedElytra);
        customizedElytra.setRepairCost(Math.max(leftWing.getBaseRepairCost(), rightWing.getBaseRepairCost()));
        if (leftWing.hasCustomHoverName()) {
            customizedElytra.setHoverName(leftWing.getHoverName());
        } else if (rightWing.hasCustomHoverName()) {
            customizedElytra.setHoverName(rightWing.getHoverName());
        }
        CompoundTag leftWingNBT = convertWingToNBT(leftWing);
        CompoundTag rightWingNBT = convertWingToNBT(rightWing);
        CompoundTag wingInfo = new CompoundTag();
        if (leftWingNBT != null) {
            wingInfo.put("left", leftWingNBT);
        }
        if (rightWingNBT != null) {
            wingInfo.put("right", rightWingNBT);
        }
        customizedElytra.addTagElement("WingInfo", wingInfo);
        return customizedElytra;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_WING_COMBINATION_RECIPE.get();
    }

    public CompoundTag convertWingToNBT(ItemStack wingIn) {
        return wingIn.getOrCreateTag();
    }
}
