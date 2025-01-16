package com.accbdd.complicated_bees.screen.widget.microscope;

import com.accbdd.complicated_bees.network.PacketHandler;
import com.accbdd.complicated_bees.network.packet.WireGamePacketClientbound;
import com.accbdd.complicated_bees.network.packet.WireGamePacketServerbound;
import com.accbdd.complicated_bees.screen.MicroscopeScreen;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import java.util.Arrays;

public class ConnectWiresGame extends BaseMicroscopeGame {

    byte[] currentGuess;
    int[] squareColors;
    int lastClicked = -1;
    int squareSize, squarePairs;
    WireGamePacketClientbound.GameState gameState;
    MicroscopeScreen screen;

    public ConnectWiresGame(int pX, int pY, int width, int height, int difficulty, MicroscopeScreen screen) {
        super(pX, pY, width, height);
        this.screen = screen;
        squarePairs = difficulty;
        currentGuess = new byte[squarePairs];
        squareColors = new int[squarePairs * 2];
        Arrays.fill(currentGuess, (byte)-1);
        squareSize = width/squarePairs;
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
        for (int i = 0; i < (squarePairs*2); i++) {
            drawBorderedRectangle(graphics, getSquareX(i), getSquareY(i), squareSize, squareSize,
                    0xFFAAAAAA,
                    lastClicked == i ? 0xFFFFCC00 : squareColors[i]);
        }
    }

    private void drawAllLinks(GuiGraphics graphics) {
        for (int i = 0; i < currentGuess.length; i++) {
            if (currentGuess[i] != -1)
                drawLineBetween(graphics, getSquareX(i)+squareSize/2, getSquareY(i)+squareSize/2, getSquareX(currentGuess[i]+squarePairs)+squareSize/2, getSquareY(currentGuess[i]+squarePairs)+squareSize/2);
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
        byte clickedSquare = getSquare(pMouseX, pMouseY);
        if (clickedSquare == -1)
            return;
        if (lastClicked != -1 && lastClicked < squarePairs && clickedSquare >= squarePairs) {
            currentGuess[lastClicked] = (byte) (clickedSquare - squarePairs);
            sendGuess(currentGuess);
            squareColors[lastClicked] = 0xFF00FF00;
            squareColors[clickedSquare] = 0xFF00FF00;
            lastClicked = -1;
        } else {
            lastClicked = clickedSquare;
        }
    }

    private byte getSquare(double pX, double pY) {
        if (getX() < pX && pX < getX()+getWidth() && getY() < pY && pY < getY()+getHeight()) {
            if (pY < getY() + squareSize) {
                return (byte) ((pX-getX()) / squareSize);
            } else if (pY > getY()+getHeight()-squareSize) {
                return (byte) ((pX-getX()) / squareSize + squarePairs);
            }
        }
        return -1;
    }

    @Override
    public void sendGuess(byte[] guess) {
        PacketHandler.CHANNEL.sendToServer(new WireGamePacketServerbound(guess));
    }

    @Override
    public void setGameState(WireGamePacketClientbound.GameState state) {
        gameState = state;
    }
}
