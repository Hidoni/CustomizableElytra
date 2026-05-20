package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.Constants;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(AtlasManager.class)
public class AtlasManagerMixin {
    @Shadow
    @Final
    @Mutable
    private static List<AtlasManager.AtlasConfig> KNOWN_ATLASES;


    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addCustomTextureAtlas(CallbackInfo ci) {
        KNOWN_ATLASES = new ArrayList<>(KNOWN_ATLASES);
        KNOWN_ATLASES.add(new AtlasManager.AtlasConfig(Constants.ELYTRA_BANNER_SHEET, Constants.ELYTRA_BANNER_ATLAS, false));
    }
}
