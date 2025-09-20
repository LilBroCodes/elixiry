package org.lilbrocodes.elixiry.client;

import net.fabricmc.api.ClientModInitializer;
import org.lilbrocodes.elixiry.registry.ModBlockEntities;
import org.lilbrocodes.elixiry.registry.ModBlocks;
import org.lilbrocodes.elixiry.registry.ModModelLayers;

public class ElixiryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModBlockEntities.initializeClient();
        ModModelLayers.initializeClient();
        ModBlocks.initializeClient();
    }
}
