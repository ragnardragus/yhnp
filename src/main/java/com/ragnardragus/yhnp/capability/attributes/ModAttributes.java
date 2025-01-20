package com.ragnardragus.yhnp.capability.attributes;

public enum ModAttributes {
    STRENGTH    (0, "strength",    "str"),
    SURVIVAL    (1, "survival",    "srv"),
    COMBAT      (2, "combat",      "com"),
    TENACITY    (3, "tenacity",    "ten"),
    CRAFTING    (4, "crafting",    "crf"),
    CULTIVATION (5, "cultivation", "cul"),
    DEXTERITY   (6, "dexterity",   "dex"),
    WISDOM      (7, "wisdom",      "wis");

    public final int index;
    public final String displayName;
    public final String abbreviation;

    ModAttributes(int index, String name, String abbreviation) {
        this.index = index;
        this.displayName = name;
        this.abbreviation = abbreviation;
    }

    public static boolean match(String name) {
        for (ModAttributes attr : ModAttributes.values()) {
            if(attr.displayName.equalsIgnoreCase(name)) return true;
        }
        return false;
    }
}
