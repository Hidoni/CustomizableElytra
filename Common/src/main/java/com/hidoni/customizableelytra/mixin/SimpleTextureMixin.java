package com.hidoni.customizableelytra.mixin;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.render.ImageTextureProvider;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(SimpleTexture.class)
public class SimpleTextureMixin implements ImageTextureProvider {
    @Unique
    private Method customizableElytra$getTextureImage = null;

    @Unique
    private boolean customizableElytra$findGetTextureImageMethod() {
        Method[] declaredMethods = SimpleTexture.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(ResourceManager.class) && method.getReturnType().getPackageName().equals(SimpleTexture.class.getPackageName())) {
                customizableElytra$getTextureImage = method;
                Constants.LOG.debug("Found getTextureImage method: " + method);
                return true;
            }
        }
        return false;
    }

    @Override
    public TextureImageInvoker getImageTexture(ResourceManager resourceManager) {
        try {
            if (customizableElytra$getTextureImage == null && !customizableElytra$findGetTextureImageMethod()) {
                throw new RuntimeException("Failed to find getTextureImage!");
            }
            return (TextureImageInvoker) customizableElytra$getTextureImage.invoke(this, resourceManager);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
