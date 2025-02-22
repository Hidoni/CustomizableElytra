package com.hidoni.customizableelytra.client;

import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CustomizableElytraDyeItemTintSource(boolean rightWing) implements ItemTintSource {
    public static final MapCodec<CustomizableElytraDyeItemTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.BOOL.fieldOf("right_wing").forGetter(CustomizableElytraDyeItemTintSource::rightWing)).apply(instance, CustomizableElytraDyeItemTintSource::new));

    @Override
    public int calculate(@NotNull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(itemStack);
        ItemStack wingItemStack = rightWing ? customization.rightWing() : customization.leftWing();
        return ((CustomizableElytraItem) wingItemStack.getItem()).getColor(wingItemStack);
    }

    @Override
    public @NotNull MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
