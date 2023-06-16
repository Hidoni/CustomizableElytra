package com.hidoni.customizableelytra.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.IOException;

@Mixin(targets="net.minecraft.client.renderer.texture.SimpleTexture$TextureImage")
public interface TextureImageInvoker {
    @Invoker
    NativeImage invokeGetImage() throws IOException;
}