package com.ragnardragus.yhnp.api;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class LevelUpEvent extends PlayerEvent {

    private final int oldLevel;
    private final int newLevel;

    public LevelUpEvent(Player player, int oldLevel, int newLevel) {
        super(player);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    @Override
    public Player getEntity() {
        return super.getEntity();
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }
}
