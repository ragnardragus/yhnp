package com.ragnardragus.yhnp.network.classes;

import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.capability.attributes.ModAttributes;
import com.ragnardragus.yhnp.capability.classes.ModClass;
import com.ragnardragus.yhnp.capability.classes.ModClasses;
import com.ragnardragus.yhnp.network.PacketHandler;
import com.ragnardragus.yhnp.network.attributes.ModAttributeMsg;
import com.ragnardragus.yhnp.requirement.Requirement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeClassMsg {

    public final String modClass;

    public ChangeClassMsg(String modClass) {
        this.modClass = modClass;
    }

    public static void encode(ChangeClassMsg msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.modClass);
    }

    public static ChangeClassMsg decode(FriendlyByteBuf buffer) {
        String modClass = buffer.readUtf();
        return new ChangeClassMsg(modClass);
    }

    public static class Handler {

        public static void handle(final ChangeClassMsg msg, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayer player = ctx.get().getSender();

            ctx.get().enqueueWork(() -> {
                player.getCapability(ModClass.ModClassCapability.INSTANCE).ifPresent(modClass -> {
                    modClass.setModClass(msg.modClass);

                    if(player instanceof ServerPlayer) {
                        modClass.sync(player);
                    }
                });

                ModAttribute attribute = ModAttribute.Implementation.get(player);

                for(int i = 0; i < ModAttributes.values().length; i++) {
                    attribute.setAttributeLevel(ModAttributes.values()[i], 1);
                }

                Requirement[] requirements = ModClasses.getClassByName(msg.modClass).statsBonus;
                for(int i = 0; i < requirements.length; i++) {
                    attribute.setAttributeLevel(requirements[i].modAttributes, requirements[i].level + 1);
                    PacketHandler.sendToPlayer(new ModAttributeMsg(attribute.serializeNBT()), player);
                }
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
