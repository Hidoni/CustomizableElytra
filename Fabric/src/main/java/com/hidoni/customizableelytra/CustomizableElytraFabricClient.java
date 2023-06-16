package com.hidoni.customizableelytra;

import net.fabricmc.api.ClientModInitializer;

public class CustomizableElytraFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomizableElytraClient.init();
    }
}
