package com.hidoni.customizableelytra.render;

import com.hidoni.customizableelytra.mixin.TextureImageInvoker;
import net.minecraft.server.packs.resources.ResourceManager;

public interface ImageTextureProvider {
    /**
     * We need to call {@link net.minecraft.client.renderer.texture.SimpleTexture#getTextureImage(ResourceManager)}
     * to get a {@link com.mojang.blaze3d.platform.NativeImage} instance in the code that turns capes to grayscale
     * but not only is it protected, it also returns a package-protected {@link net.minecraft.client.renderer.texture.SimpleTexture.TextureImage}.
     * This is a problem because {@link org.spongepowered.asm.mixin.Shadow} doesn't work on a package-protected class and does not know how to handle using a super class of its target.
     * Instead, there is the following workaround:
     * <p>
     * {@link com.hidoni.customizableelytra.mixin.SimpleTextureMixin} mixes this class into {@link net.minecraft.client.renderer.texture.SimpleTexture SimpleTexture}.
     * It implements it by invoking {@link net.minecraft.client.renderer.texture.SimpleTexture#getTextureImage(ResourceManager) getTextureImage} using Java Reflection
     * which allows {@link net.minecraft.client.renderer.texture.SimpleTexture.TextureImage TextureImage} to be returned as an {@link Object},
     * which we then cast into a {@link com.hidoni.customizableelytra.mixin.TextureImageInvoker} which finally returns the
     * {@link com.mojang.blaze3d.platform.NativeImage NativeImage} we wanted all along.
     */
    TextureImageInvoker getImageTexture(ResourceManager resourceManager);
}
