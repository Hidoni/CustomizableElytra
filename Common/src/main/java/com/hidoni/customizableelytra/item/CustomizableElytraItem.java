package com.hidoni.customizableelytra.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface CustomizableElytraItem extends DyeableLeatherItem {
    String TAG_VERSION = "version";
    String TAG_GLOWING = "glowing";
    String TAG_CAPE_HIDDEN = "cape_hidden";
    String TAG_TRIM = "trim";
    String TAG_BANNER_BASE_COLOR = "base";
    String TAG_BANNER_PATTERNS = "patterns";


    boolean isCustomized(@NotNull ItemStack stack);

    boolean canDye(@NotNull ItemStack stack);

    boolean canApplyBanner(@NotNull ItemStack stack);


    boolean hasBanner(@NotNull ItemStack stack);

    List<Pair<Holder<BannerPattern>, DyeColor>> getBannerPatterns(@NotNull ItemStack stack);

    void setBanner(@NotNull ItemStack stack, @NotNull ItemStack banner);

    boolean isGlowing(@NotNull ItemStack stack);

    void setGlowing(@NotNull ItemStack stack,  boolean glowing);

    boolean isCapeHidden(@NotNull ItemStack stack);

    void setCapeHidden(@NotNull ItemStack stack, boolean capeHidden);

    boolean hasArmorTrim(@NotNull ItemStack stack);

    Optional<ArmorTrim> getArmorTrim(@NotNull ItemStack stack, RegistryAccess registryAccess);

    void setArmorTrim(@NotNull ItemStack stack, RegistryAccess registryAccess, @NotNull ArmorTrim trim);
}
