package com.ragnardragus.yhnp.events.capabilities;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.capability.classes.ModClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Yhnp.MOD_ID)
public class ModClassEvent {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        if(!event.isWasDeath())
            return;

        oldPlayer.getCapability(ModClass.ModClassCapability.INSTANCE).ifPresent(oldClass -> {
            newPlayer.getCapability(ModClass.ModClassCapability.INSTANCE).ifPresent(newClass -> {
                newClass.setModClass(oldClass.getModClass());
            });
        });

        oldPlayer.invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        syncClientSideClass(player);
    }

    @SubscribeEvent
    public static void onRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        syncClientSideClass(player);
    }

    @SubscribeEvent
    public static void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        syncClientSideClass(player);
    }

    private static void syncClientSideClass(ServerPlayer player) {
        if(player != null && player.level() != null && !player.level().isClientSide) {
            player.getCapability(ModClass.ModClassCapability.INSTANCE).ifPresent(modClass -> modClass.sync(player));
        }
    }
}
