package org.lilbrocodes.elixiry.common.recipe.dumping;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import org.lilbrocodes.elixiry.common.recipe.common.SerializableRecipe;
import org.lilbrocodes.elixiry.common.util.NotAllFieldsFilledException;

@SuppressWarnings("ClassCanBeRecord")
public class DumpingRecipe implements SerializableRecipe {
    public final Ingredient inputItem;
    public final Potion inputPotion;
    public final ItemStack output;
    public final boolean wildPotion;
    public final int fluidUsage;

    public DumpingRecipe(Ingredient inputItem, Potion inputPotion, ItemStack output, boolean wildPotion, int fluidUsage) {
        this.inputItem = inputItem;
        this.inputPotion = inputPotion;
        this.output = output;
        this.wildPotion = wildPotion;
        this.fluidUsage = fluidUsage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Potion inputPotion = null;
        private Ingredient input = null;
        private ItemStack output = null;
        private Integer fluidUsage = null;
        private Boolean wildPotion = false;

        private Builder() {

        }

        public Builder input(Potion potion) {
            this.inputPotion = potion;
            return this;
        }

        public Builder input(Ingredient ingredient) {
            this.input = ingredient;
            return this;
        }

        public Builder output(ItemStack stack) {
            this.output = stack;
            return this;
        }

        public Builder wild(Boolean wild) {
            this.wildPotion = wild;
            return this;
        }

        public Builder uses(Integer fluid) {
            this.fluidUsage = fluid;
            return this;
        }

        public DumpingRecipe build() {
            if (inputPotion == null || input == null || output == null || wildPotion == null) {
                throw new NotAllFieldsFilledException();
            }
            return new DumpingRecipe(input, inputPotion, output, wildPotion, fluidUsage);
        }
    }

    public JsonObject toJson() {
        JsonObject root = new JsonObject();

        root.add("item", inputItem.toJson());
        root.addProperty("potion", Registries.POTION.getId(inputPotion).toString());
        root.addProperty("wild_potion", wildPotion);
        root.addProperty("fluid_usage", fluidUsage);

        JsonObject outputJson = new JsonObject();
        outputJson.addProperty("item", Registries.ITEM.getId(output.getItem()).toString());

        if (output.getCount() > 1) {
            outputJson.addProperty("count", output.getCount());
        }

        if (output.hasNbt()) {
            NbtCompound nbt = output.getNbt();
            outputJson.add("nbt", NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, nbt));
        }
        root.add("output", outputJson);

        return root;
    }

    @Override
    public String getSubPath() {
        return "dumping";
    }
}
