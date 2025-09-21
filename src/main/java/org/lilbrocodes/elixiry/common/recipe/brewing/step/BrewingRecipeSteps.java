package org.lilbrocodes.elixiry.common.recipe.brewing.step;

import com.google.gson.JsonObject;
import net.minecraft.recipe.Ingredient;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity.StirDirection;
import org.lilbrocodes.elixiry.common.util.ListBuilder;

import java.util.List;

public class BrewingRecipeSteps {
    public static BrewingRecipeStirStep.Builder stir() {
        return new BrewingRecipeStirStep.Builder();
    }

    public static BrewingRecipeItemStep.Builder item() {
        return new BrewingRecipeItemStep.Builder();
    }

    public static BrewingRecipeStep<?> fromJson(JsonObject obj) {
        String type = obj.get("type").getAsString();
        return switch (type) {
            case "item" -> BrewingRecipeItemStep.fromJson(obj);
            case "stir" -> BrewingRecipeStirStep.fromJson(obj);
            default -> throw new IllegalArgumentException("Unknown brewing step type: " + type);
        };
    }

    public static class BrewingRecipeItemStep extends BrewingRecipeStep<BrewingRecipeItemStep> {
        public final Ingredient ingredient;

        private BrewingRecipeItemStep(Ingredient stack, int time) {
            super(time);
            this.ingredient = stack;
        }

        @Override
        public BrewingRecipeStep.Builder<BrewingRecipeItemStep> builder() {
            return new Builder();
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", type());
            obj.addProperty("time", time);
            obj.add("ingredient", ingredient.toJson());
            return obj;
        }

        public static BrewingRecipeItemStep fromJson(JsonObject obj) {
            int time = obj.has("time") ? obj.get("time").getAsInt() : -1;
            Ingredient ingredient = Ingredient.fromJson(obj.get("ingredient"));
            return new BrewingRecipeItemStep(ingredient, time);
        }

        @Override
        public String type() {
            return "item";
        }

        public static class Builder extends BrewingRecipeStep.Builder<BrewingRecipeItemStep> {
            private Ingredient ingredient = Ingredient.EMPTY;

            public Builder item(Ingredient ingredient) {
                this.ingredient = ingredient;
                return this;
            }

            @Override
            public BrewingRecipeItemStep build() {
                return new BrewingRecipeItemStep(ingredient, time);
            }
        }
    }


    public static class BrewingRecipeStirStep extends BrewingRecipeStep<BrewingRecipeStirStep> {
        public final List<StirDirection> stirDirections;

        private BrewingRecipeStirStep(List<StirDirection> stirDirections, int time) {
            super(time);
            this.stirDirections = stirDirections;
        }

        @Override
        public BrewingRecipeStep.Builder<BrewingRecipeStirStep> builder() {
            return new Builder();
        }

        @Override
        public JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", type());
            obj.addProperty("time", time);
            var arr = new com.google.gson.JsonArray();
            for (StirDirection dir : stirDirections) {
                arr.add(dir.name().toLowerCase());
            }
            obj.add("directions", arr);
            return obj;
        }

        public static BrewingRecipeStirStep fromJson(JsonObject obj) {
            int time = obj.has("time") ? obj.get("time").getAsInt() : -1;
            List<StirDirection> dirs = new java.util.ArrayList<>();
            obj.getAsJsonArray("directions").forEach(e ->
                    dirs.add(StirDirection.valueOf(e.getAsString().toUpperCase())));
            return new BrewingRecipeStirStep(dirs, time);
        }

        @Override
        public String type() {
            return "stir";
        }

        public static class Builder extends BrewingRecipeStep.Builder<BrewingRecipeStirStep> {
            private final ListBuilder<Builder, StirDirection> stirDirectionBuilder = new ListBuilder<>(this);

            public ListBuilder<Builder, StirDirection> stirDirections() {
                return stirDirectionBuilder;
            }

            @Override
            public BrewingRecipeStirStep build() {
                return new BrewingRecipeStirStep(stirDirectionBuilder.build(), time);
            }
        }
    }
}
