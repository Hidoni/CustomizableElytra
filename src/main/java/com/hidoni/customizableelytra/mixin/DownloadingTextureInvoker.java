package com.hidoni.customizableelytra.mixin;

import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.File;
import java.io.InputStream;

@Mixin(DownloadingTexture.class)
public interface DownloadingTextureInvoker
{
    @Invoker("loadTexture")
    NativeImage callLoadTexture(InputStream inputStreamIn);
}
