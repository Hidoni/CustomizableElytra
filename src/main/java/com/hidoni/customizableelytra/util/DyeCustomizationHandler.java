package com.hidoni.customizableelytra.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DyeCustomizationHandler extends CustomizationHandler
{
    private final int color;

    public DyeCustomizationHandler(ItemStack itemIn)
    {
        this(itemIn.getOrCreateTag());
    }

    public DyeCustomizationHandler(CompoundNBT tagIn)
    {
        super(tagIn.getBoolean("HideCapePattern"));
        CompoundNBT childTag = tagIn.getCompound("display");
        this.color = childTag.contains("color", 99) ? childTag.getInt("color") : 16777215;
    }

    @Override
    public int getColor(int index)
    {
        return color;
    }

    @Override
    public <T extends LivingEntity, M extends AgeableModel<T>> void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, M renderModel, ResourceLocation textureLocation, boolean hasGlint)
    {
        List<Float> colors = getColors(color);
        renderModel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        IVertexBuilder ivertexbuilder = ItemRenderer.getArmorVertexBuilder(bufferIn, RenderType.getArmorCutoutNoCull(textureLocation), false, hasGlint);
        renderModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, colors.get(0), colors.get(1), colors.get(2), 1.0F);
    }

    @Nonnull
    public static List<Float> getColors(int color)
    {
        ArrayList<Float> colorOut = new ArrayList<>();
        float redValue = (float) (color >> 16 & 255) / 255.0F;
        float greenValue = (float) (color >> 8 & 255) / 255.0F;
        float blueValue = (float) (color & 255) / 255.0F;
        colorOut.add(redValue);
        colorOut.add(greenValue);
        colorOut.add(blueValue);
        return colorOut;
    }
}
