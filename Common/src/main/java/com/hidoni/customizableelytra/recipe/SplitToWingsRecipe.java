package com.hidoni.customizableelytra.recipe;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.hidoni.customizableelytra.registry.ModRecipes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SplitToWingsRecipe extends CustomRecipe {
    public static final SplitToWingsRecipe INSTANCE = new SplitToWingsRecipe();
    public static final MapCodec<SplitToWingsRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, SplitToWingsRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<SplitToWingsRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private ItemStack getElytraItem(@NotNull CraftingInput inv) {
        ItemStack elytraItem = ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack item = inv.getItem(i);
            if (item.isEmpty()) {
                continue;
            }
            if (!ElytraUtils.isElytra(item)) {
                return ItemStack.EMPTY;
            }
            if (!elytraItem.isEmpty()) {
                return ItemStack.EMPTY;
            }
            elytraItem = item;
        }
        return elytraItem;
    }

    private static void copyElytraAttributesToWing(ItemStack wing, ItemStack elytra) {
        wing.setDamageValue(elytra.getDamageValue());
        wing.set(DataComponents.REPAIR_COST, elytra.get(DataComponents.REPAIR_COST));
    }

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level level) {
        return !getElytraItem(inv).isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv) {
        ItemStack elytraItem = getElytraItem(inv);
        if (elytraItem.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(elytraItem).copy();
        ItemStack leftWing = customization.leftWing();
        copyElytraAttributesToWing(leftWing, elytraItem);
        EnchantmentHelper.setEnchantments(leftWing, EnchantmentHelper.getEnchantmentsForCrafting(elytraItem));
        if (elytraItem.has(DataComponents.CUSTOM_NAME)) {
            leftWing.set(DataComponents.CUSTOM_NAME, elytraItem.get(DataComponents.CUSTOM_NAME));
        }
        return leftWing;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput inv) {
        NonNullList<ItemStack> remainingItems = super.getRemainingItems(inv);
        for (int i = 0; i < remainingItems.size(); i++) {
            ItemStack elytraItem = inv.getItem(i);
            if (!ElytraUtils.isElytra(elytraItem) || remainingItems.get(i) != ItemStack.EMPTY) {
                continue;
            }
            ElytraCustomization customization = CustomizationUtils.getElytraCustomization(elytraItem).copy();
            ItemStack rightWing = customization.rightWing();
            copyElytraAttributesToWing(rightWing, elytraItem);
            remainingItems.set(i, rightWing);
        }
        return remainingItems;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipes.SPLIT_TO_WINGS_RECIPE.get();
    }
}
