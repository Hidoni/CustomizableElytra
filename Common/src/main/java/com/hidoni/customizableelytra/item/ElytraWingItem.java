package com.hidoni.customizableelytra.item;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ElytraWingItem extends Item implements DyeableLeatherItem, CustomizableElytraItem {
    public ElytraWingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> lines, @NotNull TooltipFlag flag) {
        RegistryAccess access = level != null ? level.registryAccess() : null;
        lines.addAll(CustomizationUtils.getElytraWingTooltipLines(stack, flag, access));
    }

    @Override
    public boolean hasCustomColor(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        if (tag == null) {
            return false;
        }
        return tag.contains(TAG_COLOR, Tag.TAG_INT);
    }

    @Override
    public int getColor(@NotNull ItemStack stack) {
        if (this.hasCustomColor(stack)) {
            return Objects.requireNonNull(stack.getTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY)).getInt(TAG_COLOR);
        }
        if (this.hasBanner(stack)) {
            DyeColor baseDyeColor = getBannerPatterns(stack).get(0).getSecond();
            return CustomizationUtils.convertDyeColorToInt(baseDyeColor);
        }
        return 0xFFFFFF;
    }

    @Override
    public void clearColor(@NotNull ItemStack stack) {
        stack.getOrCreateTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY).remove(TAG_COLOR);
    }

    @Override
    public void setColor(@NotNull ItemStack stack, int color) {
        stack.getOrCreateTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY).putInt(TAG_COLOR, color);
    }

    public boolean isCustomized(@NotNull ItemStack stack) {
        return hasCustomColor(stack) || hasBanner(stack) || isCapeHidden(stack) || isGlowing(stack) || hasArmorTrim(stack);
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
        CompoundTag tag = stack.getTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        if (tag == null) {
            return false;
        }
        return tag.contains(TAG_BANNER_BASE_COLOR, Tag.TAG_INT); // Patterns are optional,
    }

    public List<Pair<Holder<BannerPattern>, DyeColor>> getBannerPatterns(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        if (tag != null) {
            return BannerBlockEntity.createPatterns(DyeColor.byId(tag.getInt(TAG_BANNER_BASE_COLOR)), tag.getList(TAG_BANNER_PATTERNS, Tag.TAG_COMPOUND));
        }
        return BannerBlockEntity.createPatterns(DyeColor.WHITE, null);
    }

    public void setBanner(@NotNull ItemStack stack, @NotNull ItemStack banner) {
        int base = ((BannerItem) banner.getItem()).getColor().getId();
        ListTag patterns = null;
        CompoundTag blockEntityTag = banner.getTagElement(BlockItem.BLOCK_ENTITY_TAG);
        if (blockEntityTag != null) {
            patterns = blockEntityTag.getList(BannerBlockEntity.TAG_PATTERNS, Tag.TAG_COMPOUND);
        }
        CompoundTag tag = stack.getOrCreateTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        tag.remove(TAG_COLOR);
        tag.putInt(TAG_BANNER_BASE_COLOR, base);
        if (patterns != null) {
            tag.put(TAG_BANNER_PATTERNS, patterns);
        }
    }

    public boolean isGlowing(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        if (tag == null) {
            return false;
        }
        return tag.getBoolean(TAG_GLOWING);
    }

    public void setGlowing(@NotNull ItemStack stack, boolean glowing) {
        CompoundTag tag = stack.getOrCreateTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        tag.putBoolean(TAG_GLOWING, glowing);
    }

    public boolean isCapeHidden(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        if (tag == null) {
            return false;
        }
        return tag.getBoolean(TAG_CAPE_HIDDEN);
    }

    public void setCapeHidden(@NotNull ItemStack stack, boolean capeHidden) {
        CompoundTag tag = stack.getOrCreateTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        tag.putBoolean(TAG_CAPE_HIDDEN, capeHidden);
    }

    public boolean hasArmorTrim(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        if (tag == null) {
            return false;
        }
        return tag.contains(TAG_TRIM, Tag.TAG_COMPOUND);
    }

    public Optional<ArmorTrim> getArmorTrim(@NotNull ItemStack stack, RegistryAccess registryAccess) {
        CompoundTag tag = stack.getTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        if (tag == null || !tag.contains(TAG_TRIM, Tag.TAG_COMPOUND)) {
            return Optional.empty();
        }
        ArmorTrim armorTrim = ArmorTrim.CODEC.parse(RegistryOps.create(NbtOps.INSTANCE, registryAccess), tag.getCompound(TAG_TRIM)).resultOrPartial(Constants.LOG::error).orElse(null);
        return Optional.ofNullable(armorTrim);
    }

    public void setArmorTrim(@NotNull ItemStack stack, RegistryAccess registryAccess, @NotNull ArmorTrim trim) {
        Tag trimTag = ArmorTrim.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, registryAccess), trim).result().orElseThrow();
        CompoundTag tag = stack.getOrCreateTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
        tag.put(TAG_TRIM, trimTag);
    }

}
