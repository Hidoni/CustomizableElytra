package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

public class ModDataComponents {
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<Boolean>> GLOWING = ModRegistries.DATA_COMPONENT_TYPES.register(new ResourceLocation(Constants.MOD_ID, "glowing"), () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<Boolean>> CAPE_HIDDEN = ModRegistries.DATA_COMPONENT_TYPES.register(new ResourceLocation(Constants.MOD_ID, "cape_hidden"), () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<ElytraCustomization>> ELYTRA_CUSTOMIZATION = ModRegistries.DATA_COMPONENT_TYPES.register(new ResourceLocation(Constants.MOD_ID, "elytra_customization"), () -> DataComponentType.<ElytraCustomization>builder().persistent(ElytraCustomization.CODEC).networkSynchronized(ElytraCustomization.STREAM_CODEC).build());

    public static void register() {
    }
}
