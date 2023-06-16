package com.hidoni.customizableelytra.mixin;

import net.minecraftforge.client.model.generators.ItemModelBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ItemModelBuilder.class)
public interface ItemModelBuilderAccessor {
    @Accessor
    List<ItemModelBuilder.OverrideBuilder> getOverrides();
}
