package org.lilbrocodes.elixiry.common.util;


import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.common.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.common.recipe.management.BrewingRecipeManager;
import org.lilbrocodes.elixiry.common.recipe.management.DumpingRecipeManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractPseudoRegistry<V> {
    protected final Map<Identifier, V> values;

    protected AbstractPseudoRegistry() {
        this.values = new HashMap<>();
    }

    public void register(Identifier id, V value) {
        values.put(id, value);
    }

    public V get(Identifier id) {
        return values.get(id);
    }

    public Map<Identifier, V> getAll() {
        return values;
    }

    public void clear() {
        values.clear();
    }

    public Identifier find(V recipe) {
        for (Map.Entry<Identifier, V> entry : values.entrySet()) {
            if (entry.getValue() == recipe) return entry.getKey();
        }
        return null;
    }

    public static AbstractPseudoRegistry<?> registry(String id) {
        if (Objects.equals(id, "brewing_recipes")) return BrewingRecipeManager.getInstance();
        if (Objects.equals(id, "dumping_recipes")) return DumpingRecipeManager.getInstance();
        else return null;
    }
}
