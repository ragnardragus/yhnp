package com.ragnardragus.yhnp.network.levels;

import com.ragnardragus.yhnp.capability.level.ModLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ModLevelsMsg {

    private final int value;

    public ModLevelsMsg(int value) {
        this.value = value;
    }

    public static void encode(ModLevelsMsg msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.value);
    }

    public static ModLevelsMsg decode(FriendlyByteBuf buffer) {
        int value = buffer.readInt();
        return new ModLevelsMsg(value);
    }

    public static class Handler {
        public static void handle(final ModLevelsMsg msg, Supplier<NetworkEvent.Context> ctx) {

            Minecraft mc = Minecraft.getInstance();

            ctx.get().enqueueWork(() -> {
                mc.player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(levelData -> {
                    levelData.setPlayerModLevel(msg.value);
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
