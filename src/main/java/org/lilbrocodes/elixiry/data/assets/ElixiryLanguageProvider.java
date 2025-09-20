package org.lilbrocodes.elixiry.data.assets;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import org.lilbrocodes.elixiry.registry.ModBlocks;
import org.lilbrocodes.elixiry.registry.ModItemGroups;

public class ElixiryLanguageProvider extends FabricLanguageProvider {
    public ElixiryLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        // Blocks
        builder.add(ModBlocks.WITCH_CAULDRON.block, "Witch's Cauldron");

        // Item Groups
        builder.add(ModItemGroups.ELIXIRY_ITEM_GROUP_KEY, "Elixiry");
    }
}
