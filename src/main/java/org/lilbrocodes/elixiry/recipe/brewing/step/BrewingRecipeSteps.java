package org.lilbrocodes.elixiry.recipe.brewing.step;

import net.minecraft.recipe.Ingredient;
import org.lilbrocodes.elixiry.block.entity.WitchCauldronBlockEntity.StirDirection;
import org.lilbrocodes.elixiry.util.ListBuilder;

import java.util.List;

public class BrewingRecipeSteps {
    public static BrewingRecipeStirStep.Builder stir() {
        return new BrewingRecipeStirStep.Builder();
    }

    public static BrewingRecipeItemStep.Builder item() {
        return new BrewingRecipeItemStep.Builder();
    }

    public static class BrewingRecipeItemStep extends BrewingRecipeStep<BrewingRecipeItemStep> {
        public final Ingredient ingredient;

        private BrewingRecipeItemStep(Ingredient stack, int time) {
            super(time);
            this.ingredient = stack;
        }

        @Override
        public BrewingRecipeStep.Builder<BrewingRecipeItemStep> builder() {
            return new Builder();
        }

        public static class Builder extends BrewingRecipeStep.Builder<BrewingRecipeItemStep> {
            private Ingredient ingredient = Ingredient.EMPTY;

            private Builder() {

            }

            public Builder item(Ingredient ingredient) {
                this.ingredient = ingredient;
                return this;
            }

            @Override
            public BrewingRecipeItemStep build() {
                return new BrewingRecipeItemStep(ingredient, time);
            }
        }
    }

    public static class BrewingRecipeStirStep extends BrewingRecipeStep<BrewingRecipeStirStep> {
        public final List<StirDirection> stirDirections;

        private BrewingRecipeStirStep(List<StirDirection> stirDirections, int time) {
            super(time);
            this.stirDirections = stirDirections;
        }

        @Override
        public BrewingRecipeStep.Builder<BrewingRecipeStirStep> builder() {
            return new Builder();
        }

        public static class Builder extends BrewingRecipeStep.Builder<BrewingRecipeStirStep> {
            private final ListBuilder<Builder, StirDirection> stirDirectionBuilder = new ListBuilder<>(this);

            public ListBuilder<Builder, StirDirection> stirDirections() {
                return stirDirectionBuilder;
            }

            @Override
            public BrewingRecipeStirStep build() {
                return new BrewingRecipeStirStep(stirDirectionBuilder.build(), time);
            }
        }
    }
}
