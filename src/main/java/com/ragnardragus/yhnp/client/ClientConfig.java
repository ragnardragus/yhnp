package com.ragnardragus.yhnp.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> RENDER_TAB_BUTTONS;

    static {

        BUILDER.comment("Render tab buttons - Default TRUE");
        RENDER_TAB_BUTTONS = BUILDER.define("renderTabButtons", true);
        SPEC = BUILDER.build();
    }
}
