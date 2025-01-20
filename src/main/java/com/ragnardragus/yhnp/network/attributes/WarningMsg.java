package com.ragnardragus.yhnp.network.attributes;

import com.ragnardragus.yhnp.client.gui.overlay.notification.WarningHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WarningMsg {

    private final ResourceLocation resource;

    public WarningMsg(ResourceLocation resource)
    {
        this.resource = resource;
    }

    public WarningMsg(FriendlyByteBuf buffer)
    {
        resource = buffer.readResourceLocation();
    }

    public static WarningMsg decode(FriendlyByteBuf buffer) {
        ResourceLocation resource = buffer.readResourceLocation();
        return new WarningMsg(resource);
    }

    public static void encode(WarningMsg msg, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(msg.resource);
    }

    public static class Handler {
        public static void handle(WarningMsg msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> WarningHandler.showWarning(msg.resource));
            context.get().setPacketHandled(true);
        }
    }
}
