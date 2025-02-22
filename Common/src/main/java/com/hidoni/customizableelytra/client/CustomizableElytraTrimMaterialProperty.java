package com.hidoni.customizableelytra.client;

import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.CustomizableElytraItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CustomizableElytraTrimMaterialProperty(boolean rightWing) implements SelectItemModelProperty<ResourceKey<TrimMaterial>> {
    public static final SelectItemModelProperty.Type<CustomizableElytraTrimMaterialProperty, ResourceKey<TrimMaterial>> TYPE = Type.create(RecordCodecBuilder.mapCodec(((instance) -> instance.group(Codec.BOOL.fieldOf("right_wing").forGetter(CustomizableElytraTrimMaterialProperty::rightWing)).apply(instance, CustomizableElytraTrimMaterialProperty::new))), ResourceKey.codec(Registries.TRIM_MATERIAL));

    @Override
    public @Nullable ResourceKey<TrimMaterial> get(@NotNull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, @NotNull ItemDisplayContext itemDisplayContext) {
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(itemStack);
        ItemStack wingItemStack = rightWing ? customization.rightWing() : customization.leftWing();
        Optional<ArmorTrim> armorTrim = ((CustomizableElytraItem) wingItemStack.getItem()).getArmorTrim(wingItemStack);
        return armorTrim.flatMap(trim -> trim.material().unwrapKey()).orElse(null);
    }

    @Override
    public @NotNull Type<? extends SelectItemModelProperty<ResourceKey<TrimMaterial>>, ResourceKey<TrimMaterial>> type() {
        return TYPE;
    }
}
