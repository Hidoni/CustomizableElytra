package com.hidoni.customizableelytra.renderers;

import com.google.common.collect.ImmutableList;
import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.renderers.models.ElytraWingModel;
import com.hidoni.customizableelytra.renderers.models.MirroredElytraWingModel;
import com.hidoni.customizableelytra.setup.ModItems;
import com.hidoni.customizableelytra.util.ElytraCustomizationData;
import com.hidoni.customizableelytra.util.ElytraCustomizationUtil;
import com.hidoni.customizableelytra.util.SplitCustomizationHandler;
import com.hidoni.customizableelytra.util.ElytraTextureUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.Optional;

public class CustomizableElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M>
{
    private final ElytraModel<T> modelElytra = new ElytraModel<>();
    private final ElytraWingModel<T> leftElytraWing = new ElytraWingModel<>();
    private final MirroredElytraWingModel<T> rightElytraWing = new MirroredElytraWingModel<>();
    public static final ResourceLocation TEXTURE_DYEABLE_ELYTRA = new ResourceLocation(CustomizableElytra.MOD_ID, "textures/entity/elytra.png");

    public CustomizableElytraLayer(IEntityRenderer<T, M> rendererIn)
    {
        super(rendererIn);
    }


    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        ItemStack elytra = tryFindElytra(entitylivingbaseIn);
        if (elytra != ItemStack.EMPTY)
        {
            matrixStackIn.push();
            matrixStackIn.translate(0.0D, 0.0D, 0.125D);
            ElytraCustomizationData data = ElytraCustomizationUtil.getData(elytra);
            if (data.type != ElytraCustomizationData.CustomizationType.Split)
            {
                this.getEntityModel().copyModelAttributesTo(this.modelElytra);
                data.handler.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, this.modelElytra, getTextureWithCape(entitylivingbaseIn, elytra), elytra.hasEffect());
            }
            else
            {
                List<ElytraWingModel<T>> models = ImmutableList.of(leftElytraWing, rightElytraWing);
                for (ElytraWingModel<T> model : models)
                {
                    this.getEntityModel().copyModelAttributesTo(model);
                }
                ((SplitCustomizationHandler) data.handler).render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, models, getTextureWithCape(entitylivingbaseIn, elytra), elytra.hasEffect());
            }
            matrixStackIn.pop();
        }
    }

    private ResourceLocation getTextureWithCape(T entitylivingbaseIn, ItemStack elytra)
    {
        ResourceLocation elytraTexture;
        if (entitylivingbaseIn instanceof AbstractClientPlayerEntity)
        {
            AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity) entitylivingbaseIn;
            if (abstractclientplayerentity.isPlayerInfoSet() && abstractclientplayerentity.getLocationElytra() != null)
            {
                elytraTexture = ElytraTextureUtil.getGrayscale(abstractclientplayerentity.getLocationElytra());
            }
            else if (abstractclientplayerentity.hasPlayerInfo() && abstractclientplayerentity.getLocationCape() != null && abstractclientplayerentity.isWearing(PlayerModelPart.CAPE))
            {
                elytraTexture = ElytraTextureUtil.getGrayscale(abstractclientplayerentity.getLocationCape());
            }
            else
            {
                elytraTexture = getElytraTexture(elytra, entitylivingbaseIn);
            }
        }
        else
        {
            elytraTexture = getElytraTexture(elytra, entitylivingbaseIn);
        }
        return elytraTexture;
    }

    @Override
    public boolean shouldRender(ItemStack stack, LivingEntity entity)
    {
        return stack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get();
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, T entity)
    {
        if (stack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get())
        {
            if (((CustomizableElytraItem) stack.getItem()).hasColor(stack))
            {
                return TEXTURE_DYEABLE_ELYTRA;
            }
        }
        return super.getElytraTexture(stack, entity);
    }

    public ItemStack getColytraSubItem(ItemStack stack)
    {
        CompoundNBT colytraChestTag = stack.getChildTag("colytra:ElytraUpgrade");
        if (colytraChestTag != null)
        {
            ItemStack elytraStack = ItemStack.read(colytraChestTag);
            if (elytraStack.getItem() == ModItems.CUSTOMIZABLE_ELYTRA.get())
            {
                return elytraStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getCurioElytra(LivingEntity entity)
    {
        Optional<ImmutableTriple<String, Integer, ItemStack>> curio = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.CUSTOMIZABLE_ELYTRA.get(), entity);
        if (curio.isPresent())
        {
            return curio.get().getRight();
        }
        return ItemStack.EMPTY;
    }

    public ItemStack tryFindElytra(LivingEntity entity)
    {
        ItemStack elytra = entity.getItemStackFromSlot(EquipmentSlotType.CHEST);
        if (shouldRender(elytra, entity))
        {
            return elytra;
        }
        if (CustomizableElytra.caelusLoaded)
        {
            elytra = getColytraSubItem(elytra);
            if (elytra != ItemStack.EMPTY)
            {
                return elytra;
            }
            if (CustomizableElytra.curiosLoaded)
            {
                return getCurioElytra(entity);
            }
        }
        return ItemStack.EMPTY;
    }
}
