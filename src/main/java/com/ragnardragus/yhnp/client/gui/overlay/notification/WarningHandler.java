package com.ragnardragus.yhnp.client.gui.overlay.notification;

import com.ragnardragus.yhnp.Yhnp;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(modid = Yhnp.MOD_ID, value = Dist.CLIENT)
public class WarningHandler {

    private static int toastDelayTicks = 0;
    private static final List<ResourceLocation> queueRequirement = new ArrayList<>();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (toastDelayTicks > 0) {
            toastDelayTicks--;
        } else {
            queueRequirement.clear();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void showWarning(ResourceLocation resource) {
        NotificationToastData toastData = new NotificationToastData(resource);

        var mc = Minecraft.getInstance();

        if(toastDelayTicks <= 0 || !queueRequirement.contains(resource)) {
            if(toastData.requirements.length > 0) {
                mc.getToasts().addToast(new NotificationToast(mc, toastData.getDefaultToast(), toastData.getDefaultIcon()));
            }

            if(toastData.stage != null && !toastData.stage.isEmpty()) {
                mc.getToasts().addToast(new NotificationToast(mc, toastData.getStageToast(), toastData.getStageIcon()));
            }

            queueRequirement.add(resource);
            toastDelayTicks = 90;
        }
    }
}
