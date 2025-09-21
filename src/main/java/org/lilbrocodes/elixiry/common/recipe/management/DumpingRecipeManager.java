package org.lilbrocodes.elixiry.common.recipe.management;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.common.recipe.dumping.DumpingRecipe;
import org.lilbrocodes.elixiry.common.util.AbstractPseudoRegistry;

import java.util.Map;
import java.util.Optional;

public class DumpingRecipeManager extends AbstractPseudoRegistry<DumpingRecipe> {
    private static DumpingRecipeManager INSTANCE;

    private DumpingRecipeManager() {

    }

    public Optional<DumpingRecipe> getRecipeForConditions(Potion potion, ItemStack stack) {
        for (DumpingRecipe recipe : values.values()) {
            if (recipe.inputItem.test(stack) && (recipe.wildPotion || recipe.inputPotion == potion)) return Optional.of(recipe);
        }
        return Optional.empty();
    }

    public static DumpingRecipeManager getInstance() {
        if (INSTANCE == null) INSTANCE = new DumpingRecipeManager();
        return INSTANCE;
    }

    @Override
    public Map<Identifier, DumpingRecipe> getAll() {
        return Map.of();
    }
}
