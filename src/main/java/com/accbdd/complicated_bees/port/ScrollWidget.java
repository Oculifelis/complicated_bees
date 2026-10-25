package com.accbdd.complicated_bees.port;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.network.chat.Component;

/** The old content callback expressed through the current scroll-area lifecycle. */
public abstract class ScrollWidget extends AbstractScrollArea {
    protected ScrollWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message, defaultSettings(10));
    }
    protected abstract int getInnerHeight();
    protected abstract void renderContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick);
    protected void renderBorder(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {}
    @Override protected int contentHeight() { return getInnerHeight(); }
    public int getMaxScrollAmount() { return maxScrollAmount(); }
    protected void renderDecorations(GuiGraphicsExtractor graphics) {}
    protected int innerPadding() { return 4; }
    @Override protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        renderBorder(graphics, getX(), getY(), getWidth(), getHeight());
        graphics.enableScissor(getX(), getY(), getRight(), getBottom());
        graphics.pose().pushMatrix();
        graphics.pose().translate(0, (float)-scrollAmount());
        renderContents(graphics, mouseX, mouseY, partialTick);
        graphics.pose().popMatrix();
        graphics.disableScissor();
        extractScrollbar(graphics, mouseX, mouseY);
    }
}
