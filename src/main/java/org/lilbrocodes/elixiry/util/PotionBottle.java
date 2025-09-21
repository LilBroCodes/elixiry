package org.lilbrocodes.elixiry.util;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public enum PotionBottle {
    NORMAL, SPLASH, LINGERING;

    public Item getItem() {
        return switch (this) {
            case NORMAL -> Items.POTION;
            case SPLASH -> Items.SPLASH_POTION;
            case LINGERING -> Items.LINGERING_POTION;
        };
    }
}
