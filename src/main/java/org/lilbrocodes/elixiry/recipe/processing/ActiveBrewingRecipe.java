package org.lilbrocodes.elixiry.recipe.processing;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lilbrocodes.elixiry.block.entity.WitchCauldronBlockEntity;
import org.lilbrocodes.elixiry.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.recipe.brewing.step.BrewingRecipeStep;
import org.lilbrocodes.elixiry.recipe.brewing.step.BrewingRecipeSteps.*;
import org.lilbrocodes.elixiry.util.BrewingRecipeManager;
import org.lilbrocodes.elixiry.util.PotionModifier;

public class ActiveBrewingRecipe {
    private final BrewingRecipe recipe;
    private int ticksOnStep = 0;
    private int stirsOnStep = 0;
    private int step = 0;
    private boolean done = false;

    public ActiveBrewingRecipe(BrewingRecipe recipe) {
        this.recipe = recipe;
    }

    public static ActiveBrewingRecipe start(WitchCauldronBlockEntity be) {
        BrewingRecipe possibleRecipe = BrewingRecipeManager.getInstance().getRecipeForConditions(be.potion, be.getHeat());
        if (possibleRecipe != null) {
            return new ActiveBrewingRecipe(possibleRecipe);
        }

        return null;
    }

    public boolean tick(WitchCauldronBlockEntity be) {
        if (done()) return false;

        BrewingRecipeStep<?> step = recipe.steps.get(this.step);
        if (step.time != -1 && ticksOnStep > step.time) return true;
        ticksOnStep++;
        return false;
    }

    public boolean stir(WitchCauldronBlockEntity.StirDirection direction) {
        if (done()) return false;

        BrewingRecipeStep<?> step = recipe.steps.get(this.step);
        if (!(step instanceof BrewingRecipeStirStep stir)) return true;
        WitchCauldronBlockEntity.StirDirection next = stir.stirDirections.get(stirsOnStep);
        if (next == direction) {
            stirsOnStep++;
            if (stir.stirDirections.size() == stirsOnStep) step();
            return false;
        } else {
            return true;
        }
    }

    public boolean addItem(ItemStack stack) {
        if (done()) return false;

        BrewingRecipeStep<?> step = recipe.steps.get(this.step);
        if (!(step instanceof BrewingRecipeItemStep itemStep) || !itemStep.ingredient.test(stack)) return true;
        step();
        return false;
    }

    private void step() {
        ticksOnStep = 0;
        stirsOnStep = 0;
        step++;

        if (recipe.steps.size() <= step) {
            done = true;
        }
    }

    public boolean done() {
        return done;
    }

    public PotionModifier getModifier(World world, BlockPos pos) {
        PotionModifier modifier = new PotionModifier(0, 0);
        recipe.modifiers.forEach(m -> m.apply(modifier, world, pos));
        return modifier;
    }

    public Potion getResult() {
        return recipe.result;
    }

    public static ActiveBrewingRecipe readNbt(NbtCompound tag) {
        Identifier id = Identifier.of(tag.getString("namespace"), tag.getString("path"));
        BrewingRecipe recipe = BrewingRecipeManager.getInstance().get(id);
        return new ActiveBrewingRecipe(recipe);
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        Identifier id = BrewingRecipeManager.getInstance().find(recipe);
        tag.putString("namespace", id.getNamespace());
        tag.putString("path", id.getPath());
        return tag;
    }
}
