package com.hidoni.customizableelytra.mixin;

import net.minecraft.client.renderer.texture.DownloadingTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.File;

@Mixin(DownloadingTexture.class)
public interface DownloadingTextureAccessor {
    @Accessor("cacheFile")
    File getCacheFile();
}
