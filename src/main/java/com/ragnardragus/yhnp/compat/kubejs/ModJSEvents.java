package com.ragnardragus.yhnp.compat.kubejs;

import com.ragnardragus.yhnp.compat.kubejs.events.AttributeUpgradeEventJS;
import com.ragnardragus.yhnp.compat.kubejs.events.LevelUpEventJS;
import com.ragnardragus.yhnp.compat.kubejs.events.RequirementEventJS;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface ModJSEvents {
    EventGroup GROUP = EventGroup.of("YHNP");
    EventHandler LEVEL_UP = GROUP.server("onLevelUp", () -> LevelUpEventJS.class);
    EventHandler ATTR_UP = GROUP.server("onAttributeUp", () -> AttributeUpgradeEventJS.class);
    EventHandler REQUIREMENT = GROUP.server("requirement", () -> RequirementEventJS.class);
}
