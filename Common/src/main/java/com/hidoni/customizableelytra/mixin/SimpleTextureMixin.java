package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.render.ImageTextureProvider;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(SimpleTexture.class)
public class SimpleTextureMixin implements ImageTextureProvider {
    @Override
    public TextureImageInvoker getImageTexture(ResourceManager resourceManager) {
        try {
            Method getTextureImage = SimpleTexture.class.getDeclaredMethod("getTextureImage", ResourceManager.class);
            return (TextureImageInvoker) getTextureImage.invoke(this, resourceManager);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
