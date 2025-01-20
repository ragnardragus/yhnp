package com.ragnardragus.yhnp.events;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.api.LevelUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Yhnp.MOD_ID)
public class TestEventListener {

    @SubscribeEvent
    public static void onLevelUpEvent(LevelUpEvent event) {
        Yhnp.LOGGER.info("Previous LEVEL {}", event.getOldLevel());
        Yhnp.LOGGER.info("Current LEVEL {}", event.getNewLevel());
    }
}
