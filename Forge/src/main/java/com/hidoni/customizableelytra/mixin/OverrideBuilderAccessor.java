package com.hidoni.customizableelytra.mixin.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ItemModelBuilder.OverrideBuilder.class)
public
interface OverrideBuilderAccessor {
    @Accessor
    ModelFile getModel();

    @Accessor
    Map<ResourceLocation, Float> getPredicates();
}
