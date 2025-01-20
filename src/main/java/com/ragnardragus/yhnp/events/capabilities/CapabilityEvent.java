package com.ragnardragus.yhnp.events.capabilities;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.capability.classes.ModClass;
import com.ragnardragus.yhnp.capability.level.ModLevel;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Yhnp.MOD_ID)
public class CapabilityEvent {
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(ModAttribute.Implementation.class);
        event.register(ModLevel.class);
        event.register(ModClass.class);
    }
}
