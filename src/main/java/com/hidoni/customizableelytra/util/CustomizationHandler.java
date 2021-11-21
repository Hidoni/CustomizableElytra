package com.hidoni.customizableelytra.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/*
    This is the default handler, if there's no data we'll just render the most basic elytra with the vanilla colors.
 */
public class CustomizationHandler {
    private final boolean wingCapeHidden;
    private int wingLightLevel;

    public CustomizationHandler(boolean wingCapeHidden, int wingLightLevel) {
        this.wingCapeHidden = wingCapeHidden;
        this.wingLightLevel = wingLightLevel;
    }

    public int getColor(int index) {
        return 16777215;
    }

    public boolean isWingCapeHidden(int index) {
        return wingCapeHidden;
    }

    public int modifyWingLight(int lightLevel, int index) {
        if (wingLightLevel > 0) {
            lightLevel |= 0xF0;
        }
        return lightLevel;
    }

    @OnlyIn(Dist.CLIENT)
    public <T extends LivingEntity, M extends AgeableModel<T>> void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, M renderModel, ResourceLocation textureLocation, boolean hasGlint) {
        renderModel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        IVertexBuilder ivertexbuilder = ItemRenderer.getArmorVertexBuilder(bufferIn, RenderType.getArmorCutoutNoCull(textureLocation), false, hasGlint);
        renderModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
