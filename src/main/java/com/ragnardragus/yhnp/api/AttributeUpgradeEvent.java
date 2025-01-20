package com.ragnardragus.yhnp.api;

import com.ragnardragus.yhnp.capability.attributes.ModAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class AttributeUpgradeEvent extends PlayerEvent {

    private final ModAttributes attribute;
    private final int oldLevel;
    private final int newLevel;

    public AttributeUpgradeEvent(Player player, ModAttributes attribute, int oldLevel, int newLevel) {
        super(player);
        this.attribute = attribute;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    @Override
    public Player getEntity() {
        return super.getEntity();
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
