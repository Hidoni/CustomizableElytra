package com.hidoni.customizableelytra.platform;

import com.hidoni.customizableelytra.platform.services.IPlatformHelper;
import net.fabricmc.api.EnvType;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;

import java.nio.file.Path;

public class QuiltPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Quilt";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return QuiltLoader.isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return QuiltLoader.isDevelopmentEnvironment();
    }

    @Override
    public boolean isClientEnv() {
        return MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public Path getConfigDirectory() {
        return QuiltLoader.getConfigDir();
    }
}
