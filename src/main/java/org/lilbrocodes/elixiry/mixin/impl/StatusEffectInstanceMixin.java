package org.lilbrocodes.elixiry.mixin.impl;

import net.minecraft.entity.effect.StatusEffectInstance;
import org.lilbrocodes.elixiry.mixin.accessor.StatusEffectModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StatusEffectInstance.class)
public class StatusEffectInstanceMixin implements StatusEffectModifier {
    @Shadow private int amplifier;
    @Shadow private int duration;

    @Override
    public void elixiry$addDuration(int duration) {
        this.duration += duration;
    }

    @Override
    public void elixiry$addAmplifier(int amplifier) {
        this.amplifier += amplifier;
    }
}
