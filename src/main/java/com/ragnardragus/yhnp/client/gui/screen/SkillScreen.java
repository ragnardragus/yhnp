package com.ragnardragus.yhnp.client.gui.screen;

import com.ragnardragus.yhnp.Config;
import com.ragnardragus.yhnp.client.ClientUtils;
import com.ragnardragus.yhnp.client.gui.screen.widgets.LevelUpButton;
import com.ragnardragus.yhnp.client.gui.screen.widgets.SkillsButton;
import com.ragnardragus.yhnp.capability.attributes.ModAttributes;
import com.ragnardragus.yhnp.capability.level.ModLevel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public class SkillScreen extends Screen {
    public SkillScreen() {
        super(Component.translatable("yhnp.screen.skills"));
    }

    @Override
    protected void init() {

        int left = (width - 162) / 2;
        int top = (height - 112) / 2; // - 8px

        for (int i = 0; i < 8; i++) {
            int x = left + i % 2 * 81; // -2px
            int y = top + i / 2 * 34; // -2px

            addRenderableWidget(new SkillsButton(x, y, ModAttributes.values()[i]));
        }

        if(Config.LEVEL_UP_SYSTEM.get())
            addRenderableWidget(new LevelUpButton(left, top - 18));
    }

    @Override
    public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {

        int x = (width - 176) / 2;
        int y = (height - 166) / 2;

        renderBackground(stack);
        renderTitle(stack, x, y);
        if(Config.LEVEL_UP_SYSTEM.get()) renderLevelLabels(stack, x, y);

        stack.pose().pushPose();
        stack.blit(ClientUtils.SKILLS_RESOURCE, x, y, 0, 0, 176, 166);

        if(Config.LEVEL_UP_SYSTEM.get()) {
            stack.blit(ClientUtils.SKILLS_RESOURCE, x + 147, y + 10, 108, 167, 20, 12);
            stack.blit(ClientUtils.SKILLS_RESOURCE, x + 21, y + 10, 108, 167, 20, 12);
        }

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int key, int p_96553_, int p_96554_) {
        if(key == GLFW.GLFW_KEY_E) {
            this.onClose();
            return true;
        }
        return super.keyPressed(key, p_96553_, p_96554_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected void renderTitle(GuiGraphics stack, int x, int y) {
        int size = font.width(this.title) / 2;
        stack.drawString(minecraft.font, this.title, width / 2 - size, y + 6, 0x3F3F3F);
    }

    protected void renderLevelLabels(GuiGraphics stack, int x, int y) {
        Player player = minecraft.player;

        player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(skillPoint -> {
            String skillPointString = String.valueOf(skillPoint.getSkillPoints());
            stack.drawString(minecraft.font, skillPointString, width /2 - font.width(skillPointString) / 2 + 70, y + 12, 0x00C800);

            String levelString = String.valueOf(skillPoint.getPlayerModLevel());
            stack.drawString(minecraft.font, levelString, width /2 - font.width(levelString) / 2 - 57, y + 12, 0xFFFFFF);
        });
    }
}
