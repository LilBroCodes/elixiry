package org.lilbrocodes.elixiry.common.data.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.data.loader.json.DumpingRecipeJsonHelper;
import org.lilbrocodes.elixiry.common.recipe.dumping.DumpingRecipe;
import org.lilbrocodes.elixiry.common.recipe.management.DumpingRecipeManager;

import java.io.InputStreamReader;
import java.util.Map;

public class DumpingRecipeLoader implements SimpleSynchronousResourceReloadListener {
    public static final Identifier ID = Elixiry.identify("dumping_recipes");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        DumpingRecipeManager recipes = DumpingRecipeManager.getInstance();
        recipes.clear();

        var resources = manager.findResources("dumping", path -> path.getPath().endsWith(".json"));

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            Identifier id = entry.getKey();
            try (var reader = new InputStreamReader(entry.getValue().getInputStream())) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                DumpingRecipe recipe = DumpingRecipeJsonHelper.fromJson(json);
                recipes.register(id, recipe);
            } catch (Exception e) {
                Elixiry.LOGGER.error("Failed to load brewing recipe {}", id, e);
            }
        }
    }
}
