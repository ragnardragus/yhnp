package com.ragnardragus.yhnp.compat.kubejs.events;


import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.player.Player;

public class LevelUpEventJS extends PlayerEventJS {

    private final Player player;
    private final int oldLevel;
    private final int newLevel;

    public LevelUpEventJS(Player player, int oldLevel, int newLevel) {
        this.player = player;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    @Override
    public Player getEntity() {
        return player;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

}
