package org.lilbrocodes.elixiry.common.data.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.block.WitchCauldron;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity;
import org.lilbrocodes.elixiry.common.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.common.recipe.brewing.modifier.BrewingRecipeModifiers;
import org.lilbrocodes.elixiry.common.recipe.brewing.step.BrewingRecipeSteps;
import org.lilbrocodes.elixiry.common.util.IntClamper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ElixiryBrewingRecipeProvider implements DataProvider {
    List<CompletableFuture<?>> futures = new ArrayList<>();
    public static final Random random = new Random();
    private final FabricDataOutput output;

    public final WitchCauldronBlockEntity.StirDirection[] MUNDANE_STIRS = stirs(2);
    public final WitchCauldronBlockEntity.StirDirection[] AWKWARD_STIRS = stirs(2);
    public final WitchCauldronBlockEntity.StirDirection[] THICK_STIRS = stirs(2);
    public final WitchCauldronBlockEntity.StirDirection[] LONG_STIRS = stirs(3);
    public final WitchCauldronBlockEntity.StirDirection[] STRONG_STIRS = stirs(3);

    public ElixiryBrewingRecipeProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        addRecipe(
                writer,
                Elixiry.identify("night_vision"),
                BrewingRecipe.builder(Potions.NIGHT_VISION)
                        .heat(WitchCauldron.HeatState.HIGH)
                        .base(Potions.AWKWARD)
                        .energy(IntClamper.ENERGY.get(10))
                        .modifiers()
                        .push(BrewingRecipeModifiers.time(new BrewingRecipeModifiers.TimeModifier.TimeData(13000, 22999))
                                .length(20 * 30)
                                .build())
                        .end()
                        .steps()
                        .push(BrewingRecipeSteps.item()
                                .item(Ingredient.ofItems(Items.GOLDEN_CARROT))
                                .build())
                        .push(BrewingRecipeSteps.stir()
                                .stirDirections()
                                .push(stirs(3))
                                .end()
                                .time(60)
                                .build())
                        .end()
                        .build()
        );

        addRecipe(
                writer,
                Elixiry.identify("strength"),
                BrewingRecipe.builder(Potions.STRENGTH)
                        .heat(WitchCauldron.HeatState.HIGH)
                        .base(Potions.AWKWARD)
                        .energy(IntClamper.ENERGY.get(15))
                        .modifiers()
                        .push(BrewingRecipeModifiers.weather(new BrewingRecipeModifiers.WeatherModifier.WeatherData(true, true))
                                .length(20 * 30)
                                .level(1)
                                .build())
                        .end()
                        .steps()
                        .push(BrewingRecipeSteps.item()
                                .item(Ingredient.ofItems(Items.BLAZE_POWDER))
                                .build())
                        .push(BrewingRecipeSteps.stir()
                                .stirDirections()
                                .push(stirs(3))
                                .end()
                                .time(60)
                                .build())
                        .end()
                        .build()
        );

        addRecipe(
                writer,
                Elixiry.identify("swiftness"),
                BrewingRecipe.builder(Potions.SWIFTNESS)
                        .heat(WitchCauldron.HeatState.HIGH)
                        .base(Potions.AWKWARD)
                        .energy(IntClamper.ENERGY.get(12))
                        .modifiers()
                        .push(BrewingRecipeModifiers.biome(BiomeKeys.DESERT)
                                .length(20 * 60)
                                .level(1)
                                .build())
                        .end()
                        .steps()
                        .push(BrewingRecipeSteps.item()
                                .item(Ingredient.ofItems(Items.SUGAR))
                                .build())
                        .push(BrewingRecipeSteps.stir()
                                .stirDirections()
                                .push(stirs(3))
                                .end()
                                .time(60)
                                .build())
                        .end()
                        .build()
        );

        addRecipe(
                writer,
                Elixiry.identify("regeneration"),
                BrewingRecipe.builder(Potions.REGENERATION)
                        .heat(WitchCauldron.HeatState.HIGH)
                        .base(Potions.AWKWARD)
                        .energy(IntClamper.ENERGY.get(18))
                        .modifiers()
                        .push(BrewingRecipeModifiers.moonPhase(IntClamper.MOON_PHASES.get(4))
                                .length(20 * 45)
                                .level(1)
                                .build())
                        .end()
                        .steps()
                        .push(BrewingRecipeSteps.item()
                                .item(Ingredient.ofItems(Items.GHAST_TEAR))
                                .build())
                        .push(BrewingRecipeSteps.stir()
                                .stirDirections()
                                .push(stirs(4))
                                .end()
                                .time(80)
                                .build())
                        .end()
                        .build()
        );

        addBasicRecipes(writer);

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    /**
     * Method for adding potion recipes that result in stuff like awkward, mundane or thick
     * @param writer Required for saving the recipes
     */
    public void addBasicRecipes(DataWriter writer) {
        addAwkwardRecipe(writer, Items.NETHER_WART, "nether_wart");

        addMundaneRecipe(writer, Items.GLISTERING_MELON_SLICE, "glistering_melon_slice");
        addMundaneRecipe(writer, Items.GHAST_TEAR, "ghast_tear");
        addMundaneRecipe(writer, Items.RABBIT_FOOT, "rabbit_foot");
        addMundaneRecipe(writer, Items.BLAZE_POWDER, "blaze_powder");
        addMundaneRecipe(writer, Items.SPIDER_EYE, "spider_eye");
        addMundaneRecipe(writer, Items.SUGAR, "sugar");
        addMundaneRecipe(writer, Items.MAGMA_CREAM, "magma_cream");
        addMundaneRecipe(writer, Items.REDSTONE, "redstone");

        addThickRecipe(writer, Items.GLOWSTONE_DUST, "glowstone");
    }

    @Override
    public String getName() {
        return "Elixiry brewing recipes";
    }

    public static WitchCauldronBlockEntity.StirDirection[] stirs(int count) {
        List<WitchCauldronBlockEntity.StirDirection> stirs = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            stirs.add(random.nextBoolean()
                    ? WitchCauldronBlockEntity.StirDirection.LEFT
                    : WitchCauldronBlockEntity.StirDirection.RIGHT);
        }

        Collections.shuffle(stirs, random);

        return stirs.toArray(new WitchCauldronBlockEntity.StirDirection[0]);
    }

    public void addRecipe(DataWriter writer, Identifier identifier, BrewingRecipe recipe) {
        futures.add(recipe.save(writer, output.getPath(), identifier));
    }

    public void addBasicRecipe(DataWriter writer, WitchCauldronBlockEntity.StirDirection[] stirs, Potion potion, String potionName, Item item, String itemName) {
        addRecipe(
                writer,
                Elixiry.identify(String.format("%s_potion_%s", potionName, itemName)),
                BrewingRecipe.builder(potion)
                        .heat(WitchCauldron.HeatState.HIGH)
                        .base(Potions.WATER)
                        .energy(IntClamper.ENERGY.get(0))
                        .steps()
                        .push(BrewingRecipeSteps.item()
                                .item(Ingredient.ofItems(item))
                                .build())
                        .push(BrewingRecipeSteps.stir()
                                .stirDirections()
                                .push(stirs)
                                .end()
                                .time(60)
                                .build())
                        .end()
                        .build()
        );
    }

    public void addMundaneRecipe(DataWriter writer, Item item, String name) {
        addBasicRecipe(writer, MUNDANE_STIRS, Potions.MUNDANE, "mundane", item, name);
    }

    public void addAwkwardRecipe(DataWriter writer, Item item, String name) {
        addBasicRecipe(writer, AWKWARD_STIRS, Potions.AWKWARD, "awkward", item, name);
    }

    public void addThickRecipe(DataWriter writer, Item item, String name) {
        addBasicRecipe(writer, THICK_STIRS, Potions.THICK, "thick", item, name);
    }
}
