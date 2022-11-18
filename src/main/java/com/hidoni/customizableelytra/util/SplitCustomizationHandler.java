package com.hidoni.customizableelytra.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SplitCustomizationHandler extends CustomizationHandler {
    private final ElytraCustomizationData leftWing, rightWing;

    public SplitCustomizationHandler(ItemStack itemIn) {
        super(itemIn.getOrCreateTag().getBoolean("HideCapePattern"), itemIn.getOrCreateTag().getInt("WingLightLevel"));
        CompoundTag wingTag = itemIn.getTagElement("WingInfo");
        leftWing = ElytraCustomizationUtil.getData(wingTag.getCompound("left"));
        rightWing = ElytraCustomizationUtil.getData(wingTag.getCompound("right"));
    }

    @Override
    public int getColor(int index) {
        return index == 0 ? leftWing.handler.getColor(index) : rightWing.handler.getColor(index);
    }

    @Override
    public boolean isWingCapeHidden(int index) {
        return super.isWingCapeHidden(index) || (index == 0 ? leftWing.handler.isWingCapeHidden(index) : rightWing.handler.isWingCapeHidden(index));
    }

    @Override
    public int modifyWingLight(int lightLevel, int index) {
        int baseWingLight = super.modifyWingLight(lightLevel, index);
        if (baseWingLight != lightLevel) {
            return baseWingLight;
        }
        return index == 0 ? leftWing.handler.modifyWingLight(lightLevel, index) : rightWing.handler.modifyWingLight(lightLevel, index);
    }

    @Override
    public boolean isModified() {
        return super.isModified() || leftWing.handler.isModified() || rightWing.handler.isModified();
    }

    public <T extends LivingEntity, M extends AgeableListModel<T>> void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, List<M> renderModels, ResourceLocation leftWingTexture, ResourceLocation rightWingTexture, boolean hasGlint) {
        leftWing.handler.render(matrixStackIn, bufferIn, modifyWingLight(packedLightIn, 0), entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, renderModels.get(0), leftWingTexture, hasGlint);
        rightWing.handler.render(matrixStackIn, bufferIn, modifyWingLight(packedLightIn, 1), entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, renderModels.get(1), rightWingTexture, hasGlint);
    }
}
