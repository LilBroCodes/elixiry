package org.lilbrocodes.elixiry.registry;

import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.block.WitchCauldron;
import org.lilbrocodes.elixiry.block.entity.WitchCauldronBlockEntity;
import org.lilbrocodes.elixiry.recipe.brewing.BrewingRecipe;
import org.lilbrocodes.elixiry.recipe.brewing.modifier.BrewingRecipeModifiers;
import org.lilbrocodes.elixiry.recipe.brewing.modifier.BrewingRecipeModifiers.TimeModifier.TimeData;
import org.lilbrocodes.elixiry.recipe.brewing.modifier.BrewingRecipeModifiers.WeatherModifier.WeatherData;
import org.lilbrocodes.elixiry.recipe.brewing.step.BrewingRecipeSteps;
import org.lilbrocodes.elixiry.util.BrewingRecipeManager;

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
