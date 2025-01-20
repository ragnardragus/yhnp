package com.ragnardragus.yhnp.network.attributes;

import com.ragnardragus.yhnp.Config;
import com.ragnardragus.yhnp.api.AttributeUpgradeEvent;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.capability.attributes.ModAttributes;
import com.ragnardragus.yhnp.capability.level.ModLevel;
import com.ragnardragus.yhnp.compat.kubejs.ModJSEvents;
import com.ragnardragus.yhnp.compat.kubejs.events.AttributeUpgradeEventJS;
import com.ragnardragus.yhnp.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpgradeAttributeMsg {

    private final int skill;

    public UpgradeAttributeMsg(int skill) {
        this.skill = skill;
    }

    public static UpgradeAttributeMsg decode(FriendlyByteBuf buffer) {
        int skill = buffer.readInt();
        return new UpgradeAttributeMsg(skill);
    }

    public static void encode(UpgradeAttributeMsg msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.skill);
    }


    public static class Handler {
        public static void handle(UpgradeAttributeMsg msg, Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                ServerPlayer player = context.get().getSender();

                if(player != null) {
                    ModAttribute attributeData = ModAttribute.Implementation.get(player);
                    ModAttributes attribute = ModAttributes.values()[msg.skill];

                    player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(levelData -> {
                        int skillPoints = levelData.getSkillPoints();
                        var attributeLevel = ModAttribute.Implementation.get(player).getAttributeLevel(attribute);
                        if (skillPoints > 0 && attributeLevel < Config.MAX_ATTRIBUTE_LEVEL.get()) {

                            var attributeEvent = new AttributeUpgradeEvent(player, attribute, attributeLevel, attributeLevel + 1);
                            MinecraftForge.EVENT_BUS.post(attributeEvent);

                            ModJSEvents.ATTR_UP.post(new AttributeUpgradeEventJS(player, attribute, attributeLevel, attributeLevel + 1));

                            attributeData.increaseAttributeLevel(attribute);
                            levelData.subtractSkillPoints();

                            PacketHandler.sendToPlayer(new ModAttributeMsg(ModAttribute.Implementation.get(player).serializeNBT()), player);
                            levelData.sync(player);
                        }
                    });
                }
            });

            context.get().setPacketHandled(true);
        }
    }
}
