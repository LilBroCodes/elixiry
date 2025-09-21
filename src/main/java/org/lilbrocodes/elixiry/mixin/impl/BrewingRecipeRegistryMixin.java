package org.lilbrocodes.elixiry.mixin.impl;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.util.BrewingRecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
    @Inject(method = "registerPotionRecipe", at = @At("HEAD"), cancellable = true)
    private static void elixiry$removePotionRecipes(Potion input, Item item, Potion output, CallbackInfo ci) {
        ci.cancel();
        if (Elixiry.DEBUG) BrewingRecipeManager.getInstance().addVanillaRecipe(input, output, item);
    }
}
