package org.lilbrocodes.elixiry.common.recipe.brewing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.common.block.WitchCauldron;
import org.lilbrocodes.elixiry.common.recipe.brewing.modifier.BrewingRecipeModifier;
import org.lilbrocodes.elixiry.common.recipe.brewing.step.BrewingRecipeStep;
import org.lilbrocodes.elixiry.common.recipe.brewing.step.BrewingRecipeSteps;
import org.lilbrocodes.elixiry.common.util.IntClamper;
import org.lilbrocodes.elixiry.common.util.ListBuilder;
import org.lilbrocodes.elixiry.common.util.NotAllFieldsFilledException;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class BrewingRecipe {
    public final WitchCauldron.HeatState heat;
    public final Potion base;
    public final Potion result;
    public final List<BrewingRecipeStep<?>> steps;
    public final List<BrewingRecipeModifier<?, ?>> modifiers;
    public final int minimumArcaneEnergy;

    public BrewingRecipe(WitchCauldron.HeatState heat, Potion base, Potion result, List<BrewingRecipeStep<?>> steps, List<BrewingRecipeModifier<?, ?>> modifiers, int minimumArcaneEnergy) {
        this.heat = heat;
        this.base = base;
        this.result = result;
        this.steps = steps;
        this.modifiers = modifiers;
        this.minimumArcaneEnergy = minimumArcaneEnergy;
    }

    public static Builder builder(Potion result) {
        return new Builder(result);
    }

    public static class Builder {
        private WitchCauldron.HeatState heat = WitchCauldron.HeatState.NONE;
        private Potion base = Potions.EMPTY;
        private IntClamper.ValidatedInt<Builder> energy;
        private final Potion result;

        private final ListBuilder<Builder, BrewingRecipeStep<?>> steps = new ListBuilder<>(this);
        private final ListBuilder<Builder, BrewingRecipeModifier<?, ?>> modifiers = new ListBuilder<>(this);

        private Builder(Potion result) {
            this.result = result;
        }

        public Builder heat(WitchCauldron.HeatState heat) {
            this.heat = heat;
            return this;
        }

        public Builder base(Potion base) {
            this.base = base;
            return this;
        }

        public Builder energy(IntClamper.ValidatedInt<Builder> energy) {
            this.energy = energy;
            return this;
        }

        public ListBuilder<Builder, BrewingRecipeStep<?>> steps() {
            return steps;
        }

        public ListBuilder<Builder, BrewingRecipeModifier<?, ?>> modifiers() {
            return modifiers;
        }

        public BrewingRecipe build() {
            if (heat == null || base == null || energy == null || result == null) {
                throw new NotAllFieldsFilledException();
            }
            return new BrewingRecipe(heat, base, result, steps.build(), modifiers.build(), energy.build());
        }
    }

    public JsonObject toJson() {
        JsonObject root = new JsonObject();

        root.addProperty("result", Registries.POTION.getId(this.result).toString());
        root.addProperty("base", Registries.POTION.getId(this.base).toString());
        root.addProperty("heat", this.heat.name().toLowerCase(Locale.ROOT));
        root.addProperty("energy", this.minimumArcaneEnergy);

        JsonArray modifiersArray = new JsonArray();
        for (BrewingRecipeModifier<?, ?> modifier : this.modifiers) {
            modifiersArray.add(modifier.toJson());
        }
        root.add("modifiers", modifiersArray);

        JsonArray stepsArray = new JsonArray();
        for (BrewingRecipeStep<?> step : this.steps) {
            stepsArray.add(step.toJson());
        }
        root.add("steps", stepsArray);

        return root;
    }

    public CompletableFuture<?> save(DataWriter writer, Path root, Identifier id) {
        return CompletableFuture.runAsync(() -> {
            JsonObject json = this.toJson();
            Path path = root.resolve("data/" + id.getNamespace() + "/brewing/" + id.getPath() + ".json");
            DataProvider.writeToPath(writer, json, path);
        });
    }

    public boolean hasItemStep(Item item) {
        for (BrewingRecipeStep<?> step : steps) {
            if (step instanceof BrewingRecipeSteps.BrewingRecipeItemStep itemStep && itemStep.ingredient.test(new ItemStack(item))) return true;
        }
        return false;
    }
}
