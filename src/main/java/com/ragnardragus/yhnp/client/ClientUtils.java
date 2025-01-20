package com.ragnardragus.yhnp.client;

import com.ragnardragus.yhnp.Yhnp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public class ClientUtils {

    public static Minecraft CLIENT = Minecraft.getInstance();

    public static final ResourceLocation SKILLS_RESOURCE =
            new ResourceLocation(Yhnp.MOD_ID, "textures/gui/skills.png");

    public static final ResourceLocation MC_ICONS =
            new ResourceLocation("textures/gui/icons.png");

    public static final ResourceLocation BOOK_ICON =
            new ResourceLocation(Yhnp.MOD_ID, "textures/gui/icons/book_icon.png");

    public static final ResourceLocation STAGE_ICON =
            new ResourceLocation(Yhnp.MOD_ID, "textures/gui/icons/stage_icon.png");

    public static final ResourceLocation INK_ICON =
            new ResourceLocation(Yhnp.MOD_ID, "textures/gui/icons/ink_icon.png");

    public static void drawToolTip(GuiGraphics stack, Component tooltip, int mouseX, int mouseY) {
        stack.renderTooltip(CLIENT.font, tooltip, mouseX, mouseY);
    }

    public static void drawToolTip(GuiGraphics stack, List<Component> tooltip, int mouseX, int mouseY) {
        stack.renderTooltip(CLIENT.font, tooltip, Optional.empty(), mouseX, mouseY);
    }

    public static boolean isClientPlayerPresent() {
        return CLIENT.level != null && CLIENT.player != null;
    }
}
