package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.util.ElytraCustomizationUtil;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @Shadow
    @Final
    @Mutable
    private static Map<ResourceLocation, ResourceLocation> VANILLA_ATLASES;


    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addCustomTextureAtlas(CallbackInfo ci) {
        VANILLA_ATLASES = new HashMap<>(VANILLA_ATLASES);
        VANILLA_ATLASES.put(ElytraCustomizationUtil.ELYTRA_TEXTURE_ATLAS, new ResourceLocation(CustomizableElytra.MOD_ID, "elytra_patterns"));
    }
}
