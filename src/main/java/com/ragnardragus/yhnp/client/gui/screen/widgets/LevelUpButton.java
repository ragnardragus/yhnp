package com.ragnardragus.yhnp.client.gui.screen.widgets;

import com.ragnardragus.yhnp.capability.level.ModLevel;
import com.ragnardragus.yhnp.network.PacketHandler;
import com.ragnardragus.yhnp.network.levels.LevelUpMsg;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

import static com.ragnardragus.yhnp.client.ClientUtils.*;

public class LevelUpButton extends Button {

    private boolean canLevelUp = false;

    public LevelUpButton(int x, int y) {
        super(x, y, 14, 14, Component.empty(), button -> {}, narrator -> narrator.get());
    }

    @Override
    public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
        stack.blit(SKILLS_RESOURCE, getX(), getY(), 145, canLevelUp ? 240 : 226, 14, 14);

        renderToolTip(stack, mouseX, mouseY);
    }

    @Override
    public void onPress() {
        if(canLevelUp) {
            PacketHandler.sendToServer(new LevelUpMsg(true));
        }
    }

    public void renderToolTip(GuiGraphics stack, int mouseX, int mouseY) {

        CLIENT.player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(levelInstance -> {
            int playerExperience = CLIENT.player.experienceLevel;
            canLevelUp = playerExperience >= levelInstance.getMcLevelsNeed();

            if(isMouseOver(mouseX, mouseY)) {

                Component levelComponent = Component.translatable("yhnp.screen.level_label"); //, levelInstance.getPlayerModLevel()
                Component reqComponent = Component.translatable("yhnp.screen.req_label",
                        Component.literal(String.valueOf(levelInstance.getMcLevelsNeed())).withStyle(canLevelUp ? ChatFormatting.GREEN : ChatFormatting.RED),
                        Component.literal(String.valueOf(playerExperience)).withStyle(ChatFormatting.WHITE));

                drawToolTip(stack, Arrays.asList(levelComponent, reqComponent), mouseX, mouseY);
            }
        });
    }
}
