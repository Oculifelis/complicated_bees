package com.accbdd.complicated_bees.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class GuiHelper {
    /**
     * draws a number of components as wrapped paragraphs, with each component getting spaced by LINE_HEIGHT / 2
     *
     * @param graphics   a GuiGraphics
     * @param x          the x coordinate of the top left line
     * @param y          the y coordinate of the top left line
     * @param color      a color for text
     * @param lineHeight the height of a line
     * @param maxWidth   the maximum width of each line
     * @param padding    the padding between maxWidth and the actual width of each line
     * @param components a number of components
     * @return a y coordinate for the next line of text, spaced accordingly
     */
    public static int drawWrappedText(GuiGraphics graphics, int x, int y, int color, int lineHeight, int maxWidth, int padding, Component... components) {
        int curY = y;
        for (Component component : components) {
            String[] linebroken = component.getString().split("\\r?\\n");
            for (String prewrap : linebroken) {
                List<FormattedCharSequence> lines = Minecraft.getInstance().font.split(Component.literal(prewrap).withStyle(component.getStyle()), maxWidth - padding * 2);
                for (FormattedCharSequence line : lines) {
                    graphics.drawString(Minecraft.getInstance().font, line, x, curY, color);
                    curY += lineHeight;
                }
            }
            curY += lineHeight / 2;
        }
        return curY;
    }

    public static int drawCenteredWrappedText(GuiGraphics graphics, int x, int y, int color, int lineHeight, int maxWidth, int padding, Component... components) {
        int curY = y;
        for (Component component : components) {
            String[] linebroken = component.getString().split("\\r?\\n");
            for (String prewrap : linebroken) {
                List<FormattedCharSequence> lines = Minecraft.getInstance().font.split(Component.literal(prewrap).withStyle(component.getStyle()), maxWidth - padding * 2);
                for (FormattedCharSequence line : lines) {
                    graphics.drawCenteredString(Minecraft.getInstance().font, line, x, curY, color);
                    curY += lineHeight;
                }
            }
            curY += lineHeight / 2;
        }
        return curY;
    }
}
