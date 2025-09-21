package org.lilbrocodes.elixiry.util;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.potion.Potion;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.block.WitchCauldron;
import org.lilbrocodes.elixiry.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.registry.ModBrewingRecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrewingRecipeManager extends AbstractPseudoRegistry<BrewingRecipe> implements SimpleSynchronousResourceReloadListener {
    public static final Identifier ID = Elixiry.identify("brewing_recipe_reloader");
    private static BrewingRecipeManager INSTANCE;
    private final Map<Identifier, BrewingRecipe> RECIPES = new HashMap<>();

    private BrewingRecipeManager() {

    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        RECIPES.clear();
        ModBrewingRecipes.initialize();
    }

    public static BrewingRecipeManager getInstance() {
        if (INSTANCE == null) INSTANCE = new BrewingRecipeManager();
        return INSTANCE;
    }

    public List<BrewingRecipe> getRecipesForConditions(Potion input, WitchCauldron.HeatState heat, PotionBottle bottle) {
        List<BrewingRecipe> matches = new ArrayList<>();
        for (BrewingRecipe recipe : RECIPES.values()) {
            if (recipe.base == input && recipe.heat == heat && recipe.inputBottle == bottle) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    public void register(Identifier id, BrewingRecipe recipe) {
        RECIPES.put(id, recipe);
    }

    public BrewingRecipe get(Identifier id) {
        return RECIPES.get(id);
    }

    public Map<Identifier, BrewingRecipe> getAll() {
        return RECIPES;
    }

    public Identifier find(BrewingRecipe recipe) {
        for (Map.Entry<Identifier, BrewingRecipe> entry : RECIPES.entrySet()) {
            if (entry.getValue() == recipe) return entry.getKey();
        }
        return null;
    }
}
