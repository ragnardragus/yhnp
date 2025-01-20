package com.ragnardragus.yhnp.client.gui.overlay;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.compat.GameStageCompat;
import com.ragnardragus.yhnp.requirement.Requirement;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = Yhnp.MOD_ID, value = Dist.CLIENT)
public class Tooltip {

    @SubscribeEvent
    public static void onTooltipDisplay(ItemTooltipEvent event) {
        if (Minecraft.getInstance().player != null) {

            List<Component> tooltips = event.getToolTip();
            ResourceLocation rsItem = ForgeRegistries.ITEMS.getKey(event.getItemStack().getItem());

            Requirement[] requirements = Yhnp.requirementsJsonListener.getRequirements(rsItem);
            String stage = Yhnp.requirementsJsonListener.getRequirementStageName(rsItem);

            if ((requirements != null && requirements.length > 0) || (stage != null && !stage.isEmpty())) {
                renderRequirementLabel(tooltips);
                renderAttributesRequirements(tooltips, requirements);
                renderStageRequirements(tooltips, stage);
            }
        }
    }

    private static void renderRequirementLabel(List<Component> tooltips) {
        tooltips.add(Component.empty());
        tooltips.add(Component.translatable("tooltip.requirements").withStyle(ChatFormatting.GRAY));
    }

    private static void renderAttributesRequirements(List<Component> tooltips, Requirement[] requirements) {
        if (requirements != null) {
            for (Requirement requirement : requirements) {
                ChatFormatting colour = ModAttribute.Implementation.get().getAttributeLevel(requirement.modAttributes) >= requirement.level ? ChatFormatting.GREEN : ChatFormatting.RED;
                tooltips.add(Component.translatable(requirement.modAttributes.displayName).append(": " + requirement.level).withStyle(colour));
            }
        }
    }

    private static void renderStageRequirements(List<Component> tooltips, String stage) {
        if (stage != null && GameStageCompat.isLoaded()) {
            boolean hasStage = GameStageHelper.hasStage(Minecraft.getInstance().player, stage);
            ChatFormatting colour = hasStage ? ChatFormatting.GREEN : ChatFormatting.RED;
            tooltips.add(Component.translatable("tooltip.stage_requirements", stage).withStyle(colour));
        }
    }
}
