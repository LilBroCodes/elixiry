package org.lilbrocodes.elixiry.util;

import net.minecraft.util.math.MathHelper;
import org.lilbrocodes.elixiry.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.recipe.brewing.modifier.BrewingRecipeModifiers;

public class IntClamper<V> {
    public static final IntClamper<BrewingRecipeModifiers.MoonPhaseModifier> MOON_PHASES = new IntClamper<>(0, 7);
    public static final IntClamper<BrewingRecipe.Builder> ENERGY = new IntClamper<>(0, 32);

    private final Integer min;
    private final Integer max;

    public IntClamper(Integer min, Integer max) {
        this.min = min;
        this.max = min;
    }

    public ValidatedInt<V> get(Integer value) {
        return new ValidatedInt<>(MathHelper.clamp(value, min, max));
    }

    public record ValidatedInt<V>(Integer build) {}
}
