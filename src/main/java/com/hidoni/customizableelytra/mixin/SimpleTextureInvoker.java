package com.hidoni.customizableelytra.mixin;

import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.resources.IResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.IOException;

@Mixin(SimpleTexture.class)
public interface SimpleTextureInvoker
{
    @Invoker("getTextureData")
    SimpleTexture.TextureData invokeGetTextureData(IResourceManager resourceManager);
}
