package org.lilbrocodes.elixiry.util;

import net.minecraft.nbt.NbtCompound;

public class PotionModifier {
    public int level;
    public int duration;
    public PotionBottle bottle;

    public PotionModifier(int level, int duration, PotionBottle bottle) {
        this.level = level;
        this.duration = duration;
        this.bottle = bottle;
    }

    public static PotionModifier readNbt(NbtCompound tag) {
        return new PotionModifier(tag.getInt("Level"), tag.getInt("Duration"), PotionBottle.valueOf(tag.getString("Bottle")));
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putInt("Level", level);
        tag.putInt("Duration", duration);
        tag.putString("Bottle", bottle.name());
        return tag;
    }

    public void modify(int level, int duration) {
        this.level += level;
        this.duration += duration;
    }
}
