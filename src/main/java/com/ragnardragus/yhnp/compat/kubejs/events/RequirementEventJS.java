package com.ragnardragus.yhnp.compat.kubejs.events;

import com.ragnardragus.yhnp.capability.attributes.ModAttributes;
import com.ragnardragus.yhnp.requirement.Requirement;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class RequirementEventJS extends EventJS {

    private Map<ResourceLocation, Requirement[]> skillsDataMap = new HashMap<>();
    private Map<ResourceLocation, String> stageDataMap = new HashMap<>();
    private Map<ResourceLocation, Boolean> arrowDataMap = new HashMap<>();
    private Map<ResourceLocation, Boolean> spellDataMap = new HashMap<>();

    public RequirementEventJS(Map<ResourceLocation, Requirement[]> skillsDataMap,
      Map<ResourceLocation, String> stageDataMap, Map<ResourceLocation, Boolean> arrowDataMap, Map<ResourceLocation, Boolean> spellDataMap) {

        this.skillsDataMap = skillsDataMap;
        this.stageDataMap = stageDataMap;
        this.arrowDataMap = arrowDataMap;
        this.spellDataMap = spellDataMap;
    }

    public Map<ResourceLocation, Requirement[]> getSkillsDataMap() {
        return skillsDataMap;
    }

    public Map<ResourceLocation, String> getStageDataMap() {
        return stageDataMap;
    }

    public Map<ResourceLocation, Boolean> getArrowDataMap() {
        return arrowDataMap;
    }

    public Map<ResourceLocation, Boolean> getSpellDataMap() {
        return spellDataMap;
    }

    public void changeItemRequirement(Item item, String[] requirements) {
        changeItemRequirement(item, requirements, "", false);
    }

    public void changeItemRequirement(Item item, String[] requirements, String stage) {
        changeItemRequirement(item, requirements, stage, false);
    }

    public void changeItemRequirement(Item item, String[] requirements, String stage, boolean isArrow) {
        ResourceLocation rsItem = ForgeRegistries.ITEMS.getKey(item);
        if(changeRequirement(rsItem, requirements, stage)) {
            if (isArrow) arrowDataMap.put(ForgeRegistries.ITEMS.getKey(item), true);
        }
    }

    public void changeSpellRequirement(String spellId, String[] requirements) {
        changeSpellRequirement(spellId, requirements, "");
    }

    public void changeSpellRequirement(String spellId, String[] requirements, String stage) {
        ResourceLocation rsSpell = new ResourceLocation(spellId);
        if(changeRequirement(rsSpell, requirements, stage)) {
            spellDataMap.put(rsSpell, true);
        }
    }

    private boolean changeRequirement(ResourceLocation rs, String[] requirements, String stage) {
        List<Requirement> newRequirements = new ArrayList<>();

        for (String requirement : requirements) {
            Requirement req = Requirement.parseByString(requirement);
            newRequirements.add(req);
        }

        if(!newRequirements.isEmpty()) {
            skillsDataMap.put(rs, newRequirements.toArray(new Requirement[0]));
            if(!stage.isEmpty()) stageDataMap.put(rs, stage);
            return true;
        }

        return false;
    }
}
