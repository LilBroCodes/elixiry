package org.lilbrocodes.elixiry.common.registry;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity;
import org.lilbrocodes.elixiry.common.block.entity.model.StirringStickModel;

public class ModModelLayers {
    public static final EntityModelLayer STIRRING_STICK =
            new EntityModelLayer(Elixiry.identify("stirring_stick"), "main");

    public static void initializeClient() {
        EntityModelLayerRegistry.registerModelLayer(STIRRING_STICK, StirringStickModel::getTexturedModelData);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (state != null && world != null) {
                return WitchCauldronBlockEntity.getBlockWaterColor(state, world, pos, tintIndex);
            } else {
                return 0x385dc6;
            }
        }, ModBlocks.WITCH_CAULDRON.block);
    }
}
