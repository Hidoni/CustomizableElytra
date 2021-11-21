package com.hidoni.customizableelytra.crafting;

import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.setup.ModRecipes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ElytraWingGlowRecipe extends SpecialRecipe {
    private static final ResourceLocation upgradeAquaticGlowingInkSac = new ResourceLocation("upgrade_aquatic", "glowing_ink_sac");

    public ElytraWingGlowRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack elytraItem = ItemStack.EMPTY;
        ItemStack glowInkSac = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack inventoryItem = inv.getStackInSlot(i);
            if (!inventoryItem.isEmpty()) {
                if (Registry.ITEM.getKey(inventoryItem.getItem()).equals(upgradeAquaticGlowingInkSac)) {
                    if (!glowInkSac.isEmpty()) {
                        return false;
                    }

                    glowInkSac = inventoryItem;
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

        return !elytraItem.isEmpty() && !glowInkSac.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack elytraItem = ItemStack.EMPTY;
        ItemStack glowInkSac = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack inventoryItem = inv.getStackInSlot(i);
            if (!inventoryItem.isEmpty()) {
                if (Registry.ITEM.getKey(inventoryItem.getItem()).equals(upgradeAquaticGlowingInkSac)) {
                    if (!glowInkSac.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    glowInkSac = inventoryItem;
                } else if (inventoryItem.getItem() == Items.ELYTRA) {
                    if (!elytraItem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    ItemStack customizableElytraItem = new ItemStack(ModItems.CUSTOMIZABLE_ELYTRA.get());
                    EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(inventoryItem), customizableElytraItem);
                    if (!inventoryItem.getDisplayName().equals(new TranslationTextComponent(Items.ELYTRA.getTranslationKey()))) {
                        customizableElytraItem.setDisplayName(inventoryItem.getDisplayName());
                    }
                    customizableElytraItem.setDamage(inventoryItem.getDamage());
                    customizableElytraItem.setRepairCost(inventoryItem.getRepairCost());
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
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.ELYTRA_WING_GLOW_RECIPE.get();
    }
}
