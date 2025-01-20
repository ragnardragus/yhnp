package com.ragnardragus.yhnp.network.levels;

import com.ragnardragus.yhnp.capability.level.ModLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class McLevelsNeedMsg {

    private final int value;

    public McLevelsNeedMsg(int value) {
        this.value = value;
    }


    public static void encode(McLevelsNeedMsg msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.value);
    }

    public static McLevelsNeedMsg decode(FriendlyByteBuf buffer) {
        int value = buffer.readInt();
        return new McLevelsNeedMsg(value);
    }

    public static class Handler {
        public static void handle(final McLevelsNeedMsg msg, Supplier<NetworkEvent.Context> ctx) {

            Minecraft mc = Minecraft.getInstance();

            ctx.get().enqueueWork(() -> {
                mc.player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(skillPoint -> {
                    skillPoint.setMcLevelsNeed(msg.value);
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
