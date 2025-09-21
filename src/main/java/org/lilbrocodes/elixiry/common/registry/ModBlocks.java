package org.lilbrocodes.elixiry.common.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.block.WitchCauldron;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    private static final List<BlockWithItem<?>> registeredBlocks = new ArrayList<>();

    public static final BlockWithItem<WitchCauldron> WITCH_CAULDRON = register(new WitchCauldron(), "witch_cauldron");

    public static void initializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(WITCH_CAULDRON.block, RenderLayer.getTranslucent());
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ModItemGroups.ELIXIRY_ITEM_GROUP_KEY).register(entries -> registeredBlocks.forEach(block -> entries.add(block.item.getDefaultStack())));
    }

    public static <T extends Block> BlockWithItem<T> register(
            T block, String name) {
        Identifier id = Elixiry.identify(name);
        BlockItem item = Registry.register(Registries.ITEM, id, new BlockItem(block, new FabricItemSettings()));
        BlockWithItem<T> registeredBlock = new BlockWithItem<>(id, Registry.register(Registries.BLOCK, id, block), item);
        registeredBlocks.add(registeredBlock);
        return registeredBlock;
    }

    public static class BlockWithItem<T extends Block> {
        public Identifier id;
        public final T block;
        public BlockItem item;

        public BlockWithItem(Identifier id, T block, BlockItem item) {
            this.id = id;
            this.block = block;
            this.item = item;
        }
    }
}
