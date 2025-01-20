package com.ragnardragus.yhnp.client.gui.screen.widgets;

import com.ragnardragus.yhnp.client.ClientUtils;
import com.ragnardragus.yhnp.client.gui.screen.SkillScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;

public class TabButton extends Button {

    private final boolean selected;
    private final TabType type;

    public TabButton(int x, int y, TabType type, boolean selected, OnPress onPress) {
        super(x, y, 28, 30, Component.empty(), onPress, narrator -> narrator.get());

        this.type = type;
        this.selected = selected;
    }


    @Override
    public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {

        Minecraft minecraft = Minecraft.getInstance();
        active = !(minecraft.screen instanceof InventoryScreen) || !((InventoryScreen) minecraft.screen).getRecipeBookComponent().isVisible();

        if (active) {
            stack.pose().pushPose();

            stack.blit(ClientUtils.SKILLS_RESOURCE, getX(), getY(), selected ? 11 : 39, 166, width, height);
            stack.blit(ClientUtils.SKILLS_RESOURCE, getX() + 6, getY() + (selected ? 6 : 8), 240, 128 + type.iconIndex * 16, 16, 16);
        }
    }

    @Override
    public void onPress() {
        Minecraft minecraft = Minecraft.getInstance();
        switch (type) {
            case INVENTORY -> minecraft.setScreen(new InventoryScreen(minecraft.player));
            case SKILLS -> minecraft.setScreen(new SkillScreen());
        }
    }

    public enum TabType {
        INVENTORY (0),
        SKILLS (1),
        TRAITS(2),
        PLAYER_CLASS(3);

        public final int iconIndex;

        TabType(int index) {
            iconIndex = index;
        }
    }
}
