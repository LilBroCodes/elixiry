package org.lilbrocodes.elixiry.common.config;

import me.fzzyhmstrs.fzzy_config.api.FileType;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import org.jetbrains.annotations.NotNull;
import org.lilbrocodes.elixiry.Elixiry;

public class ElixiryServerConfig extends Config {
    public ElixiryServerConfig() {
        super(Elixiry.identify("server_config"));
    }

    public ValidatedBoolean arcaneDullness = new ValidatedBoolean(true);
    @Prefix("\"Arcane dullness\" defines how many of the same block type can be counted for arcane power of the Witch's Cauldron")
    public ValidatedInt maximumArcaneDullness = new ValidatedInt(3, 16, 1);

    @Override
    public int defaultPermLevel() {
        return 4;
    }

    @Override
    public @NotNull FileType fileType() {
        return FileType.JSONC;
    }

    @Override
    public @NotNull SaveType saveType() {
        return SaveType.SEPARATE;
    }

    @Override
    public @NotNull String translationKey() {
        return "elixiry.server_config";
    }

    @Override
    public boolean hasTranslation() {
        return true;
    }
}
