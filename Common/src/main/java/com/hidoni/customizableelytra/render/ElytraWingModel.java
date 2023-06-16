package com.hidoni.customizableelytra.render;

import com.hidoni.customizableelytra.mixin.ElytraModelAccessor;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ElytraWingModel<T extends LivingEntity> extends AgeableListModel<T> {
    protected ElytraModel<T> elytraParent;
    protected boolean isRightWing;

    public ElytraWingModel(ElytraModel<T> elytraParent, boolean rightWing) {
        this.elytraParent = elytraParent;
        this.isRightWing = rightWing;
    }

    @Override
    protected @NotNull Iterable<ModelPart> headParts() {
        return List.of();
    }

    @Override
    protected @NotNull Iterable<ModelPart> bodyParts() {
        if (this.isRightWing) {
            return List.of(((ElytraModelAccessor)elytraParent).getRightWing());
        }
        return List.of(((ElytraModelAccessor)elytraParent).getLeftWing());
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        throw new RuntimeException("Unexpected call to ElytraWingModel.setupAnim!");
    }
}
