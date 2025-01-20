package com.ragnardragus.yhnp.events.capabilities;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.capability.level.ModLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Yhnp.MOD_ID)
public class ModLevelEvent {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        if(!event.isWasDeath())
            return;

        oldPlayer.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(oldLevels -> {
            newPlayer.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(newLevels -> {
                newLevels.setSkillPoints(oldLevels.getSkillPoints());
                newLevels.setMcLevelsNeed(oldLevels.getMcLevelsNeed());
                newLevels.setPlayerModLevel(oldLevels.getPlayerModLevel());
            });
        });

        oldPlayer.invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        syncClientSideLevels(player);
    }

    @SubscribeEvent
    public static void onRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        syncClientSideLevels(player);
    }

    @SubscribeEvent
    public static void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        syncClientSideLevels(player);
    }

    private static void syncClientSideLevels(ServerPlayer player) {
        if(player != null && player.level() != null && !player.level().isClientSide) {
            player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(skill -> skill.sync(player));
        }
    }
}
