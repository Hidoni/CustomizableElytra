package com.hidoni.customizableelytra.events;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtlasStitchingHandler {
    @SubscribeEvent
    public static void onAtlasStiching(TextureStitchEvent.Pre event) {
        ResourceLocation stitching = event.getMap().location();
        if (stitching.equals(TextureAtlas.LOCATION_BLOCKS)) // Currently stitching to block textures, should probably stitch to separate atlas just like shields & banners do
        {
            for (BannerPattern bannerpattern : BannerPattern.values()) {
                ResourceLocation textureLocation = CustomizableElytraItem.getTextureLocation(bannerpattern);
                boolean succeeded = event.addSprite(textureLocation);
                if (!succeeded) {
                    CustomizableElytra.LOGGER.error("Failed to add " + textureLocation + " to texture atlas!");
                } else {
                    CustomizableElytra.LOGGER.debug("Added " + textureLocation + " to texture atlas.");
                }
            }
        }
    }
}
