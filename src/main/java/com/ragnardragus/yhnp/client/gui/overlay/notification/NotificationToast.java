package com.ragnardragus.yhnp.client.gui.overlay.notification;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class NotificationToast implements Toast {

    private final long duration;
    private final List<FormattedCharSequence> message;
    private final int SPLIT_WIDTH = 240;

    private long lastChanged;
    private boolean changed;
    private final ResourceLocation toastIcon;

    public NotificationToast(Minecraft mc, Component text, ResourceLocation toastIcon) {
        this.duration = 1000 * 3;
        this.message = new ArrayList<>(2);
        this.toastIcon = toastIcon;

        if (!isEmpty(text)) {
            this.message.addAll(mc.font.split(text, SPLIT_WIDTH));
        }
    }

    @Override
    public @NotNull Visibility render(@NotNull GuiGraphics graphics, @NotNull ToastComponent toastComponent, long l) {

        if (this.changed) {
            this.lastChanged = l;
            this.changed = false;
        }

        var mc = toastComponent.getMinecraft();

        var poseStack = graphics.pose();

        poseStack.pushPose();
        poseStack.translate(-2D, 2D, 0D);
        var m = poseStack.last().pose();
        int w = 160;
        int h = 32;

        int oc = 0x1a1e1d;
        int ocr = FastColor.ARGB32.red(oc);
        int ocg = FastColor.ARGB32.green(oc);
        int ocb = FastColor.ARGB32.blue(oc);

        int bc = 0x42413f;
        int bcr = FastColor.ARGB32.red(bc);
        int bcg = FastColor.ARGB32.green(bc);
        int bcb = FastColor.ARGB32.blue(bc);

        int bgc = 0x1a1e1d;
        int bgcr = FastColor.ARGB32.red(bgc);
        int bgcg = FastColor.ARGB32.green(bgc);
        int bgcb = FastColor.ARGB32.blue(bgc);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        drawRectangle(m, 2, 0, w - 2, h, ocr, ocg, ocb);
        drawRectangle(m, 0, 2, w, h - 2, ocr, ocg, ocb);
        drawRectangle(m, 1, 1, w - 1, h - 1, ocr, ocg, ocb);
        drawRectangle(m, 2, 1, w - 2, h - 1, bcr, bcg, bcb);
        drawRectangle(m, 1, 2, w - 1, h - 2, bcr, bcg, bcb);
        drawRectangle(m, 2, 2, w - 2, h - 2, bgcr, bgcg, bgcb);

        drawIconTexture(toastIcon, graphics, 12, h/2, 16);

        int th = 24;
        int tv = (h - message.size() * 10) / 2 + 1;

        for (var i = 0; i < message.size(); i++) {
            graphics.drawString(mc.font, message.get(i), th, tv + i * 10, 0xFFFFFF, true);
        }

        poseStack.popPose();
        return l - this.lastChanged < duration ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }

    private void drawRectangle(Matrix4f m, int x0, int y0, int x1, int y1, int r, int g, int b) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buf = tesselator.getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        buf.vertex(m, x0, y1, 0F).color(r, g, b, 255).endVertex();
        buf.vertex(m, x1, y1, 0F).color(r, g, b, 255).endVertex();
        buf.vertex(m, x1, y0, 0F).color(r, g, b, 255).endVertex();
        buf.vertex(m, x0, y0, 0F).color(r, g, b, 255).endVertex();
        BufferUploader.drawWithShader(buf.end());
    }

    private static boolean isEmpty(Component component) {
        return component.getContents() == ComponentContents.EMPTY && component.getSiblings().isEmpty();
    }

    private void drawItemIcon(ItemStack stack, GuiGraphics graphics, int x, int y, int size) {
        var m = RenderSystem.getModelViewStack();
        m.pushPose();
        m.translate(x - 2D, y + 2D, 0D);
        float s = size / 16F;
        m.scale(s, s, s);
        RenderSystem.applyModelViewMatrix();
        graphics.renderFakeItem(stack, -8, -8);
        m.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private void drawIconTexture(ResourceLocation texture, GuiGraphics graphics, int x, int y, int size) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        var m = graphics.pose().last().pose();

        int p0 = -size / 2;
        int p1 = p0 + size;

        var buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buf.vertex(m, x + p0, y + p1, 1F).uv(0F, 1F).color(255, 255, 255, 255).endVertex();
        buf.vertex(m, x + p1, y + p1, 1F).uv(1F, 1F).color(255, 255, 255, 255).endVertex();
        buf.vertex(m, x + p1, y + p0, 1F).uv(1F, 0F).color(255, 255, 255, 255).endVertex();
        buf.vertex(m, x + p0, y + p0, 1F).uv(0F, 0F).color(255, 255, 255, 255).endVertex();
        BufferUploader.drawWithShader(buf.end());    }
}