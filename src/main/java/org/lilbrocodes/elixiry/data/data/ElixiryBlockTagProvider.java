package org.lilbrocodes.elixiry.data.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import org.lilbrocodes.elixiry.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ElixiryBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ElixiryBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.WITCH_CAULDRON.block);
    }
}
