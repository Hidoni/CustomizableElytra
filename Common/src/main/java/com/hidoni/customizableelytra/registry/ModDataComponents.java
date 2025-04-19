package com.hidoni.customizableelytra.registry;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.hidoni.customizableelytra.item.components.GlowingWing;
import com.hidoni.customizableelytra.item.components.HiddenCape;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;

public class ModDataComponents {
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<GlowingWing>> GLOWING = ModRegistries.DATA_COMPONENT_TYPES.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "glowing"), () -> DataComponentType.<GlowingWing>builder().persistent(GlowingWing.CODEC).networkSynchronized(GlowingWing.STREAM_CODEC).build());
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<HiddenCape>> CAPE_HIDDEN = ModRegistries.DATA_COMPONENT_TYPES.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cape_hidden"), () -> DataComponentType.<HiddenCape>builder().persistent(HiddenCape.CODEC).networkSynchronized(HiddenCape.STREAM_CODEC).build());
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<ElytraCustomization>> ELYTRA_CUSTOMIZATION = ModRegistries.DATA_COMPONENT_TYPES.register(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "elytra_customization"), () -> DataComponentType.<ElytraCustomization>builder().persistent(ElytraCustomization.CODEC).networkSynchronized(ElytraCustomization.STREAM_CODEC).build());

    public static void register() {
    }
}
