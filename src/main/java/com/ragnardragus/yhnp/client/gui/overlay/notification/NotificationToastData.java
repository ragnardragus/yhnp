package com.ragnardragus.yhnp.client.gui.overlay.notification;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.client.ClientUtils;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.requirement.Requirement;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class NotificationToastData {

    public Requirement[] requirements;
    public String stage;
    private ToastIconType defaultIcon;
    private final TitleMessageType messageType;

    public NotificationToastData(ResourceLocation skillData) {
        this.requirements = Yhnp.requirementsJsonListener.getRequirements(skillData);
        this.stage = Yhnp.requirementsJsonListener.getRequirementStageName(skillData);
        var isProjectile = Yhnp.requirementsJsonListener.isArrow(skillData);
        var isSpell = Yhnp.requirementsJsonListener.isSpell(skillData);

        defaultIcon = ToastIconType.DEFAULT;

        if(isProjectile) {
            messageType = TitleMessageType.ARROW;
        } else if (isSpell) {
            messageType = TitleMessageType.SPELL;
            defaultIcon = ToastIconType.SPELL;
        } else {
            messageType = TitleMessageType.DEFAULT;
        }
    }

    public Component getDefaultToast() {
        return new NotificationToastData.RequirementComponentBuilder()
                .setTitle(getTitleMessage())
                .setRequireText("overlay.message.require")
                .setRequirements(requirements)
                .build();
    }

    public Component getStageToast() {
        return new NotificationToastData.StageComponentBuilder()
                .setTitle(getTitleMessage())
                .setRequireText("overlay.stage_requirement")
                .setStage(stage)
                .build();
    }

    public ResourceLocation getDefaultIcon() {
        return defaultIcon.iconResource;
    }

    public ResourceLocation getStageIcon() {
        return ToastIconType.STAGE.iconResource;
    }

    public String getTitleMessage() {
        return messageType.message;
    }

    enum TitleMessageType {
        ARROW ("overlay.message.arrow"),
        SPELL ("overlay.message.spell"),
        DEFAULT ("overlay.message");

        final String message;

        TitleMessageType(String message) {
            this.message = message;
        }
    }

    enum ToastIconType {
        STAGE (ClientUtils.STAGE_ICON),
        SPELL (ClientUtils.INK_ICON),
        DEFAULT (ClientUtils.BOOK_ICON);

        final ResourceLocation iconResource;

        ToastIconType(ResourceLocation iconResource) {
            this.iconResource = iconResource;
        }
    }

    public static class RequirementComponentBuilder {
        private Component title;
        private Component requireText;
        private Requirement[] requirements;

        private final MutableComponent finalMessage = Component.empty();
        private final MutableComponent requirementsMessage = Component.empty();

        public NotificationToastData.RequirementComponentBuilder setTitle(String title) {
            this.title = Component.translatable(title).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD);
            return this;
        }

        public NotificationToastData.RequirementComponentBuilder setRequireText(String requireText) {
            this.requireText = Component.translatable(requireText).withStyle(ChatFormatting.WHITE);
            return this;
        }

        public NotificationToastData.RequirementComponentBuilder setRequirements(Requirement[] requirements) {
            this.requirements = requirements;
            return this;
        }

        public MutableComponent build() {
            if(title == null || requireText == null || requirements == null) return Component.empty();

            requirementsMessage.append(requireText);

            for (Requirement requirement : requirements) {
                boolean met =
                        ModAttribute.Implementation.get().getAttributeLevel(requirement.modAttributes) >= requirement.level;
                var color = met ? ChatFormatting.GREEN : ChatFormatting.RED;
                requirementsMessage.append(Component.literal(requirement.modAttributes.abbreviation.toUpperCase() +
                        ":").withStyle(ChatFormatting.BOLD)).append(Component.literal(" " + requirement.level + " ").withStyle(color).withStyle(ChatFormatting.BOLD));
            }

            return finalMessage.append(title).append(requirementsMessage);
        }
    }

    public static class StageComponentBuilder {
        private Component title;
        private Component requireText;
        private Component stageName;

        private final MutableComponent finalMessage = Component.empty();
        private final MutableComponent requirementsMessage = Component.empty();

        public NotificationToastData.StageComponentBuilder setTitle(String title) {
            this.title = Component.translatable(title).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD);
            return this;
        }

        public NotificationToastData.StageComponentBuilder setRequireText(String requireText) {
            this.requireText = Component.translatable(requireText).withStyle(ChatFormatting.WHITE);
            return this;
        }

        public NotificationToastData.StageComponentBuilder setStage(String stage) {
            this.stageName = Component.literal(stage + " ").withStyle(ChatFormatting.BOLD);
            return this;
        }


        public MutableComponent build() {
            if(title == null || requireText == null || stageName == null) return Component.empty();

            requirementsMessage.append(stageName).append(requireText);
            return finalMessage.append(title).append(requirementsMessage);
        }
    }
}
