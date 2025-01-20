package com.ragnardragus.yhnp.events.capabilities;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.network.PacketHandler;
import com.ragnardragus.yhnp.network.attributes.ModAttributeMsg;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Yhnp.MOD_ID)
public class ModAttributeEvent {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        if(!event.isWasDeath())
            return;

        oldPlayer.getCapability(ModAttribute.AttributeCapability.INSTANCE).ifPresent(i -> {
            newPlayer.getCapability(ModAttribute.AttributeCapability.INSTANCE).ifPresent(j -> {
                ModAttribute.Implementation.get(newPlayer).setAttributeLevels(ModAttribute.Implementation.get(oldPlayer).getAttributeLevels());
            });
        });

        oldPlayer.invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent e) {
        if(e.getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) e.getEntity();
            PacketHandler.sendToPlayer(new ModAttributeMsg(ModAttribute.Implementation.get(player).serializeNBT()), player);
        }
    }

    @SubscribeEvent
    public static void onRespawnEvent(PlayerEvent.PlayerRespawnEvent e) {
        if(e.getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) e.getEntity();
            PacketHandler.sendToPlayer(new ModAttributeMsg(ModAttribute.Implementation.get(player).serializeNBT()), player);
        }
    }

    @SubscribeEvent
    public static void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent e) {
        if(e.getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) e.getEntity();
            PacketHandler.sendToPlayer(new ModAttributeMsg(ModAttribute.Implementation.get(player).serializeNBT()), player);
        }
    }
}
