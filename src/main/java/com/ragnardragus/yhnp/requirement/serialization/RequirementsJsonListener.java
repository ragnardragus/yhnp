package com.ragnardragus.yhnp.requirement.serialization;

import com.google.gson.*;
import com.ragnardragus.yhnp.capability.attributes.ModAttributes;
import com.ragnardragus.yhnp.compat.kubejs.ModJSEvents;
import com.ragnardragus.yhnp.compat.kubejs.events.RequirementEventJS;
import com.ragnardragus.yhnp.requirement.Requirement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class RequirementsJsonListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private Map<ResourceLocation, Requirement[]> skillsDataMap = new HashMap<>();
    private Map<ResourceLocation, String> stageDataMap = new HashMap<>();
    private Map<ResourceLocation, Boolean> arrowDataMap = new HashMap<>();
    private Map<ResourceLocation, Boolean> spellDataMap = new HashMap<>();

    private static final String ATTRIBUTE = "attribute";
    private static final String LEVEL = "level";
    private static final String REQUIREMENTS = "requirements";

    private static final String STAGE = "stage";
    private static final String ARROW = "arrow";
    private static final String SPELL = "spell";


    public RequirementsJsonListener() {
        super(GSON, "skills");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager resourceManager, ProfilerFiller profiler) {
        data.entrySet().forEach(e -> {
            ResourceLocation key = e.getKey();
            JsonElement value = e.getValue();
            JsonObject jsonObject = value.getAsJsonObject();

            if(jsonObject.has(STAGE)) {
                String stage = JsonParseHelper.getString(jsonObject, STAGE);

                if(!stage.isEmpty())
                    stageDataMap.put(new ResourceLocation(key.getPath().replace("/", ":")), stage);
            }

            if(jsonObject.has(ARROW)) {
                Boolean isArrow = jsonObject.get(ARROW).getAsBoolean();
                arrowDataMap.put(new ResourceLocation(key.getPath().replace("/", ":")), isArrow);
            }

            // Compat with Iron's Spells n SpellBooks
            if(jsonObject.has(SPELL)) {
                Boolean isSpell = jsonObject.get(SPELL).getAsBoolean();
                spellDataMap.put(new ResourceLocation(key.getPath().replace("/", ":")), isSpell);
            }

            if(jsonObject.has(REQUIREMENTS)) {
                JsonArray requirementsJson = jsonObject.getAsJsonArray(REQUIREMENTS);
                Requirement[] requirements = requirementParse(requirementsJson);
                if(requirements.length != 0) {
                    var skillData = new ResourceLocation(key.getPath().replace("/", ":"));
                    skillsDataMap.put(skillData, requirements);
                }
            }
        });

        ModJSEvents.REQUIREMENT.post(new RequirementEventJS(skillsDataMap, stageDataMap, arrowDataMap, spellDataMap));
    }

    public static Requirement[] requirementParse(JsonArray requirementsJson) {
        Requirement[] requirements = new Requirement[requirementsJson.size()];

        for(int i = 0; i < requirementsJson.size(); i++) {
            JsonObject jsonObject = requirementsJson.get(i).getAsJsonObject();

            if(jsonObject.has(ATTRIBUTE) && jsonObject.has(LEVEL)) {

                ModAttributes attributes = ModAttributes.valueOf(JsonParseHelper.getString(jsonObject, ATTRIBUTE).toUpperCase());
                int level = JsonParseHelper.getInt(jsonObject,LEVEL);

                Requirement requirement = new Requirement(attributes, level);
                requirements[i] = requirement;
            }
        }

        return requirements;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public Requirement[] getRequirements(ResourceLocation resourceLocation) {
        Requirement[] requirements = skillsDataMap.get(resourceLocation);
        return requirements != null ? requirements : new Requirement[]{};
    }

    public String getRequirementStageName(ResourceLocation resourceLocation) {
        return stageDataMap.get(resourceLocation);
    }

    public boolean isArrow(ResourceLocation resourceLocation) {
        return arrowDataMap.get(resourceLocation) != null ? arrowDataMap.get(resourceLocation) : false;
    }

    public boolean isSpell(ResourceLocation resourceLocation) {
        return spellDataMap.get(resourceLocation) != null ? spellDataMap.get(resourceLocation) : false;
    }
}
