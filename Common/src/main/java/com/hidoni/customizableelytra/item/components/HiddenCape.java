package com.hidoni.customizableelytra.item.components;

import com.hidoni.customizableelytra.language.TranslationKeys;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public record HiddenCape(boolean isHidden) implements TooltipProvider {
    public static final Codec<HiddenCape> CODEC = Codec.BOOL.xmap(HiddenCape::new, HiddenCape::isHidden);
    public static final StreamCodec<ByteBuf, HiddenCape> STREAM_CODEC = ByteBufCodecs.BOOL.map(HiddenCape::new, HiddenCape::isHidden);
    public static final MutableComponent HIDDEN_TEXT = Component.translatable(TranslationKeys.HIDDEN_CAPE_TRANSLATION_KEY).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

    @Override
    public void addToTooltip(Item.@NotNull TooltipContext context, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag flag, @NotNull DataComponentGetter componentGetter) {
        if (isHidden) {
            tooltipAdder.accept(HIDDEN_TEXT);
        }
    }
}
