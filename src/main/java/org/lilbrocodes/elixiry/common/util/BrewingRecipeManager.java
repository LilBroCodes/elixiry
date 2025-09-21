package org.lilbrocodes.elixiry.common.util;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.block.WitchCauldron;
import org.lilbrocodes.elixiry.common.recipe.brewing.BrewingRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrewingRecipeManager extends AbstractPseudoRegistry<BrewingRecipe>  {
    private static BrewingRecipeManager INSTANCE;
    private final List<MissingPotionEntry> vanillaPotionRecipes = new ArrayList<>();
    private final Map<Identifier, BrewingRecipe> RECIPES = new HashMap<>();

    private BrewingRecipeManager() {

    }

    public static BrewingRecipeManager getInstance() {
        if (INSTANCE == null) INSTANCE = new BrewingRecipeManager();
        return INSTANCE;
    }

    public List<BrewingRecipe> getRecipesForConditions(Potion input, WitchCauldron.HeatState heat) {
        List<BrewingRecipe> matches = new ArrayList<>();
        for (BrewingRecipe recipe : RECIPES.values()) {
            if (recipe.base == input && recipe.heat == heat) {
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

    public void clear() {
        RECIPES.clear();
    }

    public static String tryGetRegistryName(Potion potion) {
        Identifier id = Registries.POTION.getId(potion);
        if (id != null) return id.toString();
        else return Text.translatable(potion.finishTranslationKey("item.minecraft.potion.effect.")).getString();
    }

    public Identifier find(BrewingRecipe recipe) {
        for (Map.Entry<Identifier, BrewingRecipe> entry : RECIPES.entrySet()) {
            if (entry.getValue() == recipe) return entry.getKey();
        }
        return null;
    }

    public boolean containsEquivalent(Potion input, Potion output, Item item) {
        for (BrewingRecipe recipe : RECIPES.values()) {
            if (recipe.base == input && recipe.result == output && recipe.hasItemStep(item)) {
                return true;
            }
        }
        return false;
    }

    public void addVanillaRecipe(Potion input, Potion output, Item item) {
        vanillaPotionRecipes.add(new MissingPotionEntry(input, output, item));
    }

    public void warnOfMissingEntries() {
        vanillaPotionRecipes.stream().filter(entry -> !containsEquivalent(entry.input, entry.output, entry.item)).forEach(entry -> {
            Elixiry.LOGGER.warn(
                    "Vanilla recipe for {Input: {}, Output: {}, Item: {}} does not have a corresponding custom one!",
                    tryGetRegistryName(entry.input),
                    tryGetRegistryName(entry.output),
                    Text.translatable(entry.item.getTranslationKey()).getString()
            );
        });
    }

    public record MissingPotionEntry(Potion input, Potion output, Item item) {}
}
