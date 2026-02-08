package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.client.CustomizableElytraLayerHelper;
import com.hidoni.customizableelytra.customization.CustomizationUtils;
import com.hidoni.customizableelytra.item.components.ElytraCustomization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.object.equipment.ElytraModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WingsLayer.class)
public class WingsLayerMixin<S extends HumanoidRenderState, M extends EntityModel<S>> {
    @Redirect(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V"))
    private void redirectRenderCall(EquipmentLayerRenderer instance, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAsset, Model<? super S> armorModel, Object renderState, ItemStack itemStack, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, Identifier playerTexture, int outlineColor, int key) {
        ElytraCustomization customization = CustomizationUtils.getElytraCustomization(itemStack);
        if (!customization.isCustomized() || !(armorModel instanceof ElytraModel)) {
            instance.renderLayers(layerType, equipmentAsset, armorModel, (S)renderState, itemStack, poseStack, nodeCollector, packedLight, playerTexture, outlineColor, key);
        } else {
            CustomizableElytraLayerHelper.render(layerType, equipmentAsset, armorModel, (S)renderState, itemStack, poseStack, nodeCollector, packedLight, playerTexture, outlineColor, key);
        }
    }
}
