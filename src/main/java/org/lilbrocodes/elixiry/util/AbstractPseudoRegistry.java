package org.lilbrocodes.elixiry.util;


import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractPseudoRegistry<V> {
    public abstract Map<Identifier, V> getAll();

    public static AbstractPseudoRegistry<?> registry(String id) {
        if (Objects.equals(id, "brewing_recipes")) return BrewingRecipeManager.getInstance();
        else return null;
    }
}
