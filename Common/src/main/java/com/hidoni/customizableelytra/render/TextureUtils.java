package com.hidoni.customizableelytra.render;

import com.hidoni.customizableelytra.Constants;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureUtils {
    private static final Map<Identifier, Identifier> TEXTURE_CACHE = new HashMap<>();

    private static void convertTextureToGrayscale(NativeImage nativeImage) {
        for (int x = 0; x < nativeImage.getWidth(); x++) {
            for (int y = 0; y < nativeImage.getHeight(); y++) {
                int pixelARGB = nativeImage.getPixel(x, y);
                int originalRGB = pixelARGB & 0xFFFFFF;
                int grayscale = (((originalRGB & 0xFF0000) >> 16) + ((originalRGB & 0xFF00) >> 8) + (originalRGB & 0xFF)) / 3;
                int newRGB = 0x010101 * grayscale;
                nativeImage.setPixel(x, y, (pixelARGB & 0xFF000000) | (newRGB));
            }
        }
    }

    private static NativeImage copyNativeImage(NativeImage nativeImage) {
        NativeImage returnTexture = new NativeImage(nativeImage.getWidth(), nativeImage.getHeight(), false);
        returnTexture.copyFrom(nativeImage);
        return returnTexture;
    }

    private static NativeImage getNativeImageFromTexture(Identifier identifier) {
        AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(identifier);
        if (texture instanceof DynamicTexture dynamicTexture) {
            NativeImage dynamicTextureData = dynamicTexture.getPixels();
            if (dynamicTextureData != null) {
                return copyNativeImage(dynamicTextureData);
            }
        } else if (texture instanceof SimpleTexture simpleTexture) {
            try {
                return copyNativeImage(simpleTexture.loadContents(Minecraft.getInstance().getResourceManager()).image());
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private static Identifier createGrayscaleTexture(Identifier identifier) {
        Constants.LOG.debug("Creating grayscale texture for: " + identifier);
        NativeImage texture = getNativeImageFromTexture(identifier);
        if (texture == null) {
            return identifier;
        }
        convertTextureToGrayscale(texture);
        Identifier grayscaleIdentifier = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "grayscale_" + identifier.getPath());
        Minecraft.getInstance().getTextureManager().register(grayscaleIdentifier, new DynamicTexture(grayscaleIdentifier::toString, texture));
        return grayscaleIdentifier;
    }

    public static Identifier getGrayscale(Identifier identifier) {
        return TEXTURE_CACHE.computeIfAbsent(identifier, TextureUtils::createGrayscaleTexture);
    }
}
