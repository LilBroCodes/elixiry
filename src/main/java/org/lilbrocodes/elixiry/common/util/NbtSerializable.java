package org.lilbrocodes.elixiry.common.util;

import net.minecraft.nbt.NbtCompound;

public interface NbtSerializable<T> {
    T readNbt(NbtCompound tag);
    NbtCompound writeNbt(NbtCompound tag);
    default NbtCompound writeNbt() {
        return writeNbt(new NbtCompound());
    };
}
