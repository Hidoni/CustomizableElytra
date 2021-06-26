package com.hidoni.customizableelytra.util;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.mixin.SimpleTextureInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElytraTextureUtil
{
    private static final Map<ResourceLocation, ResourceLocation> TEXTURE_CACHE = new HashMap<>();

    private static NativeImage convertTextureToGrayscale(SimpleTexture texture)
    {
        SimpleTexture.TextureData textureData = ((SimpleTextureInvoker) texture).invokeGetTextureData(Minecraft.getInstance().getResourceManager());
        try
        {
            NativeImage nativeImage = textureData.getNativeImage();
            for (int x = 0; x < nativeImage.getWidth(); x++)
            {
                for (int y = 0; y < nativeImage.getHeight(); y++)
                {
                    int pixelRGBA = nativeImage.getPixelRGBA(x, y);
                    int originalRGB = pixelRGBA & 0xFFFFFF;
                    int grayscale = (((originalRGB & 0xFF0000) >> 16) + ((originalRGB & 0xFF00) >> 8) + (originalRGB & 0xFF)) / 3;
                    int newRGB = 0x010101 * grayscale;
                    nativeImage.setPixelRGBA(x, y, (pixelRGBA & 0xFF000000) | (newRGB));
                }
            }
            return nativeImage;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private static ResourceLocation createGrayscaleTexture(ResourceLocation locationIn)
    {
        Texture texture = Minecraft.getInstance().getTextureManager().getTexture(locationIn);
        if (texture instanceof SimpleTexture)
        {
            NativeImage converted = convertTextureToGrayscale((SimpleTexture) texture);
            if (converted == null)
            {
                return locationIn;
            }
            ResourceLocation locationOut = new ResourceLocation(CustomizableElytra.MOD_ID, "grayscale_" + locationIn.getPath());
            Minecraft.getInstance().getTextureManager().loadTexture(locationOut, new DynamicTexture(converted));
            TEXTURE_CACHE.put(locationIn, locationOut);
            return locationOut;
        }
        else
        {
            return locationIn;
        }
    }

    public static ResourceLocation getGrayscale(ResourceLocation locationIn)
    {
        if (!TEXTURE_CACHE.containsKey(locationIn))
        {
            return createGrayscaleTexture(locationIn);
        }
        return TEXTURE_CACHE.get(locationIn);
    }
}
