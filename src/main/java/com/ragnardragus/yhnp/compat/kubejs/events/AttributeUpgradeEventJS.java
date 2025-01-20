package com.ragnardragus.yhnp.compat.kubejs.events;

import com.ragnardragus.yhnp.capability.attributes.ModAttributes;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.player.Player;

public class AttributeUpgradeEventJS extends PlayerEventJS {

    private final Player player;
    private final ModAttributes attribute;
    private final int oldLevel;
    private final int newLevel;

    public AttributeUpgradeEventJS(Player player, ModAttributes attribute, int oldLevel, int newLevel) {
        this.player = player;
        this.attribute = attribute;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    @Override
    public Player getEntity() {
        return player;
    }

    public ModAttributes getAttribute() {
        return attribute;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }
}
