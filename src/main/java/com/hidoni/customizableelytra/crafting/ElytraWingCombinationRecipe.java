package com.hidoni.customizableelytra.crafting;

import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ElytraWingCombinationRecipe extends CustomRecipe {
    public ElytraWingCombinationRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == ModItems.ELYTRA_WING.get()) {
                    if (leftWing == ItemStack.EMPTY) {
                        leftWing = inventoryItem;
                    } else if (rightWing == ItemStack.EMPTY) {
                        rightWing = inventoryItem;
                    } else // We've already found two items.
                    {
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

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack inventoryItem = inv.getItem(i);
            if (!inventoryItem.isEmpty()) {
                if (inventoryItem.getItem() == ModItems.ELYTRA_WING.get()) {
                    if (leftWing == ItemStack.EMPTY) {
                        leftWing = inventoryItem;
                    } else if (rightWing == ItemStack.EMPTY) {
                        rightWing = inventoryItem;
                    } else // We've already found two items.
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        ItemStack customizedElytra = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA.get());
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
