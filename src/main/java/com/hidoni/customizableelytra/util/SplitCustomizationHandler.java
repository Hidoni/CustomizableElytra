package com.hidoni.customizableelytra.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class SplitCustomizationHandler extends CustomizationHandler {
    private final ElytraCustomizationData leftWing, rightWing;

    public SplitCustomizationHandler(ItemStack itemIn) {
        super(itemIn.getOrCreateTag().getBoolean("HideCapePattern"), itemIn.getOrCreateTag().getInt("WingLightLevel"));
        CompoundNBT wingTag = itemIn.getChildTag("WingInfo");
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

    public <T extends LivingEntity, M extends AgeableModel<T>> void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, List<M> renderModels, ResourceLocation leftWingTexture, ResourceLocation rightWingTexture, boolean hasGlint) {
        leftWing.handler.render(matrixStackIn, bufferIn, modifyWingLight(packedLightIn, 0), entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, renderModels.get(0), leftWingTexture, hasGlint);
        rightWing.handler.render(matrixStackIn, bufferIn, modifyWingLight(packedLightIn, 1), entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, renderModels.get(1), rightWingTexture, hasGlint);
    }
}
