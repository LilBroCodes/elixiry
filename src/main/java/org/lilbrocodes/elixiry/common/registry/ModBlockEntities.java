package org.lilbrocodes.elixiry.common.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity;
import org.lilbrocodes.elixiry.common.block.entity.render.WitchCauldronBlockEntityRenderer;

public class ModBlockEntities {
    public static final BlockEntityType<WitchCauldronBlockEntity> WITCH_CAULDRON = register(FabricBlockEntityTypeBuilder.create(WitchCauldronBlockEntity::new, ModBlocks.WITCH_CAULDRON.block).build(), "witch_cauldron");

    public static void initializeClient() {
        BlockEntityRendererFactories.register(WITCH_CAULDRON, WitchCauldronBlockEntityRenderer::new);
    }

    public static void initialize() {

    }

    private static  <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType<T> blockEntityType, String id) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Elixiry.identify(id), blockEntityType);
    }
}
