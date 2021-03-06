package com.hidoni.customizableelytra.renderers.models;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class MirroredElytraWingModel<T extends LivingEntity> extends ElytraWingModel<T>
{
    public MirroredElytraWingModel()
    {
        wing = new ModelRenderer(this, 22, 0);
        this.wing.mirror = true;
        this.wing.addBox(0.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, 1.0F);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.wing.rotationPointX = -this.wing.rotationPointX;
        this.wing.rotateAngleY = -this.wing.rotateAngleY;
        this.wing.rotateAngleZ = -this.wing.rotateAngleZ;
    }
}
