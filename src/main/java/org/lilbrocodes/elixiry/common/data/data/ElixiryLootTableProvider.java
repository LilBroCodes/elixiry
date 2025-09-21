package org.lilbrocodes.elixiry.common.data.data;


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import org.lilbrocodes.elixiry.common.registry.ModBlocks;

public class ElixiryLootTableProvider extends FabricBlockLootTableProvider {

    public ElixiryLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.WITCH_CAULDRON.block);
    }
}
