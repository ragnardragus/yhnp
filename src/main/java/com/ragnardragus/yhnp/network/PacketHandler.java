package com.ragnardragus.yhnp.network;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.network.attributes.ModAttributeMsg;
import com.ragnardragus.yhnp.network.attributes.UpgradeAttributeMsg;
import com.ragnardragus.yhnp.network.attributes.WarningMsg;
import com.ragnardragus.yhnp.network.classes.ChangeClassMsg;
import com.ragnardragus.yhnp.network.classes.ModClassMsg;
import com.ragnardragus.yhnp.network.levels.LevelUpMsg;
import com.ragnardragus.yhnp.network.levels.McLevelsNeedMsg;
import com.ragnardragus.yhnp.network.levels.ModLevelsMsg;
import com.ragnardragus.yhnp.network.levels.SkillPointMsg;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class PacketHandler {


    private static SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Yhnp.MOD_ID, "messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {

        INSTANCE.registerMessage(id(), ModAttributeMsg.class,
                ModAttributeMsg::encode,
                ModAttributeMsg::decode,
                ModAttributeMsg.Handler::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        INSTANCE.registerMessage(id(), UpgradeAttributeMsg.class,
                UpgradeAttributeMsg::encode,
                UpgradeAttributeMsg::decode,
                UpgradeAttributeMsg.Handler::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        INSTANCE.registerMessage(id(), WarningMsg.class,
                WarningMsg::encode,
                WarningMsg::decode,
                WarningMsg.Handler::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        INSTANCE.registerMessage(id(), LevelUpMsg.class,
                LevelUpMsg::encode,
                LevelUpMsg::decode,
                LevelUpMsg.Handler::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        INSTANCE.registerMessage(id(), SkillPointMsg.class,
                SkillPointMsg::encode,
                SkillPointMsg::decode,
                SkillPointMsg.Handler::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        INSTANCE.registerMessage(id(), ModLevelsMsg.class,
                ModLevelsMsg::encode,
                ModLevelsMsg::decode,
                ModLevelsMsg.Handler::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        INSTANCE.registerMessage(id(), McLevelsNeedMsg.class,
                McLevelsNeedMsg::encode,
                McLevelsNeedMsg::decode,
                McLevelsNeedMsg.Handler::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        INSTANCE.registerMessage(id(), ModClassMsg.class,
                ModClassMsg::encode,
                ModClassMsg::decode,
                ModClassMsg.Handler::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        INSTANCE.registerMessage(id(), ChangeClassMsg.class,
                ChangeClassMsg::encode,
                ChangeClassMsg::decode,
                ChangeClassMsg.Handler::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity) {
        sendToPlayersTrackingEntity(message, entity, false);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity, boolean sendToSource) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
        if (sendToSource && entity instanceof ServerPlayer serverPlayer)
            sendToPlayer(message, serverPlayer);
    }
}
