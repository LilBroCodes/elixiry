package org.lilbrocodes.elixiry.common.recipe.common;

import com.google.gson.JsonObject;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface SerializableRecipe {
    JsonObject toJson();
    String getSubPath();

    default CompletableFuture<?> save(DataWriter writer, Path root, Identifier id) {
        return CompletableFuture.runAsync(() -> {
            JsonObject json = toJson();
            Path path = root.resolve("data/" + id.getNamespace() + String.format("\\%s\\", getSubPath()) + id.getPath() + ".json");
            DataProvider.writeToPath(writer, json, path);
        });
    }
}
