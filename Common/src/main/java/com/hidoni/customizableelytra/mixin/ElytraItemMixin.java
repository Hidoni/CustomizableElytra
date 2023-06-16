package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.customization.CustomizationUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ElytraItem.class)
public class ElytraItemMixin extends Item {

    public ElytraItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, TooltipFlag flag) {
        RegistryAccess access = level != null ? level.registryAccess() : null;
        components.addAll(CustomizationUtils.getElytraTooltipLines(stack, flag, access));
    }
}
