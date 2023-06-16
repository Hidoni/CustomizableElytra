package com.hidoni.customizableelytra.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {
    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    boolean isClientEnv();

    Path getConfigDirectory();
}