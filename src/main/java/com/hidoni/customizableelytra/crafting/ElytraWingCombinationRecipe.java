package com.hidoni.customizableelytra.crafting;

import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ElytraWingCombinationRecipe extends SpecialRecipe
{
    public ElytraWingCombinationRecipe(ResourceLocation idIn)
    {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn)
    {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack inventoryItem = inv.getStackInSlot(i);
            if (!inventoryItem.isEmpty())
            {
                if (inventoryItem.getItem() == ModItems.ELYTRA_WING.get())
                {
                    if (leftWing == ItemStack.EMPTY)
                    {
                        leftWing = inventoryItem;
                    }
                    else if (rightWing == ItemStack.EMPTY)
                    {
                        rightWing = inventoryItem;
                    }
                    else // We've already found two items.
                    {
                        return false;
                    }
                }
            }
        }
        return !leftWing.isEmpty() && !rightWing.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv)
    {
        ItemStack leftWing = ItemStack.EMPTY;
        ItemStack rightWing = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack inventoryItem = inv.getStackInSlot(i);
            if (!inventoryItem.isEmpty())
            {
                if (inventoryItem.getItem() == ModItems.ELYTRA_WING.get())
                {
                    if (leftWing == ItemStack.EMPTY)
                    {
                        leftWing = inventoryItem;
                    }
                    else if (rightWing == ItemStack.EMPTY)
                    {
                        rightWing = inventoryItem;
                    }
                    else // We've already found two items.
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        ItemStack customizedElytra = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA.get());
        CompoundNBT leftWingNBT = convertWingToNBT(leftWing);
        CompoundNBT rightWingNBT = convertWingToNBT(rightWing);
        CompoundNBT wingInfo = new CompoundNBT();
        if (leftWingNBT != null)
        {
            wingInfo.put("left", leftWingNBT);
        }
        if (rightWingNBT != null)
        {
            wingInfo.put("right", rightWingNBT);
        }
        customizedElytra.setTagInfo("WingInfo", wingInfo);
        return customizedElytra;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return ModRecipes.ELYTRA_WING_COMBINATION_RECIPE.get();
    }

    public CompoundNBT convertWingToNBT(ItemStack wingIn)
    {
        if (wingIn.getChildTag("display") != null)
        {
            return wingIn.getChildTag("display");
        }
        else if (wingIn.getChildTag("BlockEntityTag") != null)
        {
            return wingIn.getChildTag("BlockEntityTag");
        }
        return null;
    }
}
