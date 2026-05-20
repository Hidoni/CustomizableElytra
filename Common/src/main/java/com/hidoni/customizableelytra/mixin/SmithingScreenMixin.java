package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.ElytraUtils;
import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.hidoni.customizableelytra.item.ElytraWingItem;
import com.hidoni.customizableelytra.registry.ModDataComponents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {

    @Unique
    private static final int ELYTRA_ROTATION_ANGLE = 135;
    @Unique
    private static final float ELYTRA_STATIC_ROT = (float) (Math.PI / 12);

    @Final
    @Shadow
    private ArmorStandRenderState armorStandPreview;

    public SmithingScreenMixin(SmithingMenu menu, Inventory inv, Component component, Identifier identifier) {
        super(menu, inv, component, identifier);
    }

    @Inject(method = "updateArmorStandPreview", at = @At("HEAD"), cancellable = true)
    private void updateArmorStandPreviewForElytra(ItemStack stack, CallbackInfo ci) {
        boolean isWing = stack.getItem() instanceof ElytraWingItem;
        if (!isWing && !ElytraUtils.isElytra(stack)) {
            return;
        }
        if (isWing) {
            ci.cancel();
            ElytraCustomization customization = new ElytraCustomization(stack, stack);
            ItemStack displayStack = new ItemStack(Items.ELYTRA);
            displayStack.set(ModDataComponents.ELYTRA_CUSTOMIZATION.get(), customization);
            this.armorStandPreview.chestEquipment = displayStack;
        }
    }

    @WrapOperation(method = "extractBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;entity(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;FLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;IIII)V"))
    private void overrideRenderAngleForElytra(GuiGraphicsExtractor instance, EntityRenderState entityRenderState, float scale, Vector3f translation, Quaternionf angle, Quaternionf overrideCameraAngle, int x0, int y0, int x1, int y1, Operation<Void> original) {
        if (ElytraUtils.isElytra(armorStandPreview.chestEquipment)) {
            angle = new Quaternionf(angle).rotateY(ELYTRA_ROTATION_ANGLE);
            this.armorStandPreview.elytraRotX = ELYTRA_STATIC_ROT;
            this.armorStandPreview.elytraRotZ = -ELYTRA_STATIC_ROT;
            this.armorStandPreview.elytraRotY = 0;
        }
        original.call(instance, entityRenderState, scale, translation, angle, null, x0, y0, x1, y1);
    }
}
