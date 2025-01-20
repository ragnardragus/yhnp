package com.ragnardragus.yhnp.network.levels;

import com.ragnardragus.yhnp.Config;
import com.ragnardragus.yhnp.api.LevelUpEvent;
import com.ragnardragus.yhnp.capability.level.ModLevel;
import com.ragnardragus.yhnp.compat.kubejs.events.LevelUpEventJS;
import com.ragnardragus.yhnp.compat.kubejs.ModJSEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LevelUpMsg {

    private final boolean aux;

    public LevelUpMsg(boolean aux) {
        this.aux = aux;
    }

    public static LevelUpMsg decode(FriendlyByteBuf buffer) {
        return new LevelUpMsg(buffer.readBoolean());
    }

    public static void encode(LevelUpMsg msg, FriendlyByteBuf buffer) {
        buffer.writeBoolean(msg.aux);
    }

    public static class Handler {
        public static void handle(final LevelUpMsg msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();

                if(player != null && Config.LEVEL_UP_SYSTEM.get()) {
                    player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(levelData -> {

                        if (player.experienceLevel >= levelData.getMcLevelsNeed()) {
                            int oldLevelsNeed = levelData.getMcLevelsNeed();
                            player.giveExperienceLevels(-levelData.getMcLevelsNeed());
                            levelData.setMcLevelsNeed(oldLevelsNeed + 1);

                            var levelEvent = new LevelUpEvent(player, levelData.getPlayerModLevel(), levelData.getPlayerModLevel() + 1);
                            MinecraftForge.EVENT_BUS.post(levelEvent);

                            ModJSEvents.LEVEL_UP.post(new LevelUpEventJS(player, levelData.getPlayerModLevel(), levelData.getPlayerModLevel() + 1));

                            levelData.addPlayerModLevel();
                            levelData.addSkillPoints();

                            if(player instanceof ServerPlayer) {
                                levelData.sync(player);
                            }
                        }
                    });
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
