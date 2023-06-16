package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.hidoni.customizableelytra.item.ElytraWingItem;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {

    @Shadow @Nullable private ArmorStand armorStandPreview;

    @Shadow @Final public static int ARMOR_STAND_Y_ROT;

    private static final int ELYTRA_Y_ROT = 25;

    public SmithingScreenMixin(SmithingMenu menu, Inventory inv, Component component, ResourceLocation location) {
        super(menu, inv, component, location);
    }

    @Inject(method="updateArmorStandPreview", at=@At("RETURN"))
    private void updateArmorStandPreviewForElytra(ItemStack stack, CallbackInfo ci) {
        if (this.armorStandPreview == null) {
            return;
        }
        boolean isWing = stack.getItem() instanceof ElytraWingItem;
        if (!isWing && !ElytraUtils.isElytra(stack)) {
            this.armorStandPreview.yBodyRot = ARMOR_STAND_Y_ROT;
            this.armorStandPreview.yHeadRot = this.armorStandPreview.getYRot();
            this.armorStandPreview.yHeadRotO = this.armorStandPreview.getYRot();
            return;
        }
        this.armorStandPreview.yBodyRot = ELYTRA_Y_ROT;
        this.armorStandPreview.yHeadRot = this.armorStandPreview.getYRot();
        this.armorStandPreview.yHeadRotO = this.armorStandPreview.getYRot();
        this.armorStandPreview.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        if (isWing) {
            ElytraCustomization customization = new ElytraCustomization(stack, stack);
            ItemStack displayStack = new ItemStack(Items.ELYTRA);
            customization.saveToElytra(displayStack);
            this.armorStandPreview.setItemSlot(EquipmentSlot.CHEST, displayStack);
        } else {
            this.armorStandPreview.setItemSlot(EquipmentSlot.CHEST, stack);
        }
    }
}
