package org.lilbrocodes.elixiry.registry;

import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.biome.BiomeKeys;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.block.WitchCauldron;
import org.lilbrocodes.elixiry.block.entity.WitchCauldronBlockEntity;
import org.lilbrocodes.elixiry.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.recipe.brewing.modifier.BrewingRecipeModifiers;
import org.lilbrocodes.elixiry.recipe.brewing.modifier.BrewingRecipeModifiers.TimeModifier.TimeData;
import org.lilbrocodes.elixiry.recipe.brewing.modifier.BrewingRecipeModifiers.WeatherModifier.WeatherData;
import org.lilbrocodes.elixiry.recipe.brewing.step.BrewingRecipeSteps;
import org.lilbrocodes.elixiry.util.BrewingRecipeManager;
import org.lilbrocodes.elixiry.util.IntClamper;
import org.lilbrocodes.elixiry.util.PotionBottle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ModBrewingRecipes {
    public static final Random random = new Random(0);

    public static void initialize() {
        BrewingRecipeManager recipes = BrewingRecipeManager.getInstance();

        recipes.register(
                Elixiry.identify("awkward_potion"),
                BrewingRecipe.builder(Potions.AWKWARD)
                        .heat(WitchCauldron.HeatState.HIGH)
                        .base(Potions.WATER)
                        .energy(IntClamper.ENERGY.get(0))
                        .steps()
                            .push(BrewingRecipeSteps.item()
                                .item(Ingredient.ofItems(Items.NETHER_WART))
                                .build())
                            .push(BrewingRecipeSteps.stir()
                                .stirDirections()
                                .push(stirs(2))
                                .end()
                                .time(60)
                                .build())
                            .end()
                        .build()
        );

        recipes.register(
                Elixiry.identify("night_vision"),
                BrewingRecipe.builder(Potions.NIGHT_VISION)
                        .heat(WitchCauldron.HeatState.HIGH)
                        .base(Potions.AWKWARD)
                        .energy(IntClamper.ENERGY.get(10))
                        .modifiers()
                            .push(BrewingRecipeModifiers.time(new TimeData(13000, 22999))
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

        recipes.register(
                Elixiry.identify("strength"),
                BrewingRecipe.builder(Potions.STRENGTH)
                        .heat(WitchCauldron.HeatState.HIGH)
                        .base(Potions.AWKWARD)
                        .energy(IntClamper.ENERGY.get(15))
                        .modifiers()
                        .push(BrewingRecipeModifiers.weather(new WeatherData(true, true))
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

        recipes.register(
                Elixiry.identify("splash_strength"),
                BrewingRecipe.builder(Potions.STRENGTH)
                        .heat(WitchCauldron.HeatState.LOW)
                        .base(Potions.STRENGTH)
                        .energy(IntClamper.ENERGY.get(5))
                        .steps()
                        .push(BrewingRecipeSteps.item()
                                .item(Ingredient.ofItems(Items.GUNPOWDER))
                                .build())
                        .push(BrewingRecipeSteps.stir()
                                .stirDirections()
                                .push(stirs(1))
                                .end()
                                .time(60)
                                .build())
                        .end()
                        .inputBottle(PotionBottle.NORMAL)
                        .resultBottle(PotionBottle.SPLASH)
                        .build()
        );

        recipes.register(
                Elixiry.identify("lingering_strength"),
                BrewingRecipe.builder(Potions.STRENGTH)
                        .heat(WitchCauldron.HeatState.LOW)
                        .base(Potions.STRENGTH)
                        .energy(IntClamper.ENERGY.get(7))
                        .steps()
                        .push(BrewingRecipeSteps.item()
                                .item(Ingredient.ofItems(Items.DRAGON_BREATH))
                                .build())
                        .push(BrewingRecipeSteps.stir()
                                .stirDirections()
                                .push(stirs(1))
                                .end()
                                .time(60)
                                .build())
                        .end()
                        .inputBottle(PotionBottle.SPLASH)
                        .resultBottle(PotionBottle.LINGERING)
                        .build()
        );

        recipes.register(
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
                                .push(stirs(2))
                                .end()
                                .time(60)
                                .build())
                        .end()
                        .build()
        );

        recipes.register(
                Elixiry.identify("regeneration_moon"),
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
}
