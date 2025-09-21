package org.lilbrocodes.elixiry.common.data.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import org.lilbrocodes.elixiry.common.registry.ModBlocks;

import java.util.function.Consumer;

public class ElixiryRecipeProvider extends FabricRecipeProvider {
    public ElixiryRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.WITCH_CAULDRON.item)
                .pattern("ISI")
                .pattern("IGI")
                .pattern("III")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GLOWSTONE_DUST)
                .input('S', Items.STICK)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(consumer);

    }
}
