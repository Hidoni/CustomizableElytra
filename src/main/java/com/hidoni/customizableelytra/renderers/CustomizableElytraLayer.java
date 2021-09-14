package com.hidoni.customizableelytra.renderers;

import com.google.common.collect.ImmutableList;
import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.mixin.ElytraLayerAccessor;
import com.hidoni.customizableelytra.renderers.models.ElytraWingModel;
import com.hidoni.customizableelytra.renderers.models.MirroredElytraWingModel;
import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.util.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;
import java.util.Optional;

public class CustomizableElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
    public static final ResourceLocation TEXTURE_DYEABLE_ELYTRA = new ResourceLocation(CustomizableElytra.MOD_ID, "textures/entity/elytra.png");
    private final ElytraModel<T> modelElytra = new ElytraModel<>();
    private final ElytraWingModel<T> leftElytraWing = new ElytraWingModel<>();
    private final MirroredElytraWingModel<T> rightElytraWing = new MirroredElytraWingModel<>();

    public CustomizableElytraLayer(RenderLayerParent<T, M> rendererIn) {
        super(rendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack elytra = ElytraInventoryUtil.tryFindElytra(entitylivingbaseIn);
        if (elytra != ItemStack.EMPTY) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0D, 0.0D, 0.125D);
            ElytraCustomizationData data = ElytraCustomizationUtil.getData(elytra);
            if (data.type != ElytraCustomizationData.CustomizationType.Split) {
                this.getParentModel().copyPropertiesTo(this.modelElytra);
                ResourceLocation elytraTexture = getTextureWithCape(entitylivingbaseIn, elytra.getTag(), data.handler.isWingCapeHidden(0));
                data.handler.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, this.modelElytra, elytraTexture, elytra.hasFoil());
            } else {
                List<ElytraWingModel<T>> models = ImmutableList.of(leftElytraWing, rightElytraWing);
                for (ElytraWingModel<T> model : models) {
                    this.getParentModel().copyPropertiesTo(model);
                }
                CompoundTag wingInfo = elytra.getTagElement("WingInfo");
                ResourceLocation leftWingTexture = getTextureWithCape(entitylivingbaseIn, wingInfo.getCompound("left"), data.handler.isWingCapeHidden(0));
                ResourceLocation rightWingTexture = getTextureWithCape(entitylivingbaseIn, wingInfo.getCompound("right"), data.handler.isWingCapeHidden(1));
                ((SplitCustomizationHandler) data.handler).render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, models, leftWingTexture, rightWingTexture, elytra.hasFoil());
            }
            matrixStackIn.popPose();
        }
    }

    private ResourceLocation getTextureWithCape(T entitylivingbaseIn, CompoundTag customizationTag, boolean capeHidden) {
        if (!capeHidden) {
            if (entitylivingbaseIn instanceof AbstractClientPlayer) {
                AbstractClientPlayer abstractclientplayerentity = (AbstractClientPlayer) entitylivingbaseIn;
                if (abstractclientplayerentity.isElytraLoaded() && abstractclientplayerentity.getElytraTextureLocation() != null) {
                    return ElytraTextureUtil.getGrayscale(abstractclientplayerentity.getElytraTextureLocation());
                } else if (abstractclientplayerentity.isCapeLoaded() && abstractclientplayerentity.getCloakTextureLocation() != null && abstractclientplayerentity.isModelPartShown(PlayerModelPart.CAPE)) {
                    return ElytraTextureUtil.getGrayscale(abstractclientplayerentity.getCloakTextureLocation());
                }
            }
            // TODO: Bring back Aether integration when it updates
        }
        return getElytraTexture(customizationTag, entitylivingbaseIn);
    }

    @Override
    public boolean shouldRender(ItemStack stack, LivingEntity entity) {
        return stack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get();
    }

    public ResourceLocation getElytraTexture(CompoundTag customizationTag, T entity) {
        if (ElytraCustomizationUtil.getData(customizationTag).type != ElytraCustomizationData.CustomizationType.None) {
            return TEXTURE_DYEABLE_ELYTRA;
        }
        return ((ElytraLayerAccessor) this).getDefaultElytraTexture();
    }
}
