package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.client.CustomizableElytraLayerHelper;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WingsLayer.class)
public class WingsLayerMixin {
    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V"))
    private void redirectRenderCall(EquipmentLayerRenderer instance, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAsset, Model armorModel, ItemStack itemStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, ResourceLocation playerTexture) {
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(itemStack);
        if (!customization.isCustomized() || !(armorModel instanceof ElytraModel)) {
            instance.renderLayers(layerType, equipmentAsset, armorModel, itemStack, poseStack, multiBufferSource, packedLight, playerTexture);
        } else {
            CustomizableElytraLayerHelper.render((ElytraModel) armorModel, itemStack, poseStack, multiBufferSource, packedLight, playerTexture);
        }
    }
}
