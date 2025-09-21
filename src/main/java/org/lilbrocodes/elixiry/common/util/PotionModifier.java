package org.lilbrocodes.elixiry.common.util;

import net.minecraft.nbt.NbtCompound;

public class PotionModifier {
    public int level;
    public int duration;

    public PotionModifier(int level, int duration) {
        this.level = level;
        this.duration = duration;
    }

    public static PotionModifier readNbt(NbtCompound tag) {
        return new PotionModifier(tag.getInt("Level"), tag.getInt("Duration"));
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putInt("Level", level);
        tag.putInt("Duration", duration);
        return tag;
    }

    public void modify(int level, int duration) {
        this.level += level;
        this.duration += duration;
    }
}
