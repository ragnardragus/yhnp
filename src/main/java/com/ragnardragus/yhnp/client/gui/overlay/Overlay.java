package com.ragnardragus.yhnp.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ragnardragus.yhnp.Config;
import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.compat.GameStageCompat;
import com.ragnardragus.yhnp.requirement.Requirement;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ragnardragus.yhnp.client.ClientUtils.*;

@Deprecated
@Mod.EventBusSubscriber(modid = Yhnp.MOD_ID, value = Dist.CLIENT)
public class Overlay {

    private static int showTicks = 0;

    private static List<Requirement> attributesReq;
    private static String stageReq;
    private static boolean arrowReq;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onHudRender(CustomizeGuiOverlayEvent.DebugText event) {
        GuiGraphics stack = event.getGuiGraphics();

        if (isClientPlayerPresent() && showTicks > 0) {
            int cx = CLIENT.getWindow().getGuiScaledWidth() / 2;
            int cy = CLIENT.getWindow().getGuiScaledHeight() / 4;

            stack.pose().pushPose();
            RenderSystem.enableBlend();


            stack.blit(SKILLS_RESOURCE, cx - 71, cy - 4, 0, 196, 142, 52);

            renderRequirementsMessage(stack, cx, cy);
            renderAttributesRequirements(stack, cx, cy);
            renderStageRequirements(stack, cx, cy);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            stack.pose().popPose();

            RenderSystem.setShaderTexture(0, MC_ICONS);
        }
    }


    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (showTicks > 0) showTicks--;
    }

    public static void showWarning(ResourceLocation resource) {
        Requirement[] requirementsArray = Yhnp.requirementsJsonListener.getRequirements(resource);
        attributesReq = requirementsArray != null ? Arrays.asList(requirementsArray) : new ArrayList<>();

        stageReq = Yhnp.requirementsJsonListener.getRequirementStageName(resource);
        arrowReq = Yhnp.requirementsJsonListener.isArrow(resource);

        showTicks = 90;
    }

    private static void renderRequirementsMessage(GuiGraphics stack, int cx, int cy) {
        Component message = Component.translatable(arrowReq ? "overlay.message.arrow": "overlay.message");
        stack.drawString(CLIENT.font, message.getString(), cx - CLIENT.font.width(message.getString()) / 2f, cy, 0xFF5555, true);
    }

    private static void renderAttributesRequirements(GuiGraphics stack, int cx, int cy) {
        if(validateAttributes()) {
            for (int i = 0; i < attributesReq.size(); i++) {
                Requirement requirement = attributesReq.get(i);

                int maxLevel = Config.MAX_ATTRIBUTE_LEVEL.get();
                int x = cx + i * 20 - attributesReq.size() * 10 + 2;
                int y = cy + 15;
                int u = Math.min(requirement.level, maxLevel - 1) / (maxLevel / 4) * 16 + 176;
                int v = requirement.modAttributes.index * 16 + 128;

                stack.blit(SKILLS_RESOURCE, x, y, u, v, 16, 16);

                String level = Integer.toString(requirement.level);
                boolean met = ModAttribute.Implementation.get().getAttributeLevel(requirement.modAttributes) >= requirement.level;
                stack.drawString(CLIENT.font, level, x + 17 - CLIENT.font.width(level), y + 9, met ? 0x55FF55 : 0xFF5555, true);
            }
        }
    }

    private static boolean validateAttributes() {
        return CLIENT.player.getCapability(ModAttribute.AttributeCapability.INSTANCE).isPresent()
                && attributesReq != null && !attributesReq.isEmpty();
    }

    private static void renderStageRequirements(GuiGraphics stack, int cx, int cy) {
        if(GameStageCompat.isLoaded()) {
            if (stageReq != null && !stageReq.isEmpty() && !GameStageHelper.hasStage(CLIENT.player, stageReq)) {
                Component stageMessage = Component.translatable("overlay.stage_requirement", stageReq);
                stack.drawString(CLIENT.font, stageMessage.getString(), cx - CLIENT.font.width(stageMessage) / 2f, cy + (attributesReq.isEmpty() ? 24 : 34), 0xFF5555, true);
            }
        }
    }
}
