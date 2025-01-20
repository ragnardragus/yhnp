package com.ragnardragus.yhnp.capability.classes;

import com.ragnardragus.yhnp.capability.attributes.ModAttributes;
import com.ragnardragus.yhnp.requirement.Requirement;
import net.minecraft.resources.ResourceLocation;

public enum ModClasses {

    NONE("vagabond", new Requirement[]{},
            "vagabond.desc", new ResourceLocation("minecraft:air")),
    WARRIOR("warrior", new Requirement[]{
            new Requirement(ModAttributes.STRENGTH, 1),
            new Requirement(ModAttributes.COMBAT, 3),
            new Requirement(ModAttributes.TENACITY, 2),
            new Requirement(ModAttributes.DEXTERITY, 1)
    }, "warrior.desc", new ResourceLocation("minecraft:iron_sword")),
    ROGUE("rogue", new Requirement[]{
            new Requirement(ModAttributes.COMBAT, 2),
            new Requirement(ModAttributes.WISDOM, 2),
            new Requirement(ModAttributes.DEXTERITY, 3)
    }, "rogue.desc", new ResourceLocation("minecraft:crossbow")),
    MAGE("mage", new Requirement[]{
            new Requirement(ModAttributes.COMBAT, 1),
            new Requirement(ModAttributes.WISDOM, 3),
            new Requirement(ModAttributes.CULTIVATION, 1),
            new Requirement(ModAttributes.CRAFTING, 2)
    }, "vagabond.desc", new ResourceLocation("minecraft:blaze_powder")),
    ARTIFICER("artificer", new Requirement[]{
            new Requirement(ModAttributes.CRAFTING, 3),
            new Requirement(ModAttributes.TENACITY, 1),
            new Requirement(ModAttributes.DEXTERITY, 1),
            new Requirement(ModAttributes.WISDOM, 2)
    }, "vagabond.desc", new ResourceLocation("minecraft:redstone")),
    DRUID("druid", new Requirement[]{
            new Requirement(ModAttributes.STRENGTH, 2),
            new Requirement(ModAttributes.TENACITY, 2),
            new Requirement(ModAttributes.CULTIVATION, 3)
    }, "vagabond.desc", new ResourceLocation("minecraft:green_dye")),
    BARBARIAN("barbarian", new Requirement[]{
            new Requirement(ModAttributes.STRENGTH, 3),
            new Requirement(ModAttributes.COMBAT, 3),
            new Requirement(ModAttributes.TENACITY, 1)
    }, "vagabond.desc", new ResourceLocation("minecraft:iron_axe")),
    HUNTER("hunter", new Requirement[]{
            new Requirement(ModAttributes.SURVIVAL, 3),
            new Requirement(ModAttributes.STRENGTH, 2),
            new Requirement(ModAttributes.CULTIVATION, 2)
    }, "vagabond.desc", new ResourceLocation("minecraft:bow")),
    CLERIC("cleric", new Requirement[]{
            new Requirement(ModAttributes.STRENGTH, 2),
            new Requirement(ModAttributes.COMBAT, 2),
            new Requirement(ModAttributes.TENACITY, 3)
    },"vagabond.desc", new ResourceLocation("minecraft:brewing_stand"));

    public final String displayName;
    public final Requirement[] statsBonus;

    public final String desc;

    public final ResourceLocation icon;

    ModClasses(String name, Requirement[] statsBonus, String desc, ResourceLocation icon) {
        this.displayName = name;
        this.statsBonus = statsBonus;
        this.desc = desc;
        this.icon = icon;
    }

    public static ModClasses getClassByName(String displayName) {
        for (ModClasses czz : ModClasses.values()) {
            if(czz.displayName.equals(displayName)) {
                return czz;
            }
        }
        return NONE;
    }

    public static ModClasses getNextClass(ModClasses actualClass) {
        ModClasses[] modClasses = ModClasses.values();
        int actualIndex = getIndexByClass(actualClass);
        if((actualIndex + 1) > (modClasses.length-1)) {
            return modClasses[0];
        } else {
            return modClasses[actualIndex+1];
        }
    }

    public static ModClasses getPreviousClass(ModClasses actualClass) {
        ModClasses[] modClasses = ModClasses.values();
        int actualIndex = getIndexByClass(actualClass);
        if((actualIndex - 1) < 0) {
            return modClasses[modClasses.length-1];
        } else {
            return modClasses[actualIndex-1];
        }
    }

    public static int getIndexByClass(ModClasses actualClass) {
        ModClasses[] srClasses = ModClasses.values();
        for (int index = 0; index < srClasses.length; index++) {
            if(srClasses[index].displayName.equals(actualClass.displayName)) {
                return index;
            }
        }
        return 0;
    }
}
