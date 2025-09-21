package org.lilbrocodes.elixiry.common.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import org.lilbrocodes.elixiry.Elixiry;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> ELIXIRY_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Elixiry.identify("elixiry"));

    public static final ItemGroup ELIXIRY_ITEMS_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.WITCH_CAULDRON.item))
            .displayName(Text.translatable("elixiry.name"))
            .build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, ELIXIRY_ITEM_GROUP_KEY, ELIXIRY_ITEMS_GROUP);
    }
}
