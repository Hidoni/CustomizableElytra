package com.hidoni.customizableelytra.util;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class BannerCustomizationHandler extends CustomizationHandler {
    private final List<Pair<BannerPattern, DyeColor>> patterns;

    public BannerCustomizationHandler(ItemStack itemIn) {
        this(itemIn.getOrCreateTag());
    }

    public BannerCustomizationHandler(CompoundTag tagIn) {
        super(tagIn.getBoolean("HideCapePattern"), tagIn.getInt("WingLightLevel"));
        CompoundTag blockEntityTag = tagIn.getCompound("BlockEntityTag");
        DyeColor baseColor = DyeColor.byId(blockEntityTag.getInt("Base"));
        ListTag patternsList = blockEntityTag.getList("Patterns", 10).copy();
        this.patterns = BannerBlockEntity.createPatterns(baseColor, patternsList);
    }

    @Override
    public int getColor(int index) {
        return ColorUtil.convertDyeColorToInt(patterns.get(0).getSecond());
    }

    @Override
    public <T extends LivingEntity, M extends AgeableListModel<T>> void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, M renderModel, ResourceLocation textureLocation, boolean hasGlint) {
        renderModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBufferDirect(bufferIn, RenderType.entityNoOutline(textureLocation), false, hasGlint);
        renderModel.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        float[] baseColor = patterns.get(0).getSecond().getTextureDiffuseColors();
        renderModel.renderToBuffer(matrixStackIn, ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityTranslucent(textureLocation), false, false), packedLightIn, OverlayTexture.NO_OVERLAY, baseColor[0], baseColor[1], baseColor[2], 1.0F);
        for (int i = 1; i < 17 && i < patterns.size(); ++i) {
            Pair<BannerPattern, DyeColor> pair = patterns.get(i);
            float[] afloat = pair.getSecond().getTextureDiffuseColors();
            Material rendermaterial = new Material(TextureAtlas.LOCATION_BLOCKS, CustomizableElytraItem.getTextureLocation(pair.getFirst()));
            if (rendermaterial.sprite().getName() != MissingTextureAtlasSprite.getLocation()) // Don't render this banner pattern if it's missing, silently hide the pattern
            {
                renderModel.renderToBuffer(matrixStackIn, rendermaterial.buffer(bufferIn, RenderType::entityTranslucent), packedLightIn, OverlayTexture.NO_OVERLAY, afloat[0], afloat[1], afloat[2], 1.0F);
            }
        }
    }
}
