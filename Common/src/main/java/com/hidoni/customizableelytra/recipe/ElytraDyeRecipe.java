package com.hidoni.customizableelytra.recipe;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import com.hidoni.customizableelytra.registry.ModRecipes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ElytraDyeRecipe extends CustomRecipe {
    public static final ElytraDyeRecipe INSTANCE = new ElytraDyeRecipe();
    public static final MapCodec<ElytraDyeRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, ElytraDyeRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<ElytraDyeRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level level) {
        ItemStack customizableStack = ItemStack.EMPTY;
        List<DyeItem> dyes = new ArrayList<>();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (ElytraUtils.isElytra(stack)) {
                if (!customizableStack.isEmpty()) {
                    return false;
                }
                customizableStack = stack;
            } else if (stack.getItem() instanceof DyeItem dyeItem) {
                dyes.add(dyeItem);
            }
        }
        if (customizableStack.isEmpty() || dyes.isEmpty()) {
            return false;
        }
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(customizableStack);
        ItemStack leftWing = customization.leftWing();
        ItemStack rightWing = customization.rightWing();
        CustomizableElytraItem leftWingItem = (CustomizableElytraItem) leftWing.getItem();
        CustomizableElytraItem rightWingItem = (CustomizableElytraItem) rightWing.getItem();
        return canWingBeCustomized(leftWing, leftWingItem) && canWingBeCustomized(rightWing, rightWingItem);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput inv) {
        ItemStack customizableStack = ItemStack.EMPTY;
        List<DyeColor> dyes = new ArrayList<>();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (ElytraUtils.isElytra(stack)) {
                if (!customizableStack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                customizableStack = stack.copy();
            } else if (stack.is(ItemTags.DYES)) {
                dyes.add(stack.getOrDefault(DataComponents.DYE, DyeColor.WHITE));
            }
        }
        if (customizableStack.isEmpty() || dyes.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(customizableStack);
        ItemStack leftWing = customization.leftWing();
        ItemStack rightWing = customization.rightWing();
        CustomizableElytraItem leftWingItem = (CustomizableElytraItem) leftWing.getItem();
        CustomizableElytraItem rightWingItem = (CustomizableElytraItem) rightWing.getItem();
        if (!canWingBeCustomized(leftWing, leftWingItem) || !canWingBeCustomized(rightWing, rightWingItem)) {
            return ItemStack.EMPTY;
        }
        customizableStack.set(ModDataComponents.ELYTRA_CUSTOMIZATION.get(), new ElytraCustomization(modifyWing(leftWing, dyes), modifyWing(rightWing, dyes)));
        return customizableStack;
    }

    private static ItemStack modifyWing(ItemStack wingStack, List<DyeColor> modifiers) {
        return DyedItemColor.applyDyes(wingStack, modifiers);
    }

    private static boolean canWingBeCustomized(ItemStack wingStack, CustomizableElytraItem wingItem) {
        return wingItem.canDye(wingStack);
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipes.ELYTRA_DYE_RECIPE.get();
    }
}
