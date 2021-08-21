package com.hidoni.customizableelytra.mixin;

import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.IResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SimpleTexture.class)
public interface SimpleTextureInvoker {
    @Invoker("getTextureData")
    SimpleTexture.TextureData invokeGetTextureData(IResourceManager resourceManager);
}
