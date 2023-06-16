package com.hidoni.customizableelytra.render;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.mixin.*;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureUtils {
    private static final Map<ResourceLocation, ResourceLocation> TEXTURE_CACHE = new HashMap<>();

    private static void convertTextureToGrayscale(NativeImage nativeImage) {
        for (int x = 0; x < nativeImage.getWidth(); x++) {
            for (int y = 0; y < nativeImage.getHeight(); y++) {
                int pixelRGBA = nativeImage.getPixelRGBA(x, y);
                int originalRGB = pixelRGBA & 0xFFFFFF;
                int grayscale = (((originalRGB & 0xFF0000) >> 16) + ((originalRGB & 0xFF00) >> 8) + (originalRGB & 0xFF)) / 3;
                int newRGB = 0x010101 * grayscale;
                nativeImage.setPixelRGBA(x, y, (pixelRGBA & 0xFF000000) | (newRGB));
            }
        }
    }

    private static NativeImage getNativeImageFromTexture(ResourceLocation locationIn) {
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(locationIn);
        if (texture instanceof DynamicTexture dynamicTexture) {
            NativeImage dynamicTextureData = dynamicTexture.getPixels();
            if (dynamicTextureData != null) {
                NativeImage returnTexture = new NativeImage(dynamicTextureData.getWidth(), dynamicTextureData.getHeight(), false);
                returnTexture.copyFrom(dynamicTextureData);
                return returnTexture;
            }
        } else if (texture instanceof HttpTexture) {
            File cacheFile = ((HttpTextureAccessor) texture).getCacheFile();
            if (cacheFile != null) {
                try {
                    return ((HttpTextureInvoker) texture).callLoadTexture(new FileInputStream(cacheFile));
                } catch (FileNotFoundException e) {
                    return null;
                }
            }
            return null;
        } else if (texture instanceof SimpleTexture) {
            try {
                return ((ImageTextureProvider)texture).getImageTexture(Minecraft.getInstance().getResourceManager()).invokeGetImage();
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private static ResourceLocation createGrayscaleTexture(ResourceLocation locationIn) {
        Constants.LOG.debug("Creating grayscale texture for: " + locationIn);
        NativeImage texture = getNativeImageFromTexture(locationIn);
        if (texture == null) {
            return locationIn;
        }
        convertTextureToGrayscale(texture);
        ResourceLocation locationOut = new ResourceLocation(Constants.MOD_ID, "grayscale_" + locationIn.getPath());
        Minecraft.getInstance().getTextureManager().register(locationOut, new DynamicTexture(texture));
        return locationOut;
    }

    public static ResourceLocation getGrayscale(ResourceLocation locationIn) {
        return TEXTURE_CACHE.computeIfAbsent(locationIn, TextureUtils::createGrayscaleTexture);
    }
}
