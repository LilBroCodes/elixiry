package org.lilbrocodes.elixiry.client;

import net.fabricmc.api.ClientModInitializer;
import org.lilbrocodes.elixiry.common.registry.ModBlockEntities;
import org.lilbrocodes.elixiry.common.registry.ModBlocks;
import org.lilbrocodes.elixiry.common.registry.ModModelLayers;

public class ElixiryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModBlockEntities.initializeClient();
        ModModelLayers.initializeClient();
        ModBlocks.initializeClient();
    }
}
