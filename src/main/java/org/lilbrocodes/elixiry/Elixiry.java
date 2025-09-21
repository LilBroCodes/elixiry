package org.lilbrocodes.elixiry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lilbrocodes.elixiry.common.commands.RegistryCommand;
import org.lilbrocodes.elixiry.common.config.Configs;
import org.lilbrocodes.elixiry.common.data.loader.BrewingRecipeLoader;
import org.lilbrocodes.elixiry.common.data.loader.DumpingRecipeLoader;
import org.lilbrocodes.elixiry.common.registry.ModBlockEntities;
import org.lilbrocodes.elixiry.common.registry.ModBlocks;
import org.lilbrocodes.elixiry.common.registry.ModItemGroups;

public class Elixiry implements ModInitializer {
    public static final String MOD_ID = "elixiry";
    public static final Logger LOGGER = LogManager.getLogger(Elixiry.class);
    public static final boolean DEBUG = true;

    @Override
    public void onInitialize() {
        ModBlockEntities.initialize();
        ModItemGroups.initialize();
        ModBlocks.initialize();
        Configs.initialize();

        CommandRegistrationCallback.EVENT.register(new RegistryCommand()::register);
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BrewingRecipeLoader());
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new DumpingRecipeLoader());
    }

    public static Identifier identify(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
