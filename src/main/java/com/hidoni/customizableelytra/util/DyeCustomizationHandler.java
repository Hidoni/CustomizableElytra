package com.hidoni.customizableelytra.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DyeCustomizationHandler extends CustomizationHandler {
    private final int color;

    public DyeCustomizationHandler(ItemStack itemIn) {
        this(itemIn.getOrCreateTag());
    }

    public DyeCustomizationHandler(CompoundTag tagIn) {
        super(tagIn.getBoolean("HideCapePattern"), tagIn.getInt("WingLightLevel"));
        CompoundTag childTag = tagIn.getCompound("display");
        this.color = childTag.contains("color", 99) ? childTag.getInt("color") : 16777215;
    }

    @Nonnull
    public static List<Float> getColors(int color) {
        ArrayList<Float> colorOut = new ArrayList<>();
        float redValue = (float) (color >> 16 & 255) / 255.0F;
        float greenValue = (float) (color >> 8 & 255) / 255.0F;
        float blueValue = (float) (color & 255) / 255.0F;
        colorOut.add(redValue);
        colorOut.add(greenValue);
        colorOut.add(blueValue);
        return colorOut;
    }

    @Override
    public int getColor(int index) {
        return color;
    }

    @Override
    public <T extends LivingEntity, M extends AgeableListModel<T>> void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, M renderModel, ResourceLocation textureLocation, boolean hasGlint) {
        List<Float> colors = getColors(color);
        renderModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(textureLocation), false, hasGlint);
        renderModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, colors.get(0), colors.get(1), colors.get(2), 1.0F);
    }
}
