package com.hidoni.customizableelytra.renderers;

import com.gildedgames.aether.common.item.accessories.cape.CapeItem;
import com.google.common.collect.ImmutableList;
import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.mixin.ElytraLayerAccessor;
import com.hidoni.customizableelytra.renderers.models.ElytraWingModel;
import com.hidoni.customizableelytra.renderers.models.MirroredElytraWingModel;
import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.util.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
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

    public CustomizableElytraLayer(IEntityRenderer<T, M> rendererIn) {
        super(rendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack elytra = ElytraInventoryUtil.tryFindElytra(entitylivingbaseIn);
        if (elytra != ItemStack.EMPTY) {
            matrixStackIn.push();
            matrixStackIn.translate(0.0D, 0.0D, 0.125D);
            ElytraCustomizationData data = ElytraCustomizationUtil.getData(elytra);
            if (data.type != ElytraCustomizationData.CustomizationType.Split) {
                this.getEntityModel().copyModelAttributesTo(this.modelElytra);
                ResourceLocation elytraTexture = getTextureWithCape(entitylivingbaseIn, elytra.getTag(), data.handler.isWingCapeHidden(0));
                data.handler.render(matrixStackIn, bufferIn, data.handler.modifyWingLight(packedLightIn, 0), entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, this.modelElytra, elytraTexture, elytra.hasEffect());
            } else {
                List<ElytraWingModel<T>> models = ImmutableList.of(leftElytraWing, rightElytraWing);
                for (ElytraWingModel<T> model : models) {
                    this.getEntityModel().copyModelAttributesTo(model);
                }
                CompoundNBT wingInfo = elytra.getChildTag("WingInfo");
                ResourceLocation leftWingTexture = getTextureWithCape(entitylivingbaseIn, wingInfo.getCompound("left"), data.handler.isWingCapeHidden(0));
                ResourceLocation rightWingTexture = getTextureWithCape(entitylivingbaseIn, wingInfo.getCompound("right"), data.handler.isWingCapeHidden(1));
                ((SplitCustomizationHandler) data.handler).render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, models, leftWingTexture, rightWingTexture, elytra.hasEffect());
            }
            matrixStackIn.pop();
        }
    }

    private ResourceLocation getTextureWithCape(T entitylivingbaseIn, CompoundNBT customizationTag, boolean capeHidden) {
        ResourceLocation elytraTexture = null;
        boolean isTextureGrayscale = ElytraCustomizationUtil.getData(customizationTag).type != ElytraCustomizationData.CustomizationType.None;
        if (!capeHidden) {
            if (entitylivingbaseIn instanceof AbstractClientPlayerEntity) {
                AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity) entitylivingbaseIn;
                if (abstractclientplayerentity.isPlayerInfoSet() && abstractclientplayerentity.getLocationElytra() != null) {
                    elytraTexture = abstractclientplayerentity.getLocationElytra();
                } else if (abstractclientplayerentity.hasPlayerInfo() && abstractclientplayerentity.getLocationCape() != null && abstractclientplayerentity.isWearing(PlayerModelPart.CAPE)) {
                    elytraTexture = abstractclientplayerentity.getLocationCape();
                }
            }
            if (elytraTexture == null && CustomizableElytra.aetherLoaded) {
                Optional<ImmutableTriple<String, Integer, ItemStack>> curiosHelper = CuriosApi.getCuriosHelper().findEquippedCurio((item) -> item.getItem() instanceof CapeItem, entitylivingbaseIn);
                Optional<ICuriosItemHandler> curiosHandler = CuriosApi.getCuriosHelper().getCuriosHandler(entitylivingbaseIn).resolve();
                if (curiosHelper.isPresent() && curiosHandler.isPresent()) {
                    Optional<ICurioStacksHandler> stacksHandler = curiosHandler.get().getStacksHandler(curiosHelper.get().getLeft());
                    if (stacksHandler.isPresent()) {
                        CapeItem cape = (CapeItem) curiosHelper.get().getRight().getItem();
                        if (cape.getCapeTexture() != null && stacksHandler.get().getRenders().get(curiosHelper.get().getMiddle())) {
                            elytraTexture = cape.getCapeTexture();
                        }
                    }
                }
            }
        }
        if (elytraTexture == null) {
            elytraTexture = getElytraTexture(isTextureGrayscale);
        } else if (isTextureGrayscale) {
            elytraTexture = ElytraTextureUtil.getGrayscale(elytraTexture);
        }
        return elytraTexture;
    }

    @Override
    public boolean shouldRender(ItemStack stack, LivingEntity entity) {
        return stack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get();
    }

    public ResourceLocation getElytraTexture(boolean isTextureGrayscale) {
        if (isTextureGrayscale) {
            return TEXTURE_DYEABLE_ELYTRA;
        }
        return ((ElytraLayerAccessor) this).getDefaultElytraTexture();
    }
}
