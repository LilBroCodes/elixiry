package org.lilbrocodes.elixiry.recipe.brewing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import org.lilbrocodes.elixiry.block.WitchCauldron;
import org.lilbrocodes.elixiry.recipe.brewing.modifier.BrewingRecipeModifier;
import org.lilbrocodes.elixiry.recipe.brewing.step.BrewingRecipeStep;
import org.lilbrocodes.elixiry.recipe.brewing.step.BrewingRecipeSteps;
import org.lilbrocodes.elixiry.util.IntClamper;
import org.lilbrocodes.elixiry.util.ListBuilder;
import org.lilbrocodes.elixiry.util.NotAllFieldsFilledException;
import org.lilbrocodes.elixiry.util.PotionBottle;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BrewingRecipe {
    public final WitchCauldron.HeatState heat;
    public final Potion base;
    public final Potion result;
    public final List<BrewingRecipeStep<?>> steps;
    public final List<BrewingRecipeModifier<?, ?>> modifiers;
    public final PotionBottle inputBottle;
    public final PotionBottle resultBottle;
    public final int minimumArcaneEnergy;

    private BrewingRecipe(WitchCauldron.HeatState heat, Potion base, Potion result, List<BrewingRecipeStep<?>> steps, List<BrewingRecipeModifier<?, ?>> modifiers, PotionBottle inputBottle, PotionBottle resultBottle, int minimumArcaneEnergy) {
        this.heat = heat;
        this.base = base;
        this.result = result;
        this.steps = steps;
        this.modifiers = modifiers;
        this.inputBottle = inputBottle;
        this.resultBottle = resultBottle;
        this.minimumArcaneEnergy = minimumArcaneEnergy;
    }

    public static Builder builder(Potion result) {
        return new Builder(result);
    }

    public static class Builder {
        private WitchCauldron.HeatState heat = WitchCauldron.HeatState.NONE;
        private Potion base = Potions.EMPTY;
        private PotionBottle inputBottle = PotionBottle.NORMAL;
        private PotionBottle resultBottle = PotionBottle.NORMAL;
        private IntClamper.ValidatedInt<Builder> energy;
        private final Potion result;

        private final ListBuilder<Builder, BrewingRecipeStep<?>> steps = new ListBuilder<>(this);
        private final ListBuilder<Builder, BrewingRecipeModifier<?, ?>> modifiers = new ListBuilder<>(this);

        private Builder(Potion result) {
            this.result = result;
        }

        public Builder inputBottle(PotionBottle bottle) {
            this.inputBottle = bottle;
            return this;
        }

        public Builder resultBottle(PotionBottle bottle) {
            this.resultBottle = bottle;
            return this;
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
            if (heat == null || base == null || inputBottle == null || resultBottle == null || energy == null || result == null) {
                throw new NotAllFieldsFilledException();
            }
            return new BrewingRecipe(heat, base, result, steps.build(), modifiers.build(), inputBottle, resultBottle, energy.build());
        }
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        Map<String, Object> json = Map.of(
                "heat", heat.name(),
                "base", Text.translatable(base.finishTranslationKey(Items.POTION.getTranslationKey() + ".effect.")).getString(),
                "result", Text.translatable(result.finishTranslationKey(Items.POTION.getTranslationKey() + ".effect.")).getString(),
                "steps", steps.stream().map(this::stepToJson).collect(Collectors.toList()),
                "modifiers", modifiers.stream().map(this::modifierToJson).collect(Collectors.toList())
        );

        return gson.toJson(json);
    }

    private Object stepToJson(BrewingRecipeStep<?> step) {
        if (step instanceof BrewingRecipeSteps.BrewingRecipeStirStep stirStep) {
            return Map.of(
                    "type", "stir",
                    "directions", stirStep.stirDirections.stream().map(Enum::name).toList()
            );
        } else if (step instanceof BrewingRecipeSteps.BrewingRecipeItemStep itemStep) {
            return Map.of(
                    "type", "ingredient",
                    "ingredient", itemStep.ingredient.toJson()
            );
        }
        return Map.of("type", step.getClass().getSimpleName());
    }

    private Object modifierToJson(BrewingRecipeModifier<?, ?> modifier) {
        return Map.of(
                "type", modifier.getClass().getSimpleName(),
                "data", modifier.data.toString(),
                "length", modifier.duration,
                "level", modifier.level,
                "required", modifier.required
        );
    }
}
