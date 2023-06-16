package com.hidoni.customizableelytra.customization;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.language.Translationkeys;
import com.hidoni.customizableelytra.registry.ModItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class CustomizationUtils {
    public static @Nullable CompoundTag getElytraCustomizationTag(ItemStack elytra) {
        return elytra.getTagElement(Constants.ELYTRA_CUSTOMIZATION_KEY);
    }

    public static ElytraCustomization getElytraCustomization(ItemStack elytra) {
        CompoundTag tag = getElytraCustomizationTag(elytra);
        if (tag == null) {
            ItemStack leftWing = new ItemStack(ModItems.ELYTRA_WING.get()), rightWing = leftWing.copy();
            return new ElytraCustomization(leftWing, rightWing);
        }
        return getElytraCustomization(tag);
    }

    public static ElytraCustomization getElytraCustomization(CompoundTag tag) {
        ItemStack leftWing = new ItemStack(ModItems.ELYTRA_WING.get());
        ItemStack rightWing = new ItemStack(ModItems.ELYTRA_WING.get());
        if (tag.contains(Constants.ELYTRA_LEFT_WING_CUSTOMIZATION_KEY, Tag.TAG_COMPOUND)) {
            leftWing = getWing(tag.getCompound(Constants.ELYTRA_LEFT_WING_CUSTOMIZATION_KEY));
        }
        if (tag.contains(Constants.ELYTRA_RIGHT_WING_CUSTOMIZATION_KEY, Tag.TAG_COMPOUND)) {
            rightWing = getWing(tag.getCompound(Constants.ELYTRA_RIGHT_WING_CUSTOMIZATION_KEY));
        }
        return new ElytraCustomization(leftWing, rightWing);
    }

    private static ItemStack getWing(CompoundTag tag) {
        return ItemStack.of(tag);
    }

    public static int convertDyeColorToInt(DyeColor dyeColor) {
        float[] colorValues = dyeColor.getTextureDiffuseColors();
        int red = (int) (colorValues[0] * 255) << 16;
        int green = (int) (colorValues[1] * 255) << 8;
        int blue = (int) (colorValues[2] * 255);
        return red | green | blue;
    }

    public static float[] convertIntToRGB(int color) {
        float redValue = (float) (color >> 16 & 255) / 255.0F;
        float greenValue = (float) (color >> 8 & 255) / 255.0F;
        float blueValue = (float) (color & 255) / 255.0F;
        return new float[]{redValue, greenValue, blueValue};
    }

    public static ItemStack copyCustomizationTagToNewStack(ItemStack stack) {
        CompoundTag tag = getElytraCustomizationTag(stack);
        ItemStack outStack = new ItemStack(stack.getItem());
        if (tag != null) {
            outStack.getOrCreateTag().put(Constants.ELYTRA_CUSTOMIZATION_KEY, tag);
        }
        return outStack;
    }

    public static List<Component> getElytraWingTooltipLines(ItemStack wing, TooltipFlag flag, @Nullable RegistryAccess access) {
        List<Component> lines = new ArrayList<>();
        if (!(wing.getItem() instanceof CustomizableElytraItem item)) {
            return lines;
        }
        if (item.isCapeHidden(wing)) {
            lines.add(getCapeHiddenComponent());
        }
        if (item.isGlowing(wing)) {
            lines.add(getGlowingComponent());
        }
        if (item.hasCustomColor(wing)) {
            lines.add(getColorComponent(wing, flag));
        }
        if (item.hasBanner(wing)) {
            lines.addAll(getBannerComponents(wing));
        }
        if (item.hasArmorTrim(wing) && access != null) {
            lines.addAll(getArmorTrimComponents(wing, access));
        }
        return lines;
    }

    @NotNull
    private static Component getCapeHiddenComponent() {
        return Component.translatable(Translationkeys.HIDDEN_CAPE_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    }

    @NotNull
    private static Component getGlowingComponent() {
        return Component.translatable(Translationkeys.GLOWING_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    }

    @NotNull
    private static Component getColorComponent(ItemStack wing, TooltipFlag flag) {
        if (flag.isAdvanced()) {
            return Component.translatable("item.color", String.format("#%06X", ((CustomizableElytraItem) wing.getItem()).getColor(wing))).withStyle(ChatFormatting.GRAY);
        }
        return Component.translatable("item.dyed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    }

    @NotNull
    private static List<Component> getBannerComponents(ItemStack wing) {
        List<Pair<Holder<BannerPattern>, DyeColor>> bannerPatterns = ((CustomizableElytraItem) wing.getItem()).getBannerPatterns(wing);
        List<Component> lines = new ArrayList<>(bannerPatterns.size());
        for (Pair<Holder<BannerPattern>, DyeColor> pattern : bannerPatterns) {
            Holder<BannerPattern> patternHolder = pattern.getFirst();
            DyeColor dyeColor = pattern.getSecond();
            patternHolder.unwrapKey()
                    .map(bannerPatternResourceKey -> bannerPatternResourceKey.location().toShortLanguageKey())
                    .ifPresent(location -> {
                        ResourceLocation bannerPatternLocation = new ResourceLocation(location);
                        lines.add(Component.translatable("block." + bannerPatternLocation.getNamespace() + ".banner." + bannerPatternLocation.getPath() + "." + dyeColor.getName()).withStyle(ChatFormatting.GRAY)
                        );
                    });
        }
        return lines;
    }

    @NotNull
    private static List<Component> getArmorTrimComponents(ItemStack wing, @Nullable RegistryAccess access) {
        if (access == null) {
            return List.of();
        }
        List<Component> lines = new ArrayList<>(3);
        ArmorTrim.appendUpgradeHoverText(wing, access, lines);
        return lines;
    }

    public static void addComponentsToLists(ElytraCustomization customization, List<Component> common, List<Component> left, List<Component> right, Predicate<ItemStack> hasComponent, Function<ItemStack, List<Component>> componentProvider) {
        ItemStack leftWing = customization.leftWing();
        ItemStack rightWing = customization.rightWing();
        if (hasComponent.test(leftWing)) {
            List<Component> leftWingComponents = componentProvider.apply(leftWing);
            if (hasComponent.test(rightWing)) {
                List<Component> rightWingComponents = componentProvider.apply(rightWing);
                if (leftWingComponents.equals(rightWingComponents)) {
                    common.addAll(leftWingComponents);
                } else {
                    left.addAll(leftWingComponents);
                    right.addAll(rightWingComponents);
                }
            } else {
                left.addAll(leftWingComponents);
            }
        } else if (hasComponent.test(rightWing)) {
            right.addAll(componentProvider.apply(rightWing));
        }
    }

    public static List<Component> getElytraTooltipLines(ItemStack elytra, TooltipFlag flag, @Nullable RegistryAccess access) {
        ElytraCustomization customization = getElytraCustomization(elytra);
        List<Component> leftWingLines = new ArrayList<>();
        List<Component> rightWingLines = new ArrayList<>();
        List<Component> outputLines = new ArrayList<>();

        addComponentsToLists(customization, outputLines, leftWingLines, rightWingLines, (stack) -> ((CustomizableElytraItem)stack.getItem()).isGlowing(stack), (stack) -> List.of(getGlowingComponent()));
        addComponentsToLists(customization, outputLines, leftWingLines, rightWingLines, (stack) -> ((CustomizableElytraItem)stack.getItem()).isCapeHidden(stack), (stack) -> List.of(getCapeHiddenComponent()));
        addComponentsToLists(customization, outputLines, leftWingLines, rightWingLines, (stack) -> ((CustomizableElytraItem)stack.getItem()).hasCustomColor(stack), (stack) -> List.of(getColorComponent(stack, flag)));
        addComponentsToLists(customization, outputLines, leftWingLines, rightWingLines, (stack) -> ((CustomizableElytraItem)stack.getItem()).hasBanner(stack), CustomizationUtils::getBannerComponents);
        addComponentsToLists(customization, outputLines, leftWingLines, rightWingLines, (stack) -> ((CustomizableElytraItem)stack.getItem()).hasArmorTrim(stack), (stack) -> getArmorTrimComponents(stack, access));

        /*if (leftWingItem.isGlowing(leftWingStack)) {
            if (rightWingItem.isGlowing(rightWingStack)) {
                outputLines.add(getGlowingComponent());
            } else {
                leftWingLines.add(getGlowingComponent());
            }
        } else if (rightWingItem.isGlowing(rightWingStack)) {
            rightWingLines.add(getGlowingComponent());
        }

        if (leftWingItem.isCapeHidden(leftWingStack)) {
            if (rightWingItem.isCapeHidden(rightWingStack)) {
                outputLines.add(getCapeHiddenComponent());
            } else {
                leftWingLines.add(getCapeHiddenComponent());
            }
        } else if (rightWingItem.isCapeHidden(rightWingStack)) {
            rightWingLines.add(getCapeHiddenComponent());
        }

        if (leftWingItem.hasCustomColor(leftWingStack)) {
            Component leftWingComponent = getColorComponent(leftWingStack, flag);
            if (rightWingItem.hasCustomColor(rightWingStack)) {
                Component rightWingComponents = getColorComponent(rightWingStack, flag);
                if (leftWingComponent.equals(rightWingComponents)) {
                    outputLines.add(leftWingComponent);
                } else {
                    leftWingLines.add(leftWingComponent);
                    rightWingLines.add(rightWingComponents);
                }
            } else {
                leftWingLines.add(leftWingComponent);
            }
        } else if (rightWingItem.hasCustomColor(rightWingStack)) {
            Component rightWingComponent = getColorComponent(rightWingStack, flag);
            rightWingLines.add(rightWingComponent);
        }

        if (leftWingItem.hasBanner(leftWingStack)) {
            List<Component> leftWingComponents = getBannerComponents(leftWingStack);
            if (rightWingItem.hasBanner(rightWingStack)) {
                List<Component> rightWingComponents = getBannerComponents(rightWingStack);
                if (leftWingComponents.equals(rightWingComponents)) {
                    outputLines.addAll(leftWingComponents);
                } else {
                    leftWingLines.addAll(leftWingComponents);
                    rightWingLines.addAll(rightWingComponents);
                }
            } else {
                leftWingLines.addAll(leftWingComponents);
            }
        } else if (rightWingItem.hasBanner(rightWingStack)) {
            List<Component> rightWingComponents = getBannerComponents(rightWingStack);
            rightWingLines.addAll(rightWingComponents);
        }

        if (leftWingItem.hasArmorTrim(leftWingStack)) {
            List<Component> leftWingComponents = getArmorTrimComponents(leftWingStack, access);
            if (rightWingItem.hasArmorTrim(rightWingStack)) {
                List<Component> rightWingComponents = getArmorTrimComponents(rightWingStack, access);
                if (leftWingComponents.equals(rightWingComponents)) {
                    outputLines.addAll(leftWingComponents);
                } else {
                    leftWingLines.addAll(leftWingComponents);
                    rightWingLines.addAll(rightWingComponents);
                }
            } else {
                leftWingLines.addAll(leftWingComponents);
            }
        } else if (rightWingItem.hasArmorTrim(rightWingStack)) {
            List<Component> rightWingComponents = getArmorTrimComponents(rightWingStack, access);
            rightWingLines.addAll(rightWingComponents);
        }*/

        if (!leftWingLines.isEmpty()) {
            outputLines.add(Component.translatable(Translationkeys.LEFT_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY));
            outputLines.addAll(leftWingLines);
        }
        if (!rightWingLines.isEmpty()) {
            outputLines.add(Component.translatable(Translationkeys.RIGHT_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY));
            outputLines.addAll(rightWingLines);
        }
        return outputLines;
    }
}
