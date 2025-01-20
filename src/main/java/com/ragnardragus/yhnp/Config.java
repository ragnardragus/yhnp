package com.ragnardragus.yhnp;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_ATTRIBUTE_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> LEVEL_UP_SYSTEM;

    static {

        BUILDER.comment("Max of attribute levels - Default 16");
        MAX_ATTRIBUTE_LEVEL = BUILDER.defineInRange("maxAttributeLevels", 16, 8, 128);

        BUILDER.comment("Activate mod level up system - Default TRUE");
        LEVEL_UP_SYSTEM = BUILDER.define("levelUpSystem", true);
        SPEC = BUILDER.build();
    }
}
