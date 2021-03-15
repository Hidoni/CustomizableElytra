package com.hidoni.customizableelytra.renderers.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

public class ElytraWingModel<T extends LivingEntity> extends AgeableModel<T>
{
    protected ModelRenderer wing;

    public ElytraWingModel()
    {
        wing = new ModelRenderer(this, 22, 0);
        this.wing.addBox(-10.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, 1.0F);
    }

    @Override
    protected Iterable<ModelRenderer> getHeadParts()
    {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelRenderer> getBodyParts()
    {
        return ImmutableList.of(this.wing);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        float f = 0.2617994F;
        float f1 = -0.2617994F;
        float f2 = 0.0F;
        float f3 = 0.0F;
        if (entityIn.isElytraFlying()) {
            float f4 = 1.0F;
            Vector3d vector3d = entityIn.getMotion();
            if (vector3d.y < 0.0D) {
                Vector3d vector3d1 = vector3d.normalize();
                f4 = 1.0F - (float)Math.pow(-vector3d1.y, 1.5D);
            }

            f = f4 * 0.34906584F + (1.0F - f4) * f;
            f1 = f4 * (-(float)Math.PI / 2F) + (1.0F - f4) * f1;
        } else if (entityIn.isCrouching()) {
            f = 0.6981317F;
            f1 = (-(float)Math.PI / 4F);
            f2 = 3.0F;
            f3 = 0.08726646F;
        }

        this.wing.rotationPointX = 5.0F;
        this.wing.rotationPointY = f2;
        if (entityIn instanceof AbstractClientPlayerEntity) {
            AbstractClientPlayerEntity abstractclientplayerentity = (AbstractClientPlayerEntity)entityIn;
            abstractclientplayerentity.rotateElytraX = (float)((double)abstractclientplayerentity.rotateElytraX + (double)(f - abstractclientplayerentity.rotateElytraX) * 0.1D);
            abstractclientplayerentity.rotateElytraY = (float)((double)abstractclientplayerentity.rotateElytraY + (double)(f3 - abstractclientplayerentity.rotateElytraY) * 0.1D);
            abstractclientplayerentity.rotateElytraZ = (float)((double)abstractclientplayerentity.rotateElytraZ + (double)(f1 - abstractclientplayerentity.rotateElytraZ) * 0.1D);
            this.wing.rotateAngleX = abstractclientplayerentity.rotateElytraX;
            this.wing.rotateAngleY = abstractclientplayerentity.rotateElytraY;
            this.wing.rotateAngleZ = abstractclientplayerentity.rotateElytraZ;
        } else {
            this.wing.rotateAngleX = f;
            this.wing.rotateAngleZ = f1;
            this.wing.rotateAngleY = f3;
        }
    }
}
