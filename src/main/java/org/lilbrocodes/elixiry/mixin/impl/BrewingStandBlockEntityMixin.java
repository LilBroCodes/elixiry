package org.lilbrocodes.elixiry.mixin.impl;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin {

    @ModifyExpressionValue(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/BrewingRecipeRegistry;craft(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack preservePotionNbt(ItemStack original, @Local ItemStack ingredient, @Local int i, @Local(argsOnly = true) DefaultedList<ItemStack> slots) {
        ItemStack input = slots.get(i);
        ItemStack result = BrewingRecipeRegistry.craft(ingredient, input);

        if (input.hasNbt() && input.getItem() instanceof PotionItem && result.getItem() instanceof PotionItem) {
            NbtCompound inputNbt = input.getNbt().copy();

            String correctPotionId = result.getOrCreateNbt().getString("Potion");
            result.setNbt(inputNbt);
            result.getOrCreateNbt().putString("Potion", correctPotionId);
        }

        return result;
    }
}
