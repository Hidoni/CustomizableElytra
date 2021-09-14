package com.hidoni.customizableelytra.util;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class BannerCustomizationHandler extends CustomizationHandler {
    private final List<Pair<BannerPattern, DyeColor>> patterns;

    public BannerCustomizationHandler(ItemStack itemIn) {
        this(itemIn.getOrCreateTag());
    }

    public BannerCustomizationHandler(CompoundNBT tagIn) {
        super(tagIn.getBoolean("HideCapePattern"));
        CompoundNBT blockEntityTag = tagIn.getCompound("BlockEntityTag");
        DyeColor baseColor = DyeColor.byId(blockEntityTag.getInt("Base"));
        ListNBT patternsList = blockEntityTag.getList("Patterns", 10).copy();
        this.patterns = BannerTileEntity.createPatterns(baseColor, patternsList);
    }

    @Override
    public int getColor(int index) {
        return patterns.get(0).getSecond().getColorValue();
    }

    @Override
    public <T extends LivingEntity, M extends AgeableModel<T>> void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, M renderModel, ResourceLocation textureLocation, boolean hasGlint) {
        renderModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        IVertexBuilder ivertexbuilder = ItemRenderer.getFoilBufferDirect(bufferIn, RenderType.entityNoOutline(textureLocation), false, hasGlint);
        renderModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        float[] baseColor = patterns.get(0).getSecond().getTextureDiffuseColors();
        renderModel.renderToBuffer(matrixStackIn, ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityTranslucent(textureLocation), false, false), packedLightIn, OverlayTexture.NO_OVERLAY, baseColor[0], baseColor[1], baseColor[2], 1.0F);
        for (int i = 1; i < 17 && i < patterns.size(); ++i) {
            Pair<BannerPattern, DyeColor> pair = patterns.get(i);
            float[] afloat = pair.getSecond().getTextureDiffuseColors();
            RenderMaterial rendermaterial = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, CustomizableElytraItem.getTextureLocation(pair.getFirst()));
            if (rendermaterial.sprite().getName() != MissingTextureSprite.getLocation()) // Don't render this banner pattern if it's missing, silently hide the pattern
            {
                renderModel.renderToBuffer(matrixStackIn, rendermaterial.buffer(bufferIn, RenderType::entityTranslucent), packedLightIn, OverlayTexture.NO_OVERLAY, afloat[0], afloat[1], afloat[2], 1.0F);
            }
        }
    }
}
