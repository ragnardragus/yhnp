package com.ragnardragus.yhnp;

import com.mojang.logging.LogUtils;
import com.ragnardragus.yhnp.capability.classes.ModClass;
import com.ragnardragus.yhnp.client.ClientConfig;
import com.ragnardragus.yhnp.client.KeysHandler;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.capability.level.ModLevel;
import com.ragnardragus.yhnp.commands.ModCommands;
import com.ragnardragus.yhnp.compat.CompatHandler;
import com.ragnardragus.yhnp.network.PacketHandler;
import com.ragnardragus.yhnp.requirement.serialization.RequirementsJsonListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(Yhnp.MOD_ID)
public class Yhnp {

    public static final String MOD_ID = "yhnp";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String RESOURCE_PREFIX = MOD_ID + ":";

    public static RequirementsJsonListener requirementsJsonListener;

    public Yhnp() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.CLIENT, ClientConfig.SPEC, String.format("%s-client.toml", Yhnp.MOD_ID));

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        if (FMLEnvironment.dist == Dist.CLIENT)
            modEventBus.addListener(KeysHandler::initKeyBinds);

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.SERVER, Config.SPEC, String.format("%s-server.toml", Yhnp.MOD_ID));

        CompatHandler.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("[You Have No Power]: Registering Network Stuff");
        PacketHandler.register();
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void attachCapabilitiesEntity(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(ModAttribute.Provider.IDENTIFIER, new ModAttribute.Provider());
            event.addCapability(ModLevel.Provider.IDENTIFIER, new ModLevel.Provider());
            event.addCapability(ModClass.Provider.IDENTIFIER, new ModClass.Provider());
        }
    }
}
