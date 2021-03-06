package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtlasStitchingHandler
{
    @SubscribeEvent
    public static void onAtlasStiching(TextureStitchEvent.Pre event)
    {
        ResourceLocation stitching = event.getMap().getTextureLocation();
        if (stitching.equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) // Currently stitching to block textures, should probably stitch to separate atlas just like shields & banners do
        {
            for (BannerPattern bannerpattern : BannerPattern.values())
            {
                ResourceLocation textureLocation = CustomizableElytraItem.getTextureLocation(bannerpattern);
                boolean succeeded = event.addSprite(textureLocation);
                if (!succeeded)
                {
                    CustomizableElytra.LOGGER.error("Failed to add " + textureLocation + " to texture atlas!");
                }
                else
                {
                    CustomizableElytra.LOGGER.debug("Added " + textureLocation + " to texture atlas.");
                }
            }
        }
    }
}
