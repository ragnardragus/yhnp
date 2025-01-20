package com.ragnardragus.yhnp.network.classes;

import com.ragnardragus.yhnp.capability.classes.ModClass;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ModClassMsg {

    public final String modClass;

    public ModClassMsg(String modClass) {
        this.modClass = modClass;
    }

    public static void encode(ModClassMsg msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.modClass);
    }

    public static ModClassMsg decode(FriendlyByteBuf buffer) {
        String modClass = buffer.readUtf();
        return new ModClassMsg(modClass);
    }

    public static class Handler {
        public static void handle(final ModClassMsg msg, Supplier<NetworkEvent.Context> ctx) {
            Minecraft mc = Minecraft.getInstance();

            ctx.get().enqueueWork(() -> {
                mc.player.getCapability(ModClass.ModClassCapability.INSTANCE).ifPresent(modClass -> {
                    modClass.setModClass(msg.modClass);
                });
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
