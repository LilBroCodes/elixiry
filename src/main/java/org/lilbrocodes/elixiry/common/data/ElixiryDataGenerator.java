package org.lilbrocodes.elixiry.common.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.lilbrocodes.elixiry.common.data.assets.ElixiryLanguageProvider;
import org.lilbrocodes.elixiry.common.data.data.*;

public class ElixiryDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();

        // assets
        pack.addProvider(ElixiryLanguageProvider::new);

        // data
        pack.addProvider(ElixiryBlockTagProvider::new);
        pack.addProvider(ElixiryBrewingRecipeProvider::new);
        pack.addProvider(ElixiryDumpingRecipeProvider::new);
        pack.addProvider(ElixiryLootTableProvider::new);
        pack.addProvider(ElixiryRecipeProvider::new);
    }
}
