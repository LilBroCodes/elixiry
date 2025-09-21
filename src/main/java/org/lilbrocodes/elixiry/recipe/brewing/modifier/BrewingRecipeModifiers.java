package org.lilbrocodes.elixiry.recipe.brewing.modifier;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.lilbrocodes.elixiry.util.IntClamper;

import java.util.Objects;

public class BrewingRecipeModifiers {
    public static BrewingRecipeModifier.Builder<RegistryKey<Biome>, BiomeModifier> biome(RegistryKey<Biome> biome) {
        return BrewingRecipeModifier.builder(biome,
                b -> new BiomeModifier(b.data(), b.length(), b.level(), b.required()));
    }

    public static BrewingRecipeModifier.Builder<WeatherModifier.WeatherData, WeatherModifier> weather(WeatherModifier.WeatherData weather) {
        return BrewingRecipeModifier.builder(weather,
                b -> new WeatherModifier(b.data(), b.length(), b.level(), b.required()));
    }

    public static BrewingRecipeModifier.Builder<TimeModifier.TimeData, TimeModifier> time(TimeModifier.TimeData time) {
        return BrewingRecipeModifier.builder(time,
                b -> new TimeModifier(b.data(), b.length(), b.level(), b.required()));
    }

    public static BrewingRecipeModifier.Builder<IntClamper.ValidatedInt<MoonPhaseModifier>, MoonPhaseModifier> moonPhase(IntClamper.ValidatedInt<MoonPhaseModifier> phase) {
        return BrewingRecipeModifier.builder(phase,
                b -> new MoonPhaseModifier(b.data(), b.length(), b.level(), b.required()));
    }

    public static class BiomeModifier extends BrewingRecipeModifier<RegistryKey<Biome>, BiomeModifier> {
        private BiomeModifier(RegistryKey<Biome> biome, int length, int level, boolean required) {
            super(biome, length, level, required);
        }

        @Override
        public boolean shouldApply(World world, BlockPos pos) {
            RegistryKey<Biome> currentBiome = world.getBiome(pos).getKey().orElse(null);
            return currentBiome != null && Objects.equals(currentBiome, data);
        }
    }

    public static class WeatherModifier extends BrewingRecipeModifier<WeatherModifier.WeatherData, WeatherModifier> {
        private WeatherModifier(WeatherData weather, int length, int level, boolean required) {
            super(weather, length, level, required);
        }

        @Override
        public boolean shouldApply(World world, BlockPos pos) {
            boolean bl1 = !data.rain || world.hasRain(pos.up());
            boolean bl2 = !data.thunder || world.isThundering();
            return bl1 && bl2;
        }

        public record WeatherData(boolean rain, boolean thunder) {}
    }

    public static class TimeModifier extends BrewingRecipeModifier<TimeModifier.TimeData, TimeModifier> {
        private TimeModifier(TimeData time, int length, int level, boolean required) {
            super(time, length, level, required);
        }

        @Override
        public boolean shouldApply(World world, BlockPos pos) {
            return world.getTimeOfDay() > data.minTimeOfDay && world.getTimeOfDay() < data.maxTimeOfDay;
        }

        public record TimeData(int minTimeOfDay, int maxTimeOfDay) {}
    }

    public static class MoonPhaseModifier extends BrewingRecipeModifier<IntClamper.ValidatedInt<MoonPhaseModifier>, MoonPhaseModifier> {
        protected MoonPhaseModifier(IntClamper.ValidatedInt<MoonPhaseModifier> data, int length, int level, boolean required) {
            super(data, length, level, required);
        }

        @Override
        public boolean shouldApply(World world, BlockPos pos) {
            return world.getMoonPhase() == data.build();
        }
    }
}
