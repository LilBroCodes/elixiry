package org.lilbrocodes.elixiry.common.recipe.brewing.modifier;

import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lilbrocodes.elixiry.common.util.PotionModifier;

import java.util.function.Function;

public abstract class BrewingRecipeModifier<D, T extends BrewingRecipeModifier<D, T>> {
    public final boolean required;
    public final int duration;
    public final int level;
    public final D data;

    protected BrewingRecipeModifier(D data, int duration, int level, boolean required) {
        this.data = data;
        this.duration = duration;
        this.level = level;
        this.required = required;
    }

    public PotionModifier apply(PotionModifier modifier, World world, BlockPos pos) {
        if (shouldApply(world, pos)) {
            modifier.modify(level, duration);
        }
        return modifier;
    }

    public abstract boolean shouldApply(World world, BlockPos pos);

    public static <D, T extends BrewingRecipeModifier<D, T>> Builder<D, T> builder(
            D data, Function<Builder<D, T>, T> factory) {
        return new Builder<>(data, factory);
    }
    public abstract JsonObject toJson();
    public abstract String type();

    public static class Builder<D, T extends BrewingRecipeModifier<D, T>> {
        private final D data;
        private final Function<Builder<D, T>, T> factory;
        private int length = 0;
        private int level = 0;
        private boolean required = false;

        private Builder(D data, Function<Builder<D, T>, T> factory) {
            this.data = data;
            this.factory = factory;
        }

        public Builder<D, T> length(int length) {
            this.length = length;
            return this;
        }

        public Builder<D, T> level(int level) {
            this.level = level;
            return this;
        }

        public Builder<D, T> required(boolean required) {
            this.required = required;
            return this;
        }

        public T build() {
            return factory.apply(this);
        }

        public D data() { return data; }
        public int length() { return length; }
        public int level() { return level; }
        public boolean required() { return required; }
    }
}
