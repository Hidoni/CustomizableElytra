package com.hidoni.customizableelytra.items;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.config.Config;
import com.hidoni.customizableelytra.util.ElytraCustomizationData;
import com.hidoni.customizableelytra.util.ElytraCustomizationUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CustomizableElytraItem extends ElytraItem implements DyeableLeatherItem {

    public final static String LEFT_WING_TRANSLATION_KEY = "item.customizableelytra.left_wing";
    public final static String RIGHT_WING_TRANSLATION_KEY = "item.customizableelytra.right_wing";
    public final static String HIDDEN_CAPE_TRANSLATION_KEY = "item.customizableelytra.cape_hidden";
    public final static String GLOWING_WING_TRANSLATION_KEY = "item.customizableelytra.glowing_wing";

    public CustomizableElytraItem(Properties builder) {
        super(builder);
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getTextureLocation(ResourceKey<BannerPattern> bannerIn) {
        if (Config.useLowQualityElytraBanners.get()) {
            return new ResourceLocation(CustomizableElytra.MOD_ID, "entity/elytra_banner_low/" + bannerIn.location().getPath());
        }
        return new ResourceLocation(CustomizableElytra.MOD_ID, "entity/elytra_banner/" + bannerIn.location().getPath());
    }

    @Override
    public int getColor(ItemStack stack) {
        return getColor(stack, 0);
    }

    public int getColor(ItemStack stack, int index) {
        return ElytraCustomizationUtil.getData(stack).handler.getColor(index);
    }

    @Override
    public boolean hasCustomColor(ItemStack stack) {
        CompoundTag bannerTag = stack.getTagElement("BlockEntityTag");
        CompoundTag wingTag = stack.getTagElement("WingInfo");
        return DyeableLeatherItem.super.hasCustomColor(stack) || bannerTag != null || wingTag != null || stack.getOrCreateTag().getBoolean("HideCapePattern") || stack.getOrCreateTag().getInt("WingLightLevel") > 0;
    }

    @Override
    public void clearColor(ItemStack stack) {
        DyeableLeatherItem.super.clearColor(stack);
        stack.getOrCreateTag().remove("HideCapePattern");
        stack.getOrCreateTag().remove("WingLightLevel");
        stack.removeTagKey("BlockEntityTag");
        stack.removeTagKey("WingInfo");
    }

    @Nullable
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.CHEST;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        applyTooltip(tooltip, flagIn, stack.getTag(), true);
        CompoundTag wingInfo = stack.getTagElement("WingInfo");
        if (wingInfo != null) {
            if (wingInfo.contains("left")) {
                CompoundTag leftWing = wingInfo.getCompound("left");
                ElytraCustomizationData leftWingData = ElytraCustomizationUtil.getData(leftWing);
                if (leftWingData.handler.isModified()) {
                    tooltip.add(Component.translatable(LEFT_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY));
                    applyTooltip(tooltip, flagIn, leftWing);
                }
            }
            if (wingInfo.contains("right")) {
                CompoundTag rightWing = wingInfo.getCompound("right");
                ElytraCustomizationData rightWingData = ElytraCustomizationUtil.getData(rightWing);
                if (rightWingData.handler.isModified()) {
                    tooltip.add(Component.translatable(RIGHT_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY));
                    applyTooltip(tooltip, flagIn, rightWing);
                }
            }
        }
    }

    @Override
    public String getDescriptionId() {
        return Items.ELYTRA.getDescriptionId();
    }

    public static void applyTooltip(List<Component> tooltip, TooltipFlag flagIn, CompoundTag wingIn) {
        applyTooltip(tooltip, flagIn, wingIn, false);
    }

    public static void applyTooltip(List<Component> tooltip, TooltipFlag flagIn, CompoundTag wingIn, boolean ignoreDisplayTag) {
        CompoundTag wing = ElytraCustomizationUtil.migrateOldSplitWingFormat(wingIn);
        if (wing.getBoolean("HideCapePattern")) {
            tooltip.add(Component.translatable(HIDDEN_CAPE_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        if (wing.getInt("WingLightLevel") > 0) {
            tooltip.add(Component.translatable(GLOWING_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        if (!ignoreDisplayTag && wing.contains("display")) {
            CompoundTag displayTag = wing.getCompound("display");
            if (displayTag.contains("color", 99)) {
                if (flagIn.isAdvanced()) {
                    tooltip.add((Component.translatable("item.color", String.format("#%06X", displayTag.getInt("color")))).withStyle(ChatFormatting.GRAY));
                } else {
                    tooltip.add((Component.translatable("item.dyed")).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                }
            }
        } else if (wing.contains("BlockEntityTag")) {
            CompoundTag blockEntityTag = wingIn.getCompound("BlockEntityTag");
            int baseColor = blockEntityTag.getInt("Base");
            tooltip.add((Component.translatable("block.minecraft.banner." + BannerPatterns.BASE.location().getPath() + '.' + DyeColor.byId(baseColor).getName())).withStyle(ChatFormatting.GRAY));
            ListTag listnbt = blockEntityTag.getList("Patterns", 10);

            for (int i = 0; i < listnbt.size() && i < 6; ++i) {
                CompoundTag patternNBT = listnbt.getCompound(i);
                DyeColor dyecolor = DyeColor.byId(patternNBT.getInt("Color"));
                Holder<BannerPattern> bannerPatternHolder = BannerPattern.byHash(patternNBT.getString("Pattern"));
                if (bannerPatternHolder != null) {
                    bannerPatternHolder.unwrapKey().map(bannerPatternResourceKey -> bannerPatternResourceKey.location().toShortLanguageKey())
                    .ifPresent(bannerPatternLoc -> {
                        ResourceLocation fileLoc = new ResourceLocation(bannerPatternLoc);
                        tooltip.add((Component.translatable("block." + fileLoc.getNamespace() + ".banner." + fileLoc.getPath() + '.' + dyecolor.getName())).withStyle(ChatFormatting.GRAY));
                    });
                }
            }
        }
    }
}
