package org.lilbrocodes.elixiry.util;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.block.WitchCauldron;
import org.lilbrocodes.elixiry.recipe.brewing.BrewingRecipe;

import java.util.HashMap;
import java.util.Map;

public class BrewingRecipeManager extends AbstractPseudoRegistry<BrewingRecipe> {
    private static BrewingRecipeManager INSTANCE;
    private final Map<Identifier, BrewingRecipe> RECIPES = new HashMap<>();

    private BrewingRecipeManager() {

    }

    public static BrewingRecipeManager getInstance() {
        if (INSTANCE == null) INSTANCE = new BrewingRecipeManager();
        return INSTANCE;
    }

    public BrewingRecipe getRecipeForConditions(Potion input, WitchCauldron.HeatState heat) {
        for (BrewingRecipe recipe : RECIPES.values()) {
            if (recipe.base == input && recipe.heat == heat) return recipe;
        }
        return null;
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
