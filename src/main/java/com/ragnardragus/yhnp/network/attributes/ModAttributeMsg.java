package com.ragnardragus.yhnp.network.attributes;

import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ModAttributeMsg {

    private final CompoundTag skillModel;

    public ModAttributeMsg(CompoundTag skillModel) {
        this.skillModel = skillModel;
    }

    public ModAttributeMsg(FriendlyByteBuf buffer) {
        this.skillModel = buffer.readNbt();
    }

    public static ModAttributeMsg decode(FriendlyByteBuf buffer) {
        CompoundTag tag = buffer.readNbt();
        return new ModAttributeMsg(tag);
    }

    public static void encode(ModAttributeMsg msg, FriendlyByteBuf buffer) {
        buffer.writeNbt(msg.skillModel);
    }

    public static class Handler {
        public static void handle(final ModAttributeMsg msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> ModAttribute.Implementation.get().deserializeNBT(msg.skillModel));
            ctx.get().setPacketHandled(true);
        }
    }
}
