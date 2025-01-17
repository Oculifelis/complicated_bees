package com.accbdd.complicated_bees.screen.widget.microscope;

import com.accbdd.complicated_bees.network.PacketHandler;
import com.accbdd.complicated_bees.network.packet.MicroscopeGamePacketClientbound;
import com.accbdd.complicated_bees.network.packet.MicroscopeGamePacketServerbound;
import com.accbdd.complicated_bees.screen.MicroscopeScreen;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Random;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class ConnectWiresGame extends BaseMicroscopeGame {
    private static final ResourceLocation BG = new ResourceLocation(MODID, "textures/gui/microscope/matching.png");
    private static final int BORDER_WIDTH = 1;
    private static final int VERTICAL_PADDING = 3;
    private static final int VERTICAL_SPACE = 29;
    private static final int MIN_SECTION_HEIGHT = 15;
    private static final int MIN_SECTION_WIDTH = 15;

    byte[] currentGuess;
    Section[] sections;
    byte lastClicked = -1;
    byte clickedSquare = -1;
    float transparencyMod = 0;
    int maxSectionWidth, sectionPairs;
    Component bannerText;
    MicroscopeGamePacketClientbound.GameState gameState;
    MicroscopeScreen screen;
    Random rand = new Random();

    public ConnectWiresGame(int pX, int pY, int width, int height, int difficulty, MicroscopeScreen screen) {
        super(pX, pY, width, height);
        this.screen = screen;
        sectionPairs = difficulty;
        currentGuess = new byte[sectionPairs];
        reset();
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        //todo: add animation
        pGuiGraphics.blit(BG, getX(), getY(), 0, 0, getWidth(), getHeight());
        drawAllSections(pGuiGraphics, getX(), getY());
        drawAllLinks(pGuiGraphics);
        if (gameState != MicroscopeGamePacketClientbound.GameState.ONGOING) {
            transparencyMod += pPartialTick;
            transparencyMod %= 30;
            drawText(pGuiGraphics);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    private void drawText(GuiGraphics graphics) {
        graphics.pose().pushPose();
        graphics.pose().translate(getX(), getY(), 0);
        graphics.pose().scale(2, 2, 1);
        int color = gameState == MicroscopeGamePacketClientbound.GameState.FAILED ? 0xFFFF0000 : 0xFF00FF00;
        graphics.fill(0, getHeight()/4-Minecraft.getInstance().font.lineHeight/2 - 3, getWidth()/2, getHeight()/4+7, color & (transparencyMod < 15 ? 0xCCCCCC00 : 0xAACCCC00));
        graphics.drawCenteredString(Minecraft.getInstance().font,
                bannerText,
                getWidth()/4,
                getHeight()/4-Minecraft.getInstance().font.lineHeight/2,
                color);
        graphics.pose().popPose();
    }

    private void drawBorderedRectangle(GuiGraphics graphics, int pX, int pY, int pWidth, int pHeight, int borderColor, int fillColor) {
        graphics.fill(pX, pY, pX + pWidth, pY + BORDER_WIDTH, borderColor);
        graphics.fill(pX, pY + pHeight - BORDER_WIDTH, pX + pWidth, pY + pHeight, borderColor);
        graphics.fill(pX, pY + BORDER_WIDTH, pX + BORDER_WIDTH, pY + pHeight - BORDER_WIDTH, borderColor);
        graphics.fill(pX + pWidth - BORDER_WIDTH, pY + BORDER_WIDTH, pX + pWidth, pY + pHeight - BORDER_WIDTH, borderColor);
        graphics.fill(pX + BORDER_WIDTH, pY + BORDER_WIDTH, pX + pWidth - BORDER_WIDTH, pY + pHeight - BORDER_WIDTH, fillColor);
    }

    private void drawAllSections(GuiGraphics graphics, int pX, int pY) {
        graphics.pose().pushPose();
        graphics.pose().translate(getX(), getY(), 0);
        for (int i = 0; i < sections.length; i++) {
            Section section = sections[i];
            drawBorderedRectangle(graphics,
                    section.x,
                    section.y,
                    section.width,
                    section.height,
                    0xFFFFCC00,
                    lastClicked == i ? 0x66FFCC00 : section.color);
        }
        graphics.pose().popPose();
    }

    private void drawAllLinks(GuiGraphics graphics) {
        for (int i = 0; i < currentGuess.length; i++) {
            if (currentGuess[i] != -1) {
                Section start = sections[i];
                Section end = sections[currentGuess[i]+sectionPairs];
                drawLineBetween(graphics, start.getCenter()[0] + getX(), start.getCenter()[1] + getY(), end.getCenter()[0]+getX(), end.getCenter()[1]+getY());
            }
        }
    }

    private void drawLineBetween(GuiGraphics graphics, int x, int y, int x2, int y2) {
        int dist = (int)Math.sqrt(Math.pow(x2-x, 2) + Math.pow(y2-y, 2));
        graphics.pose().pushPose();
        graphics.pose().rotateAround(Axis.ZP.rotationDegrees((float) (Math.atan2(x2-x, y2-y) * -180 / Math.PI)), x, y, 0);
        graphics.fill(x-1, y-1, x+1, y+dist+1, 0xFFFFFFFF);
        graphics.pose().popPose();
    }

    private void generateSections() {
        sections = new Section[sectionPairs * 2];
        maxSectionWidth = getWidth()/sectionPairs;
        for (int i = 0; i < sections.length; i++) {
            int minX = (i % sectionPairs) * maxSectionWidth;
            int x = rand.nextInt(minX, minX+maxSectionWidth-MIN_SECTION_WIDTH);
            int minY = i < sectionPairs ? VERTICAL_PADDING : getHeight() - VERTICAL_PADDING - VERTICAL_SPACE;
            int y = rand.nextInt(minY, minY+VERTICAL_SPACE-MIN_SECTION_WIDTH);
            sections[i] = new Section(x,
                    y,
                    rand.nextInt(MIN_SECTION_WIDTH, maxSectionWidth - (x - minX)),
                    rand.nextInt(MIN_SECTION_HEIGHT, VERTICAL_SPACE - (y - minY)),
                    0);
        }
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        clickedSquare = getSquare(pMouseX, pMouseY);
        if (clickedSquare == -1 || gameState != MicroscopeGamePacketClientbound.GameState.ONGOING) {
            lastClicked = -1;
            return;
        }
        if (lastClicked != -1) { //we have a previously clicked square
            if (lastClicked < sectionPairs && clickedSquare >= sectionPairs) { //top to bottom
                currentGuess[lastClicked] = (byte) (clickedSquare - sectionPairs);
                sendGuess(currentGuess);
            } else if (clickedSquare < sectionPairs && lastClicked >= sectionPairs) { //bottom to top
                currentGuess[clickedSquare] = (byte) (lastClicked - sectionPairs);
                sendGuess(currentGuess);
            } else { //both clicked are in the same row
                lastClicked = clickedSquare;
            }
        } else {
            lastClicked = clickedSquare;
        }
    }

    private byte getSquare(double pX, double pY) {
        for (byte i = 0; i < sections.length; i++) {
            if (sections[i].checkInside(pX-getX(), pY-getY()))
                return i;
        }
        return -1;
    }

    @Override
    public void sendGuess(byte[] guess) {
        PacketHandler.CHANNEL.sendToServer(new MicroscopeGamePacketServerbound(guess));
    }

    @Override
    public void setGameState(MicroscopeGamePacketClientbound.GameState state) {
        gameState = state;
        switch (state) {
            case START:
                reset();
                break;
            case FAILED:
                lastClicked = -1;
                for (byte i = 0; i < sections.length; i++) {
                    sections[i].color = 0x66FF0000;
                }
                bannerText = Component.translatable("gui.complicated_bees.microscope.sequence.lose");
                break;
            case WON:
            case ONGOING:
                sections[lastClicked].color = 0x6600FF00;
                sections[clickedSquare].color = 0x6600FF00;
                bannerText = Component.translatable("gui.complicated_bees.microscope.sequence.win");
                lastClicked = -1;
                break;
        }
    }

    @Override
    public void reset() {
        maxSectionWidth = width/sectionPairs;
        Arrays.fill(currentGuess, (byte)-1);
        gameState = MicroscopeGamePacketClientbound.GameState.ONGOING;
        generateSections();
    }

    private static final class Section {
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private int color;

        private Section(int x, int y, int width, int height, int color) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        public int width() {
            return width;
        }

        public int height() {
            return height;
        }

        public boolean checkInside(double pX, double pY) {
            return (this.x < pX && pX < this.x + this.width && this.y < pY && pY < this.y + this.height);
        }

        public int[] getCenter() {
            return new int[]{this.x + this.width / 2, this.y + this.height / 2};
        }
    }
}
