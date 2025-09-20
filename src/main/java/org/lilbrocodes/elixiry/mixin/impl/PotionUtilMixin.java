package org.lilbrocodes.elixiry.mixin.impl;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotionUtil.class)
public class PotionUtilMixin {
    @ModifyExpressionValue(method = "getPotionEffects(Lnet/minecraft/nbt/NbtCompound;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/PotionUtil;getPotion(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/potion/Potion;"))
    private static Potion elixiry$removeDefaultEffects(Potion original, @Local(argsOnly = true) NbtCompound compound) {
        if (compound.getBoolean("ElixiryPotion")) return Potions.EMPTY;
        return original;
    }
}
