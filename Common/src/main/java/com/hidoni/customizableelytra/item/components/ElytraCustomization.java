package com.hidoni.customizableelytra.item.components;

import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.hidoni.customizableelytra.language.TranslationKeys;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public record ElytraCustomization(ItemStack leftWing, ItemStack rightWing) implements TooltipProvider {
    public static final Codec<ElytraCustomization> CODEC = RecordCodecBuilder.create(instance -> instance.group(ItemStack.SINGLE_ITEM_CODEC.fieldOf("leftWing").forGetter(ElytraCustomization::leftWing), ItemStack.SINGLE_ITEM_CODEC.fieldOf("rightWing").forGetter(ElytraCustomization::rightWing)).apply(instance, ElytraCustomization::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ElytraCustomization> STREAM_CODEC = StreamCodec.composite(ItemStack.OPTIONAL_STREAM_CODEC, ElytraCustomization::leftWing, ItemStack.OPTIONAL_STREAM_CODEC, ElytraCustomization::rightWing, ElytraCustomization::new);

    public boolean isCustomized() {
        return ((CustomizableElytraItem) leftWing.getItem()).isCustomized(leftWing) || ((CustomizableElytraItem) rightWing.getItem()).isCustomized(rightWing);
    }

    public ElytraCustomization copy() {
        return new ElytraCustomization(leftWing.copy(), rightWing.copy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElytraCustomization that = (ElytraCustomization) o;
        return ItemStack.isSameItemSameComponents(leftWing, that.leftWing) && ItemStack.isSameItemSameComponents(rightWing, that.rightWing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ItemStack.hashItemAndComponents(leftWing), ItemStack.hashItemAndComponents(rightWing));
    }

    @Override
    public void addToTooltip(Item.@NotNull TooltipContext context, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flag, @NotNull DataComponentGetter componentGetter) {
        ElytraTooltipLines allTooltipLines = ElytraTooltipLines.merge(
                ElytraTooltipLines.fromDataComponent(this, ModDataComponents.GLOWING.get(), context, TooltipDisplay.DEFAULT, flag),
                ElytraTooltipLines.fromDataComponent(this, ModDataComponents.CAPE_HIDDEN.get(), context, TooltipDisplay.DEFAULT, flag),
                ElytraTooltipLines.fromDataComponent(this, DataComponents.DYED_COLOR, context, TooltipDisplay.DEFAULT, flag),
                ElytraTooltipLines.fromDataComponent(this, DataComponents.BANNER_PATTERNS, context, TooltipDisplay.DEFAULT, flag),
                ElytraTooltipLines.fromDataComponent(this, DataComponents.TRIM, context, TooltipDisplay.DEFAULT, flag)
        );

        for (Component component : allTooltipLines.common) {
            tooltipAdder.accept(component);
        }
        if (!allTooltipLines.leftWing.isEmpty()) {
            tooltipAdder.accept(Component.translatable(TranslationKeys.LEFT_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY));
            for (Component component : allTooltipLines.leftWing) {
                tooltipAdder.accept(component);
            }
        }
        if (!allTooltipLines.rightWing.isEmpty()) {
            tooltipAdder.accept(Component.translatable(TranslationKeys.RIGHT_WING_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY));
            for (Component component : allTooltipLines.rightWing) {
                tooltipAdder.accept(component);
            }
        }

    }

    public record ElytraTooltipLines(List<Component> common, List<Component> leftWing,
                                     List<Component> rightWing) {
        public static <T extends TooltipProvider> ElytraTooltipLines fromDataComponent(ElytraCustomization customization, DataComponentType<T> component, Item.TooltipContext context, TooltipDisplay tooltipDisplay, TooltipFlag tooltipFlag) {
            ItemStack leftWingItem = customization.leftWing();
            ItemStack rightWingItem = customization.rightWing();
            List<Component> leftWingLines = List.of(), rightWingLines = List.of();
            if (leftWingItem.has(component)) {
                leftWingLines = new ArrayList<>();
                leftWingItem.addToTooltip(component, context, tooltipDisplay, leftWingLines::add, tooltipFlag);
            }
            if (rightWingItem.has(component)) {
                rightWingLines = new ArrayList<>();
                rightWingItem.addToTooltip(component, context, tooltipDisplay, rightWingLines::add, tooltipFlag);
            }
            if (leftWingLines.equals(rightWingLines)) {
                return new ElytraTooltipLines(leftWingLines, List.of(), List.of());
            }
            return new ElytraTooltipLines(List.of(), leftWingLines, rightWingLines);
        }

        public static ElytraTooltipLines merge(ElytraTooltipLines... tooltipLinesToMerge) {
            List<Component> common = new ArrayList<>();
            List<Component> leftWing = new ArrayList<>();
            List<Component> rightWing = new ArrayList<>();
            for (ElytraTooltipLines tooltipLines : tooltipLinesToMerge) {
                common.addAll(tooltipLines.common);
                leftWing.addAll(tooltipLines.leftWing);
                rightWing.addAll(tooltipLines.rightWing);
            }
            return new ElytraTooltipLines(common, leftWing, rightWing);
        }
    }
}
