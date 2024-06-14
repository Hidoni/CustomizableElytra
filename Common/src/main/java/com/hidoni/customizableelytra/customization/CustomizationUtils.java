package com.hidoni.customizableelytra.customization;

import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.language.TranslationKeys;
import com.hidoni.customizableelytra.mixin.ItemStackInvoker;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import com.hidoni.customizableelytra.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CustomizationUtils {
    public static ElytraCustomization getElytraCustomization(ItemStack elytra) {
        if (!elytra.has(ModDataComponents.ELYTRA_CUSTOMIZATION.get())) {
            ItemStack emptyWing = new ItemStack(ModItems.ELYTRA_WING.get());
            return new ElytraCustomization(emptyWing, emptyWing);
        }
        return elytra.get(ModDataComponents.ELYTRA_CUSTOMIZATION.get());
    }

    public static List<Component> getElytraWingTooltipLines(ItemStack wing, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag) {
        List<Component> lines = new ArrayList<>();
        if (!(wing.getItem() instanceof CustomizableElytraItem item)) {
            return lines;
        }
        if (item.isCapeHidden(wing)) {
            getCapeHiddenComponent(lines::add);
        }
        if (item.isGlowing(wing)) {
            getGlowingComponent(lines::add);
        }
        if (item.hasBanner(wing)) {
            getBannerComponents(wing, lines::add);
        }
        return lines;
    }

    private static void getCapeHiddenComponent(Consumer<Component> componentConsumer) {
        componentConsumer.accept(Component.translatable(TranslationKeys.HIDDEN_CAPE_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    private static void getGlowingComponent(Consumer<Component> componentConsumer) {
        componentConsumer.accept(Component.translatable(TranslationKeys.GLOWING_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    private static void getColorComponent(ItemStack wing, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, Consumer<Component> componentConsumer) {
        ((ItemStackInvoker) (Object) wing).invokeAddToTooltip(DataComponents.DYED_COLOR, tooltipContext, componentConsumer, tooltipFlag);
    }

    private static void getBannerComponents(ItemStack wing, Consumer<Component> componentConsumer) {
        CustomizableElytraItem item = (CustomizableElytraItem) wing.getItem();
        DyeColor baseColor = item.getBaseColor(wing);
        componentConsumer.accept(Component.translatable("block.minecraft.banner.base." + baseColor.getName()).withStyle(ChatFormatting.GRAY));
        BannerPatternLayers bannerPatterns = item.getBannerPatterns(wing);
        for (BannerPatternLayers.Layer layer : bannerPatterns.layers()) {
            layer.pattern().unwrapKey()
                    .map(bannerPatternResourceKey -> bannerPatternResourceKey.location().toShortLanguageKey())
                    .ifPresent(location -> {
                        ResourceLocation bannerPatternLocation = ResourceLocation.parse(location);
                        componentConsumer.accept(Component.translatable("block." + bannerPatternLocation.getNamespace() + ".banner." + bannerPatternLocation.getPath() + "." + layer.color().getName()).withStyle(ChatFormatting.GRAY)
                        );
                    });
        }
    }

    private static void getArmorTrimComponents(ItemStack wing, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, Consumer<Component> componentConsumer) {
        ((ItemStackInvoker) (Object) wing).invokeAddToTooltip(DataComponents.TRIM, tooltipContext, componentConsumer, tooltipFlag);
    }

    public static void addComponentsToLists(ElytraCustomization customization, List<Component> common, List<Component> left, List<Component> right, Predicate<ItemStack> hasComponent, BiConsumer<ItemStack, Consumer<Component>> componentProvider) {
        ItemStack leftWing = customization.leftWing();
        ItemStack rightWing = customization.rightWing();
        if (hasComponent.test(leftWing)) {
            List<Component> leftWingComponents = new ArrayList<>();
            componentProvider.accept(leftWing, leftWingComponents::add);
            if (hasComponent.test(rightWing)) {
                List<Component> rightWingComponents = new ArrayList<>();
                componentProvider.accept(rightWing, rightWingComponents::add);
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
            componentProvider.accept(rightWing, right::add);
        }
    }

    public static List<Component> getElytraTooltipLines(ItemStack elytra, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag) {
        ElytraCustomization customization = getElytraCustomization(elytra);
        List<Component> leftWingLines = new ArrayList<>();
        List<Component> rightWingLines = new ArrayList<>();
        List<Component> outputLines = new ArrayList<>();

        addComponentsToLists(customization, outputLines, leftWingLines, rightWingLines, (stack) -> ((CustomizableElytraItem) stack.getItem()).isGlowing(stack), (stack, componentConsumer) -> getGlowingComponent(componentConsumer));
        addComponentsToLists(customization, outputLines, leftWingLines, rightWingLines, (stack) -> ((CustomizableElytraItem) stack.getItem()).isCapeHidden(stack), (stack, componentConsumer) -> getCapeHiddenComponent(componentConsumer));
        addComponentsToLists(customization, outputLines, leftWingLines, rightWingLines, (stack) -> ((CustomizableElytraItem) stack.getItem()).isDyed(stack), (stack, componentConsumer) -> getColorComponent(stack, tooltipContext, tooltipFlag, componentConsumer));
        addComponentsToLists(customization, outputLines, leftWingLines, rightWingLines, (stack) -> ((CustomizableElytraItem) stack.getItem()).hasBanner(stack), CustomizationUtils::getBannerComponents);
        addComponentsToLists(customization, outputLines, leftWingLines, rightWingLines, (stack) -> ((CustomizableElytraItem) stack.getItem()).hasArmorTrim(stack), (stack, componentConsumer) -> getArmorTrimComponents(stack, tooltipContext, tooltipFlag, componentConsumer));

        if (!leftWingLines.isEmpty()) {
            outputLines.add(Component.translatable(TranslationKeys.LEFT_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY));
            outputLines.addAll(leftWingLines);
        }
        if (!rightWingLines.isEmpty()) {
            outputLines.add(Component.translatable(TranslationKeys.RIGHT_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY));
            outputLines.addAll(rightWingLines);
        }
        return outputLines;
    }
}
