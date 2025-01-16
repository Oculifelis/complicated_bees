package com.accbdd.complicated_bees.screen.widget.microscope;

import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class ConnectWiresGame extends AbstractMicroscopeGame {

    int[] correctLinks;
    int[] currentLinks;
    int lastClicked = -1;
    int squareSize, squarePairs;

    public ConnectWiresGame(int pX, int pY, int difficulty) {
        super(pX, pY, 120, 100);
        squarePairs = difficulty;
        correctLinks = IntStream.range(0, squarePairs).toArray();
        currentLinks = new int[squarePairs];
        Arrays.fill(currentLinks, -1);
        squareSize = 120/squarePairs;
        shuffle();
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFF222222);
        drawAllSquares(pGuiGraphics, getX(), getY());
        drawAllLinks(pGuiGraphics);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    private void drawBorderedRectangle(GuiGraphics graphics, int pX, int pY, int pWidth, int pHeight, int borderColor, int fillColor) {
        graphics.fill(pX, pY, pX+pWidth, pY+pHeight, borderColor);
        graphics.fill(pX+2, pY+2, pX+pWidth-2, pY+pHeight-2, fillColor);
    }

    private void drawAllSquares(GuiGraphics graphics, int pX, int pY) {
        for (int i = 0; i < (squarePairs *2); i++) {
            drawBorderedRectangle(graphics, getSquareX(i), getSquareY(i), squareSize, squareSize,
                    i == lastClicked ? 0xFFFFCC00 : 0xFF00FF00,
                    i == lastClicked ? 0xFFFF0000 : 0xFF0000FF);
        }
    }

    private void drawAllLinks(GuiGraphics graphics) {
        for (int i = 0; i < currentLinks.length; i++) {
            if (currentLinks[i] != -1)
                drawLineBetween(graphics, getSquareX(i)+squareSize/2, getSquareY(i)+squareSize/2, getSquareX(currentLinks[i]+squarePairs)+squareSize/2, getSquareY(currentLinks[i]+squarePairs)+squareSize/2);
        }
    }

    private int getSquareX(int square) {
        return getX() + ((square % squarePairs) * squareSize);
    }

    private int getSquareY(int square) {
        return square < squarePairs ? getY() : getY()+getHeight()-squareSize;
    }

    private void drawLineBetween(GuiGraphics graphics, int x, int y, int x2, int y2) {
        int dist = (int)Math.sqrt(Math.pow(x2-x, 2) + Math.pow(y2-y, 2));
        graphics.pose().pushPose();
        graphics.pose().rotateAround(Axis.ZP.rotationDegrees((float) (Math.atan2(x2-x, y2-y) * -180 / Math.PI)), x, y, 0);
        graphics.fill(x-1, y-1, x+1, y+dist+1, 0xFFFFFFFF);
        graphics.pose().popPose();
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        int clickedSquare = getSquare(pMouseX, pMouseY);
        if (lastClicked != -1 && lastClicked < squarePairs && clickedSquare >= squarePairs) {
            currentLinks[lastClicked] = clickedSquare - squarePairs;
            lastClicked = -1;
        } else {
            lastClicked = clickedSquare;
        }
    }

    private int getSquare(double pX, double pY) {
        if (getX() < pX && pX < getX()+getWidth() && getY() < pY && pY < getY()+getHeight()) {
            if (pY < getY() + squareSize) {
                return (int)(pX-getX()) / squareSize;
            } else if (pY > getY()+getHeight()-squareSize) {
                return (int)(pX-getX()) / squareSize + squarePairs;
            }
        }
        return -1;
    }

    private void shuffle() {
        Random rnd = new Random();
        for (int i = correctLinks.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i+1);
            int a = correctLinks[index];
            correctLinks[index] = correctLinks[i];
            correctLinks[i] = a;
        }
    }
}
