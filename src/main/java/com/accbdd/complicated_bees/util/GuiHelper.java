package com.accbdd.complicated_bees.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class GuiHelper {
    /**
     * draws a number of components as wrapped paragraphs, with each component getting spaced by lineHeight / 2
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
                    graphics.drawString(Minecraft.getInstance().font, line, x + padding, curY, color);
                    curY += lineHeight;
                }
            }
            curY += lineHeight / 2;
        }
        return curY;
    }

    /**
     * draws a number of components as wrapped center-aligned paragraphs, with each component getting spaced by lineHeight / 2
     *
     * @param graphics   a GuiGraphics
     * @param x          the x coordinate of the center of all lines
     * @param y          the y coordinate of the center of all lines
     * @param color      a color for text
     * @param lineHeight the height of a line
     * @param maxWidth   the maximum width of each line
     * @param padding    the padding between maxWidth and the actual width of each line
     * @param components a number of components
     * @return a y coordinate for the next line of text, spaced accordingly
     */
    public static int drawCenteredWrappedText(GuiGraphics graphics, int x, int y, int color, int lineHeight, int maxWidth, int padding, Component... components) {
        int curY = y + Minecraft.getInstance().font.lineHeight / 2;
        List<List<FormattedCharSequence>> paragraphs = new ArrayList<>();
        for (Component component : components) {
            if (component == null)
                continue;
            List<FormattedCharSequence> paragraphLines = new ArrayList<>();
            String[] linebroken = component.getString().split("\\r?\\n");
            for (String prewrap : linebroken) {
                List<FormattedCharSequence> lines = Minecraft.getInstance().font.split(Component.literal(prewrap).withStyle(component.getStyle()), maxWidth - padding * 2);
                paragraphLines.addAll(lines);
            }
            paragraphs.add(paragraphLines);
            curY -= paragraphLines.size() * (lineHeight / 2);
        }
        curY -= (paragraphs.size() - 1) * (lineHeight / 4);

        for (List<FormattedCharSequence> paragraph : paragraphs) {
            for (FormattedCharSequence line : paragraph) {
                graphics.drawCenteredString(Minecraft.getInstance().font, line, x, curY, color);
                curY += lineHeight;
            }
            curY += lineHeight / 2;
        }
        return curY;
    }

    /**
     * draws a number of components as wrapped center-aligned paragraphs, with each component getting spaced by lineHeight / 2
     *
     * @param graphics   a GuiGraphics
     * @param x          the x coordinate of the center of the topmost line
     * @param y          the y coordinate of the top of the topmost line
     * @param color      a color for text
     * @param lineHeight the height of a line
     * @param maxWidth   the maximum width of each line
     * @param padding    the padding between maxWidth and the actual width of each line
     * @param components a number of components
     * @return a y coordinate for the next line of text, spaced accordingly
     */
    public static int drawTopAlignedCenteredWrappedText(GuiGraphics graphics, int x, int y, int color, int lineHeight, int maxWidth, int padding, Component... components) {
        int curY = y;
        List<List<FormattedCharSequence>> paragraphs = new ArrayList<>();
        for (Component component : components) {
            if (component == null)
                continue;
            List<FormattedCharSequence> paragraphLines = new ArrayList<>();
            String[] linebroken = component.getString().split("\\r?\\n");
            for (String prewrap : linebroken) {
                List<FormattedCharSequence> lines = Minecraft.getInstance().font.split(Component.literal(prewrap).withStyle(component.getStyle()), maxWidth - padding * 2);
                paragraphLines.addAll(lines);
            }
            paragraphs.add(paragraphLines);
        }

        for (List<FormattedCharSequence> paragraph : paragraphs) {
            for (FormattedCharSequence line : paragraph) {
                graphics.drawCenteredString(Minecraft.getInstance().font, line, x, curY, color);
                curY += lineHeight;
            }
            curY += lineHeight / 2;
        }
        return curY;
    }

    /**
     * draws a component right-aligned
     *
     * @param graphics  a GuiGraphics
     * @param x         the x coordinate of the right side of the line
     * @param y         the y coordinate of the line
     * @param color     the color of the text
     * @param component the component to write
     */
    public static void drawRightAlignedText(GuiGraphics graphics, int x, int y, int color, Component component) {
        graphics.drawString(Minecraft.getInstance().font, component, x - Minecraft.getInstance().font.width(component), y, color);
    }

    /**
     * Draws a bordered rectangle
     *
     * @param graphics
     * @param pX
     * @param pY
     * @param pWidth
     * @param pHeight
     * @param borderWidth
     * @param borderColor
     * @param fillColor
     */
    public static void drawBorderedRectangle(GuiGraphics graphics, int pX, int pY, int pWidth, int pHeight, int borderWidth, int borderColor, int fillColor) {
        graphics.fill(pX, pY, pX + pWidth, pY + borderWidth, borderColor);
        graphics.fill(pX, pY + pHeight - borderWidth, pX + pWidth, pY + pHeight, borderColor);
        graphics.fill(pX, pY + borderWidth, pX + borderWidth, pY + pHeight - borderWidth, borderColor);
        graphics.fill(pX + pWidth - borderWidth, pY + borderWidth, pX + pWidth, pY + pHeight - borderWidth, borderColor);
        graphics.fill(pX + borderWidth, pY + borderWidth, pX + pWidth - borderWidth, pY + pHeight - borderWidth, fillColor);
    }
}
