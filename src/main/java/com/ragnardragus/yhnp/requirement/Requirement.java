package com.ragnardragus.yhnp.requirement;

import com.ragnardragus.yhnp.capability.attributes.ModAttributes;

public class Requirement {
    public ModAttributes modAttributes;
    public int level;
    
    public Requirement(ModAttributes modAttributes, int level) {
        this.modAttributes = modAttributes;
        this.level = level;
    }

    @Override
    public String toString() {
        return modAttributes + ":" + level;
    }

    public static Requirement parseByString(String requirement) {
        if(!validateRequirementByString(requirement)) return null;

        String[] split = requirement.split(":");
        return new Requirement(ModAttributes.valueOf(split[0].toUpperCase()), Integer.parseInt(split[1]));
    }

    private static boolean validateRequirementByString(String requirement) {
        if(requirement.isEmpty()) return false;

        String[] split = requirement.split(":");
        if (split.length != 2) return false;

        String attribute = split[0].trim().toUpperCase();
        if (!attribute.matches("^[A-Z]+$")) return false;

        try {
            Integer.parseInt(split[1].trim());
        } catch (IllegalArgumentException e) {
            return false;
        }

        return ModAttributes.match(split[0]);
    }
}