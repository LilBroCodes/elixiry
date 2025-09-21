package org.lilbrocodes.elixiry.recipe.processing;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.lilbrocodes.elixiry.block.entity.WitchCauldronBlockEntity;
import org.lilbrocodes.elixiry.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.util.BrewingRecipeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ActiveBrewingSession {
    private final List<BrewingRecipeProgress> candidates = new ArrayList<>();
    private boolean done = false;

    public ActiveBrewingSession(List<BrewingRecipe> possibleRecipes) {
        for (BrewingRecipe r : possibleRecipes) {
            candidates.add(new BrewingRecipeProgress(r));
        }
    }

    public static ActiveBrewingSession start(WitchCauldronBlockEntity be) {
        List<BrewingRecipe> possible =
                BrewingRecipeManager.getInstance().getRecipesForConditions(be.potion, be.getHeat(), be.modifier.bottle);
        if (!possible.isEmpty()) {
            return new ActiveBrewingSession(possible);
        }
        return null;
    }

    public boolean tick(WitchCauldronBlockEntity be) {
        if (done) return false;

        filter(progress -> progress.tick(be));
        return candidates.isEmpty();
    }

    public boolean stir(WitchCauldronBlockEntity.StirDirection direction) {
        if (done) return false;

        filter(progress -> progress.stir(direction));
        return candidates.isEmpty();
    }

    public boolean addItem(ItemStack stack) {
        if (done) return false;

        filter(progress -> progress.addItem(stack));
        return candidates.isEmpty();
    }

    private void filter(Function<BrewingRecipeProgress, Boolean> action) {
        candidates.removeIf(action::apply);
        if (candidates.size() == 1 && candidates.get(0).done()) {
            done = true;
        }
    }

    public boolean done() {
        return done;
    }

    public BrewingRecipeProgress getWinningRecipe() {
        return done && candidates.size() == 1 ? candidates.get(0) : null;
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putBoolean("done", done);
        NbtList list = new NbtList();
        for (BrewingRecipeProgress progress : candidates) {
            list.add(progress.writeNbt(new NbtCompound()));
        }
        tag.put("candidates", list);
        return tag;
    }

    public static ActiveBrewingSession readNbt(NbtCompound tag) {
        NbtList list = tag.getList("candidates", NbtElement.COMPOUND_TYPE);
        List<BrewingRecipeProgress> candidates = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            candidates.add(BrewingRecipeProgress.readNbt(list.getCompound(i)));
        }
        ActiveBrewingSession session = new ActiveBrewingSession(new ArrayList<>());
        session.candidates.addAll(candidates);
        session.done = tag.getBoolean("done");
        return session;
    }

}
