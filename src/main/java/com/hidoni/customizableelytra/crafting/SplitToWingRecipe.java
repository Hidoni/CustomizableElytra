package com.hidoni.customizableelytra.crafting;

import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.Predicate;

public class SplitToWingRecipe extends CustomRecipe {
    public SplitToWingRecipe(ResourceLocation idIn, CraftingBookCategory categoryIn) {
        super(idIn, categoryIn);
    }
    Predicate<ItemStack> IS_ELYTRA_ITEM = stack -> stack.getItem() instanceof ElytraItem;

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        return !(getElytraItem(inv, IS_ELYTRA_ITEM).isEmpty());
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack elytraItem = getElytraItem(inv, IS_ELYTRA_ITEM);
        if (elytraItem.isEmpty())
            return ItemStack.EMPTY;
        ItemStack leftWing = new ItemStack(ModItems.ELYTRA_WING.get());
        CompoundTag nbt = elytraItem.getOrCreateTag();
        if (nbt.contains("WingInfo", Tag.TAG_COMPOUND)) {
            CompoundTag wingInfo = nbt.getCompound("WingInfo");
            if (wingInfo.contains("left", Tag.TAG_COMPOUND)) {
                leftWing.setTag(wingInfo.getCompound("left"));
            }
        }

        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(elytraItem), leftWing);
        leftWing.setDamageValue(elytraItem.getDamageValue());
        leftWing.setRepairCost(elytraItem.getBaseRepairCost());
        leftWing.resetHoverName();
        if (elytraItem.hasCustomHoverName()) {
            leftWing.setHoverName(elytraItem.getHoverName());
        }
        return leftWing;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remainders = super.getRemainingItems(inv);
        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            ItemStack elytraItem = inv.getItem(slot);
            if (IS_ELYTRA_ITEM.test(elytraItem) && remainders.get(slot).isEmpty()) {
                ItemStack rightWing = new ItemStack(ModItems.ELYTRA_WING.get());
                CompoundTag nbt = elytraItem.getOrCreateTag();
                if (nbt.contains("WingInfo", Tag.TAG_COMPOUND)) {
                    CompoundTag wingInfo = nbt.getCompound("WingInfo");
                    if (wingInfo.contains("right", Tag.TAG_COMPOUND)) {
                        rightWing.setTag(wingInfo.getCompound("right"));
                    }
                }
                EnchantmentHelper.setEnchantments(Map.of(), rightWing);
                rightWing.setDamageValue(elytraItem.getDamageValue());
                rightWing.setRepairCost(elytraItem.getBaseRepairCost());
                rightWing.resetHoverName();
                remainders.set(slot, rightWing);
            }
        }
        return remainders;
    }

    private ItemStack getElytraItem(CraftingContainer inv, Predicate<ItemStack> itemCheck) {
        ItemStack elytraItem = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                if (!elytraItem.isEmpty() || !itemCheck.test(inventoryItem)) {
                    return ItemStack.EMPTY;
                }
                elytraItem = inventoryItem;
            }
        }
        return elytraItem;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SPLIT_TO_WING_RECIPE.get();
    }
}
