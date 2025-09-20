package org.lilbrocodes.elixiry.recipe.brewing.step;

public abstract class BrewingRecipeStep<T extends BrewingRecipeStep<?>> {
    public final int time;

    public BrewingRecipeStep(int time) {
        this.time = time;
    }

    public abstract Builder<T> builder();

    public abstract static class Builder<T extends BrewingRecipeStep<?>> {
        protected int time = -1;

        public Builder<T> time(int time) {
            this.time = time;
            return this;
        }

        Builder() {

        }

        public abstract T build();
    }
}
