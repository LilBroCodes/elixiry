package org.lilbrocodes.elixiry.mixin.accessor;

public interface StatusEffectModifier {
    void elixiry$addDuration(int duration);
    void elixiry$addAmplifier(int amplifier);

    default void modify(int duration, int amplifier) {
        elixiry$addDuration(duration);
        elixiry$addAmplifier(amplifier);
    }
}
