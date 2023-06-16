package com.hidoni.customizableelytra;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class CustomizableElytraQuiltClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        CustomizableElytraClient.init();
    }
}
