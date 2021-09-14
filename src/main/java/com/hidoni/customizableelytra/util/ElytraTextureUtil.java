package com.hidoni.customizableelytra.util;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.mixin.DownloadingTextureAccessor;
import com.hidoni.customizableelytra.mixin.DownloadingTextureInvoker;
import com.hidoni.customizableelytra.mixin.SimpleTextureInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElytraTextureUtil {
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
        Texture texture = Minecraft.getInstance().getTextureManager().getTexture(locationIn);
        if (texture instanceof DynamicTexture) {
            DynamicTexture dynamicTexture = (DynamicTexture) texture;
            NativeImage dynamicTextureData = dynamicTexture.getPixels();
            if (dynamicTextureData != null) {
                NativeImage returnTexture = new NativeImage(dynamicTextureData.getWidth(), dynamicTextureData.getHeight(), false);
                returnTexture.copyFrom(dynamicTextureData);
                return returnTexture;
            }
        } else if (texture instanceof DownloadingTexture) {
            File cacheFile = ((DownloadingTextureAccessor) texture).getCacheFile();
            if (cacheFile != null) {
                try {
                    return ((DownloadingTextureInvoker) texture).callLoadTexture(new FileInputStream(cacheFile));
                } catch (FileNotFoundException e) {
                    return null;
                }
            }
            return null;
        } else if (texture instanceof SimpleTexture) {
            try {
                return ((SimpleTextureInvoker) texture).invokeGetTextureData(Minecraft.getInstance().getResourceManager()).getImage();
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private static ResourceLocation createGrayscaleTexture(ResourceLocation locationIn) {
        NativeImage texture = getNativeImageFromTexture(locationIn);
        if (texture == null) {
            return locationIn;
        }
        convertTextureToGrayscale(texture);
        ResourceLocation locationOut = new ResourceLocation(CustomizableElytra.MOD_ID, "grayscale_" + locationIn.getPath());
        Minecraft.getInstance().getTextureManager().register(locationOut, new DynamicTexture(texture));
        TEXTURE_CACHE.put(locationIn, locationOut);
        return locationOut;
    }

    public static ResourceLocation getGrayscale(ResourceLocation locationIn) {
        if (!TEXTURE_CACHE.containsKey(locationIn)) {
            CustomizableElytra.LOGGER.debug("Creating grayscale texture for: " + locationIn);
            return createGrayscaleTexture(locationIn);
        }
        return TEXTURE_CACHE.get(locationIn);
    }
}
