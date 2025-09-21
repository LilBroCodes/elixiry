package org.lilbrocodes.elixiry.common.registry;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import org.lilbrocodes.elixiry.Elixiry;

public class ModBlockTags {
    public static final TagKey<Block> WEAK_ARCANE_BLOCKS = TagKey.of(Registries.BLOCK.getKey(), Elixiry.identify("weak_arcane_blocks"));
    public static final TagKey<Block> STRONG_ARCANE_BLOCKS = TagKey.of(Registries.BLOCK.getKey(), Elixiry.identify("strong_arcane_blocks"));
}
