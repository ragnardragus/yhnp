package com.ragnardragus.yhnp.network.levels;

import com.ragnardragus.yhnp.capability.level.ModLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SkillPointMsg {

    private final int value;

    public SkillPointMsg(int value) {
        this.value = value;
    }

    public static void encode(SkillPointMsg msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.value);
    }

    public static SkillPointMsg decode(FriendlyByteBuf buffer) {
        int value = buffer.readInt();
        return new SkillPointMsg(value);
    }

    public static class Handler {
        public static void handle(final SkillPointMsg msg, Supplier<NetworkEvent.Context> ctx) {

            Minecraft mc = Minecraft.getInstance();

            ctx.get().enqueueWork(() -> {
                mc.player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(skillPoint -> {
                    skillPoint.setSkillPoints(msg.value);
                });
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
