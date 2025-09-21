package org.lilbrocodes.elixiry.common.config;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;

public class Configs {
    public static ElixiryServerConfig SERVER;

    public static void initialize() {
        SERVER = ConfigApiJava.registerAndLoadConfig(ElixiryServerConfig::new, RegisterType.BOTH);
    }
}
