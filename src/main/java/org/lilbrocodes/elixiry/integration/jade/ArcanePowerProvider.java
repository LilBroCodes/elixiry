package org.lilbrocodes.elixiry.integration.jade;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lilbrocodes.elixiry.Elixiry;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.ui.ProgressElement;

public enum ArcanePowerProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
        int power = accessor.getServerData().getInt("Power");
        float progress = (float) power / WitchCauldronBlockEntity.MAX_POWER;

        IElement bar = new ProgressElement(
                progress,
                Text.empty(),
                new PowerBar(power),
                new BoxStyle(),
                true
        );

        iTooltip.add(bar);
    }

    @Override
    public void appendServerData(NbtCompound tag, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof WitchCauldronBlockEntity be)) return;
        tag.putInt("Power", be.calculateArcanePower());
    }

    @Override
    public Identifier getUid() {
        return Elixiry.identify("arcane_power");
    }
}
