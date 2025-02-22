package com.hidoni.customizableelytra.item;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface CustomizableElytraItem {
    boolean isDyed(@NotNull ItemStack stack);

    int getColor(@NotNull ItemStack stack);

    boolean isCustomized(@NotNull ItemStack stack);

    boolean canDye(@NotNull ItemStack stack);

    boolean canApplyBanner(@NotNull ItemStack stack);


    boolean hasBanner(@NotNull ItemStack stack);

    DyeColor getBaseColor(@NotNull ItemStack stack);

    BannerPatternLayers getBannerPatterns(@NotNull ItemStack stack);

    void setBanner(@NotNull ItemStack stack, @NotNull ItemStack banner);

    boolean isGlowing(@NotNull ItemStack stack);

    void setGlowing(@NotNull ItemStack stack,  boolean glowing);

    boolean isCapeHidden(@NotNull ItemStack stack);

    void setCapeHidden(@NotNull ItemStack stack, boolean capeHidden);

    boolean hasArmorTrim(@NotNull ItemStack stack);

    Optional<ArmorTrim> getArmorTrim(@NotNull ItemStack stack);

    void setArmorTrim(@NotNull ItemStack stack, @NotNull ArmorTrim trim);
}
