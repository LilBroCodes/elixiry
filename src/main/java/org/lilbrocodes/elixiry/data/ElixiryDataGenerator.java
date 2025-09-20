package org.lilbrocodes.elixiry.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.lilbrocodes.elixiry.data.assets.ElixiryLanguageProvider;
import org.lilbrocodes.elixiry.data.data.ElixiryBlockTagProvider;
import org.lilbrocodes.elixiry.data.data.ElixiryLootTableProvider;
import org.lilbrocodes.elixiry.data.data.ElixiryRecipeProvider;

public class ElixiryDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();

        // assets
        pack.addProvider(ElixiryLanguageProvider::new);

        // data
        pack.addProvider(ElixiryBlockTagProvider::new);
        pack.addProvider(ElixiryLootTableProvider::new);
        pack.addProvider(ElixiryRecipeProvider::new);
    }
}
