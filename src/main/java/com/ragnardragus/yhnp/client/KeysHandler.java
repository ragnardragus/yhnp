package com.ragnardragus.yhnp.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.client.gui.screen.SkillScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Yhnp.MOD_ID, value = Dist.CLIENT)
public class KeysHandler {

    public static final Lazy<KeyMapping> OPEN_STATS_MENU = Lazy.of(() -> new KeyMapping("key.yhnp.open_stats_menu",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, "key.yhnp.category"));

    public static void initKeyBinds(RegisterKeyMappingsEvent event) {
        event.register(OPEN_STATS_MENU.get());
    }

    @SubscribeEvent
    public static void handleKeys(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.screen == null && event.phase == TickEvent.Phase.END) {
            if(OPEN_STATS_MENU.get().isDown()) {
                mc.setScreen(new SkillScreen());
            }
        }
    }
}
