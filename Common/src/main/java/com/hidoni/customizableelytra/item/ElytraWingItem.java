package com.hidoni.customizableelytra.item;

import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ElytraWingItem extends Item implements CustomizableElytraItem {
    public ElytraWingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext tooltipContext, List<Component> lines, @NotNull TooltipFlag flag) {
        lines.addAll(CustomizationUtils.getElytraWingTooltipLines(stack, tooltipContext, flag));
    }

    public boolean isDyed(@NotNull ItemStack stack) {
        return stack.has(DataComponents.DYED_COLOR);
    }

    public int getColor(@NotNull ItemStack stack) {
        if (this.isDyed(stack)) {
            return DyedItemColor.getOrDefault(stack, 0xFFFFFFFF);
        }
        if (this.hasBanner(stack)) {
            DyeColor baseDyeColor = Objects.requireNonNull(stack.get(DataComponents.BASE_COLOR));
            return CustomizationUtils.convertDyeColorToInt(baseDyeColor);
        }
        return 0xFFFFFFFF;
    }

    public boolean isCustomized(@NotNull ItemStack stack) {
        return isDyed(stack) || hasBanner(stack) || isCapeHidden(stack) || isGlowing(stack) || hasArmorTrim(stack);
    }

    @Override
    public boolean canDye(@NotNull ItemStack stack) {
        return !hasBanner(stack);
    }

    @Override
    public boolean canApplyBanner(@NotNull ItemStack stack) {
        return !hasBanner(stack);
    }

    public boolean hasBanner(@NotNull ItemStack stack) {
        return stack.has(DataComponents.BASE_COLOR);
    }

    public DyeColor getBaseColor(@NotNull ItemStack stack) {
        return stack.get(DataComponents.BASE_COLOR);
    }

    public BannerPatternLayers getBannerPatterns(@NotNull ItemStack stack) {
        return stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
    }

    public void setBanner(@NotNull ItemStack stack, @NotNull ItemStack banner) {
        DyeColor base = ((BannerItem) banner.getItem()).getColor();
        stack.remove(DataComponents.DYED_COLOR);
        stack.set(DataComponents.BASE_COLOR, base);
        stack.set(DataComponents.BANNER_PATTERNS, banner.get(DataComponents.BANNER_PATTERNS));
    }

    public boolean isGlowing(@NotNull ItemStack stack) {
        return stack.has(ModDataComponents.GLOWING.get());
    }

    public void setGlowing(@NotNull ItemStack stack, boolean glowing) {
        stack.set(ModDataComponents.GLOWING.get(), glowing);
    }

    public boolean isCapeHidden(@NotNull ItemStack stack) {
        return stack.has(ModDataComponents.CAPE_HIDDEN.get());
    }

    public void setCapeHidden(@NotNull ItemStack stack, boolean capeHidden) {
        stack.set(ModDataComponents.CAPE_HIDDEN.get(), capeHidden);
    }

    public boolean hasArmorTrim(@NotNull ItemStack stack) {
        return stack.has(DataComponents.TRIM);
    }

    public Optional<ArmorTrim> getArmorTrim(@NotNull ItemStack stack) {
        return Optional.ofNullable(stack.get(DataComponents.TRIM));
    }

    public void setArmorTrim(@NotNull ItemStack stack, @NotNull ArmorTrim trim) {
        stack.set(DataComponents.TRIM, trim);
    }

}
