package com.hidoni.customizableelytra.customization;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public record ElytraCustomization(ItemStack leftWing, ItemStack rightWing) {
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
}
