package org.lilbrocodes.elixiry.common.data.loader.json;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.common.recipe.dumping.DumpingRecipe;

public class DumpingRecipeJsonHelper {
    public static DumpingRecipe fromJson(JsonObject json) {
        Ingredient inputItem = Ingredient.fromJson(json.get("item"));

        Potion inputPotion = Registries.POTION.getOrEmpty(
                Identifier.tryParse(json.get("potion").getAsString())
        ).orElseThrow();

        boolean wildPotion = json.has("wild_potion") && json.get("wild_potion").getAsBoolean();
        int fluidUsage = json.has("fluid_usage") ? json.get("fluid_usage").getAsInt() : 0;

        JsonObject outputObj = json.getAsJsonObject("output");
        ItemStack output = parseItemStack(outputObj);

        return new DumpingRecipe(inputItem, inputPotion, output, wildPotion, fluidUsage);
    }

    private static ItemStack parseItemStack(JsonObject json) {
        String itemId = json.get("item").getAsString();
        var item = Registries.ITEM.getOrEmpty(net.minecraft.util.Identifier.tryParse(itemId))
                .orElse(Items.AIR);

        int count = json.has("count") ? json.get("count").getAsInt() : 1;

        return new ItemStack(item, count);
    }
}
