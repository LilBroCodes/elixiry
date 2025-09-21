package org.lilbrocodes.elixiry.common.data.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.recipe.dumping.DumpingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ElixiryDumpingRecipeProvider implements DataProvider {
    List<CompletableFuture<?>> futures = new ArrayList<>();
    private final FabricDataOutput output;

    public ElixiryDumpingRecipeProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        addRecipe(
                writer,
                Elixiry.identify("tipping"),
                DumpingRecipe.builder()
                        .input(Potions.EMPTY)
                        .input(Ingredient.ofItems(Items.ARROW))
                        .output(new ItemStack(Items.TIPPED_ARROW))
                        .uses(2)
                        .wild(true)
                        .build()
        );

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Elixiry dumping recipes";
    }

    public void addRecipe(DataWriter writer, Identifier identifier, DumpingRecipe recipe) {
        futures.add(recipe.save(writer, output.getPath(), identifier));
    }
}
