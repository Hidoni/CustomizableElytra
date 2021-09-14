package com.hidoni.customizableelytra.mixin;

import net.minecraft.client.renderer.texture.HttpTexture;
import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.InputStream;

@Mixin(HttpTexture.class)
public interface DownloadingTextureInvoker {
    @Invoker("load")
    NativeImage callLoadTexture(InputStream inputStreamIn);
}
