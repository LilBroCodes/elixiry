package org.lilbrocodes.elixiry.common.data.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.data.loader.json.BrewingRecipeJsonHelper;
import org.lilbrocodes.elixiry.common.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.common.util.BrewingRecipeManager;

import java.io.InputStreamReader;
import java.util.Map;

public class BrewingRecipeLoader implements SimpleSynchronousResourceReloadListener {
    public static final Identifier ID = Elixiry.identify("brewing_recipes");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        BrewingRecipeManager recipes = BrewingRecipeManager.getInstance();
        recipes.clear();

        var resources = manager.findResources("brewing", path -> path.getPath().endsWith(".json"));

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            Identifier id = entry.getKey();
            try (var reader = new InputStreamReader(entry.getValue().getInputStream())) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                BrewingRecipe recipe = BrewingRecipeJsonHelper.fromJson(id, json);
                recipes.register(id, recipe);
            } catch (Exception e) {
                Elixiry.LOGGER.error("Failed to load brewing recipe {}", id, e);
            }
        }

        recipes.warnOfMissingEntries();
    }
}

