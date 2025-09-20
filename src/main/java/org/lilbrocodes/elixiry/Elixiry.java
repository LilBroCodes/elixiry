package org.lilbrocodes.elixiry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.commands.RegistryCommand;
import org.lilbrocodes.elixiry.registry.ModBrewingRecipes;
import org.lilbrocodes.elixiry.registry.ModBlockEntities;
import org.lilbrocodes.elixiry.registry.ModBlocks;
import org.lilbrocodes.elixiry.registry.ModItemGroups;
import org.lilbrocodes.elixiry.util.BrewingRecipeManager;

public class Elixiry implements ModInitializer {
    public static final String MOD_ID = "elixiry";

    @Override
    public void onInitialize() {
        ModBlockEntities.initialize();
        ModBrewingRecipes.initialize();
        ModItemGroups.initialize();
        ModBlocks.initialize();

        CommandRegistrationCallback.EVENT.register(new RegistryCommand()::register);
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(BrewingRecipeManager.getInstance());
    }

    public static Identifier identify(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
