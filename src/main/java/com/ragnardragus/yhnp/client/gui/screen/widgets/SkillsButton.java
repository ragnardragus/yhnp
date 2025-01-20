package com.ragnardragus.yhnp.client.gui.screen.widgets;

import com.ragnardragus.yhnp.Config;
import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.capability.attributes.ModAttributes;
import com.ragnardragus.yhnp.capability.level.ModLevel;
import com.ragnardragus.yhnp.network.PacketHandler;
import com.ragnardragus.yhnp.network.attributes.UpgradeAttributeMsg;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

import static com.ragnardragus.yhnp.client.ClientUtils.*;

public class SkillsButton extends Button {

    private final ModAttributes srAttributes;

    public SkillsButton(int x, int y, ModAttributes srAttributes) {
        super(x, y, 79, 32, Component.empty(), button -> {}, narrator -> narrator.get());

        this.srAttributes = srAttributes;
    }

    @Override
    public void render(GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {

        int level = ModAttribute.Implementation.get().getAttributeLevel(srAttributes);
        int maxLevel = Config.MAX_ATTRIBUTE_LEVEL.get();

        int u = ((int) Math.ceil((double) level * 4 / maxLevel) - 1) * 16 + 176;
        int v = srAttributes.index * 16 + 128;

        stack.blit(SKILLS_RESOURCE, getX(), getY(), 176, (level == maxLevel ? 64 : 0) + (isMouseOver(mouseX, mouseY) ? 32 : 0), width, height);
        stack.blit(SKILLS_RESOURCE, getX() + 6, getY() + 8, u, v, 16, 16);

        if(CLIENT.player != null) {
            CLIENT.player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(playerLevel -> {
                if (playerLevel.getSkillPoints() > 0 && level < maxLevel) {
                    stack.blit(SKILLS_RESOURCE, getX() + 62, getY() + 18, 0, 168, 11, 12);
                }
            });
        }

        int attrAbbreviationSize = Minecraft.getInstance().font.width(srAttributes.abbreviation);
        stack.drawString(CLIENT.font, Component.literal(srAttributes.abbreviation.toUpperCase()).withStyle(ChatFormatting.BOLD), getX() + 40 - attrAbbreviationSize / 2, getY() + 7, 0xFFFFFF);

        String levelInfo = level + "/" + maxLevel;
        int levelInfoSize = Minecraft.getInstance().font.width(levelInfo);
        stack.drawString(CLIENT.font, level + "/" + maxLevel, getX() + 40 - levelInfoSize / 2, getY() + 18, 0xBEBEBE);

        if(isMouseOver(mouseX, mouseY)) {
            drawToolTip(stack, Arrays.asList(Component.translatable(srAttributes.displayName)), mouseX, mouseY + 8);
        }
    }

    @Override
    public void onPress() {
        PacketHandler.sendToServer(new UpgradeAttributeMsg(srAttributes.index));
    }
}
