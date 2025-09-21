package org.lilbrocodes.elixiry.common.recipe.brewing.modifier;

import com.google.gson.JsonObject;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.lilbrocodes.elixiry.common.util.IntClamper;

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

    public static BrewingRecipeModifier<?, ?> fromJson(JsonObject obj) {
        String type = obj.get("type").getAsString();
        return switch (type) {
            case "biome" -> BiomeModifier.fromJson(obj);
            case "weather" -> WeatherModifier.fromJson(obj);
            case "time" -> TimeModifier.fromJson(obj);
            case "moon_phase" -> MoonPhaseModifier.fromJson(obj);
            default -> throw new IllegalArgumentException("Unknown brewing modifier type: " + type);
        };
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

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", type());
            obj.addProperty("length", duration);
            obj.addProperty("level", level);
            obj.addProperty("required", required);
            obj.addProperty("biome", data.getValue().toString()); // registry ID
            return obj;
        }

        public static BiomeModifier fromJson(JsonObject obj) {
            int length = obj.get("length").getAsInt();
            int level = obj.get("level").getAsInt();
            boolean required = obj.get("required").getAsBoolean();
            RegistryKey<Biome> biome = RegistryKey.of(RegistryKeys.BIOME,
                    new Identifier(obj.get("biome").getAsString()));
            return new BiomeModifier(biome, length, level, required);
        }

        @Override
        public String type() {
            return "biome";
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

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", type());
            obj.addProperty("length", duration);
            obj.addProperty("level", level);
            obj.addProperty("required", required);
            obj.addProperty("rain", data.rain());
            obj.addProperty("thunder", data.thunder());
            return obj;
        }

        public static WeatherModifier fromJson(JsonObject obj) {
            int length = obj.get("length").getAsInt();
            int level = obj.get("level").getAsInt();
            boolean required = obj.get("required").getAsBoolean();
            boolean rain = obj.get("rain").getAsBoolean();
            boolean thunder = obj.get("thunder").getAsBoolean();
            return new WeatherModifier(new WeatherData(rain, thunder), length, level, required);
        }

        @Override
        public String type() {
            return "weather";
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

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", type());
            obj.addProperty("length", duration);
            obj.addProperty("level", level);
            obj.addProperty("required", required);
            obj.addProperty("minTime", data.minTimeOfDay());
            obj.addProperty("maxTime", data.maxTimeOfDay());
            return obj;
        }

        public static TimeModifier fromJson(JsonObject obj) {
            int length = obj.get("length").getAsInt();
            int level = obj.get("level").getAsInt();
            boolean required = obj.get("required").getAsBoolean();
            int min = obj.get("minTime").getAsInt();
            int max = obj.get("maxTime").getAsInt();
            return new TimeModifier(new TimeData(min, max), length, level, required);
        }

        @Override
        public String type() {
            return "time";
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

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", type());
            obj.addProperty("length", duration);
            obj.addProperty("level", level);
            obj.addProperty("required", required);
            obj.addProperty("phase", data.build());
            return obj;
        }

        public static MoonPhaseModifier fromJson(JsonObject obj) {
            int length = obj.get("length").getAsInt();
            int level = obj.get("level").getAsInt();
            boolean required = obj.get("required").getAsBoolean();
            int phase = obj.get("phase").getAsInt();
            IntClamper.ValidatedInt<MoonPhaseModifier> validated = IntClamper.MOON_PHASES.get(phase);
            return new MoonPhaseModifier(validated, length, level, required);
        }

        @Override
        public String type() {
            return "moon_phase";
        }
    }
}
