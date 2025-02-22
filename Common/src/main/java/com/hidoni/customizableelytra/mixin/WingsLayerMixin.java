package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.client.CustomizableElytraLayerHelper;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.customization.ElytraCustomization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentModel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WingsLayer.class)
public class WingsLayerMixin {
    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/world/item/equipment/EquipmentModel$LayerType;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V"))
    private void redirectRenderCall(EquipmentLayerRenderer instance, EquipmentModel.LayerType layerType, ResourceLocation equipmentModel, Model armorModel, ItemStack itemStack, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, @Nullable ResourceLocation playerTexture) {
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(itemStack);
        if (!customization.isCustomized() || !(armorModel instanceof ElytraModel)) {
            instance.renderLayers(layerType, equipmentModel, armorModel, itemStack, poseStack, multiBufferSource, packedLight, playerTexture);
        } else {
            CustomizableElytraLayerHelper.render((ElytraModel) armorModel, itemStack, poseStack, multiBufferSource, packedLight, playerTexture);
        }
    }
}
