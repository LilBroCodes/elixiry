package org.lilbrocodes.elixiry.common.recipe.processing;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity;
import org.lilbrocodes.elixiry.common.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.common.recipe.brewing.step.BrewingRecipeStep;
import org.lilbrocodes.elixiry.common.recipe.brewing.step.BrewingRecipeSteps.*;
import org.lilbrocodes.elixiry.common.recipe.management.BrewingRecipeManager;
import org.lilbrocodes.elixiry.common.util.PotionModifier;

public class BrewingRecipeProgress {
    private final BrewingRecipe recipe;
    private int ticksOnStep = 0;
    private int stirsOnStep = 0;
    private int step = 0;
    private boolean done = false;

    public BrewingRecipeProgress(BrewingRecipe recipe) {
        this.recipe = recipe;
    }

    public boolean tick(WitchCauldronBlockEntity be) {
        if (done) return false;
        if (recipe == null) return true;

        BrewingRecipeStep<?> stepObj = recipe.steps.get(step);
        if (stepObj.time != -1 && ticksOnStep > stepObj.time) return true;

        ticksOnStep++;
        return false;
    }

    public boolean stir(WitchCauldronBlockEntity.StirDirection direction) {
        if (done) return false;
        BrewingRecipeStep<?> stepObj = recipe.steps.get(step);
        if (!(stepObj instanceof BrewingRecipeStirStep stirStep)) return true;
        if (stirStep.stirDirections.get(stirsOnStep) == direction) {
            stirsOnStep++;
            if (stirsOnStep == stirStep.stirDirections.size()) advance();
            return false;
        }
        return true; // fail
    }

    public boolean addItem(ItemStack stack) {
        if (done) return false;
        BrewingRecipeStep<?> stepObj = recipe.steps.get(step);
        if (!(stepObj instanceof BrewingRecipeItemStep itemStep) || !itemStep.ingredient.test(stack)) return true;
        advance();
        return false;
    }

    private void advance() {
        ticksOnStep = 0;
        stirsOnStep = 0;
        step++;
        if (step >= recipe.steps.size()) done = true;
    }

    public boolean done() {
        return done;
    }

    public Potion getResult() {
        return recipe.result;
    }

    public PotionModifier getModifier(World world, BlockPos pos) {
        PotionModifier modifier = new PotionModifier(0, 0);
        recipe.modifiers.forEach(m -> m.apply(modifier, world, pos));
        return modifier;
    }

    public BrewingRecipe getRecipe() {
        return recipe;
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        Identifier id = BrewingRecipeManager.getInstance().find(recipe);
        tag.putString("namespace", id.getNamespace());
        tag.putString("path", id.getPath());
        tag.putInt("step", step);
        tag.putInt("ticksOnStep", ticksOnStep);
        tag.putInt("stirsOnStep", stirsOnStep);
        tag.putBoolean("done", done);
        return tag;
    }

    public static BrewingRecipeProgress readNbt(NbtCompound tag) {
        Identifier id = new Identifier(tag.getString("namespace"), tag.getString("path"));
        BrewingRecipe recipe = BrewingRecipeManager.getInstance().get(id);
        BrewingRecipeProgress progress = new BrewingRecipeProgress(recipe);
        progress.step = tag.getInt("step");
        progress.ticksOnStep = tag.getInt("ticksOnStep");
        progress.stirsOnStep = tag.getInt("stirsOnStep");
        progress.done = tag.getBoolean("done");
        return progress;
    }
}
