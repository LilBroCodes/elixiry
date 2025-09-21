package org.lilbrocodes.elixiry.common.data.loader.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.block.WitchCauldron;
import org.lilbrocodes.elixiry.common.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.common.recipe.brewing.modifier.BrewingRecipeModifier;
import org.lilbrocodes.elixiry.common.recipe.brewing.modifier.BrewingRecipeModifiers;
import org.lilbrocodes.elixiry.common.recipe.brewing.step.BrewingRecipeStep;
import org.lilbrocodes.elixiry.common.recipe.brewing.step.BrewingRecipeSteps;

import java.util.ArrayList;
import java.util.List;

public class BrewingRecipeJsonHelper {
    public static BrewingRecipe fromJson(Identifier id, JsonObject json) {
        Potion result = Registries.POTION.get(new Identifier(json.get("result").getAsString()));
        Potion base = Registries.POTION.get(new Identifier(json.get("base").getAsString()));
        WitchCauldron.HeatState heat = WitchCauldron.HeatState.valueOf(json.get("heat").getAsString().toUpperCase());
        int energy = json.get("energy").getAsInt();

        List<BrewingRecipeModifier<?, ?>> modifiers = parseModifiers(id, json.getAsJsonArray("modifiers"));
        List<BrewingRecipeStep<?>> steps = parseSteps(id, json.getAsJsonArray("steps"));

        return new BrewingRecipe(heat, base, result, steps, modifiers, energy);
    }

    public static List<BrewingRecipeModifier<?, ?>> parseModifiers(Identifier id, JsonArray array) {
        List<BrewingRecipeModifier<?, ?>> result = new ArrayList<>();
        if (array == null) return result;

        for (JsonElement element : array) {
            if (!element.isJsonObject()) {
                Elixiry.LOGGER.error("Invalid modifier entry in brewing recipe {}: {}", id, element);
                continue;
            }
            JsonObject obj = element.getAsJsonObject();
            try {
                result.add(BrewingRecipeModifiers.fromJson(obj));
            } catch (Exception e) {
                Elixiry.LOGGER.error("Failed to parse brewing modifier in recipe {}: {}", id, e.getMessage());
            }
        }
        return result;
    }

    public static List<BrewingRecipeStep<?>> parseSteps(Identifier id, JsonArray array) {
        List<BrewingRecipeStep<?>> result = new ArrayList<>();
        if (array == null) return result;

        for (JsonElement element : array) {
            if (!element.isJsonObject()) {
                Elixiry.LOGGER.error("Invalid step entry in brewing recipe {}: {}", id, element);
                continue;
            }
            JsonObject obj = element.getAsJsonObject();
            try {
                result.add(BrewingRecipeSteps.fromJson(obj));
            } catch (Exception e) {
                Elixiry.LOGGER.error("Failed to parse brewing step in recipe {}: {}", id, e.getMessage());
            }
        }
        return result;
    }
}
