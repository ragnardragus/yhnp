package com.ragnardragus.yhnp.client.gui.screen.event;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.client.ClientConfig;
import com.ragnardragus.yhnp.client.gui.screen.SkillScreen;
import com.ragnardragus.yhnp.client.gui.screen.widgets.TabButton;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Yhnp.MOD_ID, value = Dist.CLIENT)
public class ScreenHandler {

    @SubscribeEvent
    public static void onInitScreen(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();

        if ((screen instanceof InventoryScreen || screen instanceof SkillScreen) && ClientConfig.RENDER_TAB_BUTTONS.get()) {

            boolean inventoryOpen = screen instanceof InventoryScreen;
            boolean skillsOpen = screen instanceof SkillScreen;

            int x = (screen.width  / 2) - 86;
            int y = (screen.height / 2) - 110;

            event.addListener(new TabButton(x, y, TabButton.TabType.INVENTORY, inventoryOpen, button -> {}));
            event.addListener(new TabButton(x + 28, y, TabButton.TabType.SKILLS, skillsOpen, button -> {}));
        }

    }
}
