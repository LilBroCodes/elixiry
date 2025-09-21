package org.lilbrocodes.elixiry.common.recipe.brewing.step;

import com.google.gson.JsonObject;

public abstract class BrewingRecipeStep<T extends BrewingRecipeStep<?>> {
    public final int time;

    public BrewingRecipeStep(int time) {
        this.time = time;
    }

    public abstract Builder<T> builder();

    public abstract JsonObject toJson();
    public abstract String type();

    public abstract static class Builder<T extends BrewingRecipeStep<?>> {
        protected int time = -1;

        public Builder<T> time(int time) {
            this.time = time;
            return this;
        }

        public abstract T build();
    }
}
