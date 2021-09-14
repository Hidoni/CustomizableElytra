package com.hidoni.customizableelytra.mixin;

import net.minecraft.client.renderer.texture.HttpTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.File;

@Mixin(HttpTexture.class)
public interface DownloadingTextureAccessor {
    @Accessor("cacheFile")
    File getCacheFile();
}
