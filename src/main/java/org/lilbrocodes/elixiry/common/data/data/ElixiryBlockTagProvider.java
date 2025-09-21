package org.lilbrocodes.elixiry.common.data.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.common.registry.ModBlockTags;
import org.lilbrocodes.elixiry.common.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ElixiryBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ElixiryBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.WITCH_CAULDRON.block);

        getOrCreateTagBuilder(ModBlockTags.WEAK_ARCANE_BLOCKS)
                .addOptionalTag(BlockTags.CANDLES)
                .add(Blocks.SKELETON_SKULL)
                .add(Blocks.SKELETON_WALL_SKULL)
                .add(Blocks.SOUL_LANTERN)
                .add(Blocks.SOUL_TORCH)
                .add(Blocks.AMETHYST_BLOCK)
                .add(Blocks.SMALL_AMETHYST_BUD)
                .add(Blocks.MEDIUM_AMETHYST_BUD)
                .add(Blocks.SOUL_WALL_TORCH)
                .add(Blocks.LARGE_AMETHYST_BUD)
                .add(Blocks.AMETHYST_CLUSTER)
                .add(Blocks.SOUL_CAMPFIRE)
                .add(Blocks.BUDDING_AMETHYST)
                .addOptional(Identifier.of("amendments", "skull_pile"))
                .addOptional(Identifier.of("handcrafted", "stackable_book"))
                .addOptional(Identifier.of("amendments", "skull_candle"))
                .addOptional(Identifier.of("amendments", "skull_candle_wall"))
                .addOptionalTag(Identifier.of("supplementaries", "candle_holders"));

        getOrCreateTagBuilder(ModBlockTags.STRONG_ARCANE_BLOCKS)
                .add(Blocks.WITHER_SKELETON_SKULL)
                .add(Blocks.WITHER_SKELETON_WALL_SKULL)
                .add(Blocks.WITHER_ROSE)
                .add(Blocks.POTTED_WITHER_ROSE)
                .add(Blocks.SOUL_FIRE)
                .add(Blocks.DRAGON_EGG)
                .addOptional(Identifier.of("amarite", "amarite_cluster"))
                .addOptional(Identifier.of("amarite", "budding_amarite"))
                .addOptional(Identifier.of("amarite", "amarite_block"))
                .addOptional(Identifier.of("amarite", "amarite_spark"))
                .addOptional(Identifier.of("amarite", "fresh_amarite_bud"))
                .addOptional(Identifier.of("amarite", "partial_amarite_bud"))
                .addOptional(Identifier.of("amendments", "skull_candle_soul"))
                .addOptional(Identifier.of("amendments", "skull_candle_soul_wall"))
                .addOptional(Identifier.of("spelunkery", "portal_fluid"))
                .addOptional(Identifier.of("spelunkery", "portal_cauldron"))
                .addOptional(Identifier.of("create_dd", "shimmer"));
    }
}
