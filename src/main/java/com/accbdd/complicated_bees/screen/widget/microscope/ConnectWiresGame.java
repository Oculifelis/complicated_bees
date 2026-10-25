package com.accbdd.complicated_bees.screen.widget.microscope;

import com.accbdd.complicated_bees.network.packet.MicroscopeGameClientbound;
import com.accbdd.complicated_bees.network.packet.MicroscopeGameServerbound;
import com.accbdd.complicated_bees.screen.MicroscopeScreen;
import com.accbdd.complicated_bees.util.GuiHelper;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class ConnectWiresGame extends AbstractMicroscopeGame {
    private static final Identifier BG = Identifier.fromNamespaceAndPath(MODID, "textures/gui/microscope/matching.png");
    private static final int BORDER_WIDTH = 1;
    private static final int VERTICAL_PADDING = 3;
    private static final int VERTICAL_SPACE = 29;
    private static final int MIN_SECTION_HEIGHT = 15;
    private static final int MIN_SECTION_WIDTH = 15;

    byte[] currentGuess;
    Section[] sections;
    List<Section> hintHighlighted = new ArrayList<>();
    byte lastClicked = -1;
    byte clickedSquare = -1;
    int maxSectionWidth, sectionPairs;
    Component bannerText;
    MicroscopeGameClientbound.GameState gameState;
    Random rand = new Random();

    //animation stuff
    private static final int BG_ANIM_LENGTH = 40;
    private static final int SQUARE_ANIM_LENGTH = 60;
    private float transparencyMod = 0;
    private float animationTimer = BG_ANIM_LENGTH + SQUARE_ANIM_LENGTH;

    public ConnectWiresGame(int pX, int pY, int width, int height, int difficulty, MicroscopeScreen screen) {
        super(pX, pY, width, height, difficulty, screen);
        sectionPairs = getDifficulty();
        currentGuess = new byte[sectionPairs];
        reset();
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor pGuiGraphicsExtractor, int pMouseX, int pMouseY, float pPartialTick) {
        if (animationTimer > 0)
            animationTimer -= pPartialTick;
        com.accbdd.complicated_bees.port.PortGui.blit(pGuiGraphicsExtractor,BG,
                getX(),
                getY(),
                0,
                0,
                animationTimer > SQUARE_ANIM_LENGTH ? (int) (getWidth()-((animationTimer-SQUARE_ANIM_LENGTH) * getWidth()/BG_ANIM_LENGTH)) : getWidth(),
                getHeight());
        drawAllSections(pGuiGraphicsExtractor, getX(), getY());
        drawAllLinks(pGuiGraphicsExtractor);
        if (gameState != MicroscopeGameClientbound.GameState.ONGOING) {
            transparencyMod += pPartialTick;
            transparencyMod %= 30;
            drawText(pGuiGraphicsExtractor);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void hint(byte index, byte hint) {
        hintHighlighted.clear();
        hintHighlighted.add(sections[index]);
        hintHighlighted.add(sections[sectionPairs + hint]);
    }

    private void drawText(GuiGraphicsExtractor graphics) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(getX(), getY());
        int color = gameState == MicroscopeGameClientbound.GameState.FAILED ? 0xFFFF0000 : 0xFF00FF00;
        graphics.fill(0, getHeight()/2-Minecraft.getInstance().font.lineHeight/2 - 14, getWidth(), getHeight()/2+16, color & (transparencyMod < 15 ? 0xCCCCCC00 : 0xAACCCC00));
        graphics.pose().scale(2, 2);
        graphics.centeredText(Minecraft.getInstance().font,
                bannerText,
                getWidth()/4,
                getHeight()/4-Minecraft.getInstance().font.lineHeight/2-3,
                color);
        graphics.pose().scale(0.5f, 0.5f);
        graphics.centeredText(Minecraft.getInstance().font,
                Component.translatable("gui.complicated_bees.microscope.click"),
                getWidth()/2,
                getHeight()/2-Minecraft.getInstance().font.lineHeight/2+9,
                color);
        graphics.pose().popMatrix();
    }

    private void drawAllSections(GuiGraphicsExtractor graphics, int pX, int pY) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(getX(), getY());
        if (animationTimer <= SQUARE_ANIM_LENGTH)
            for (int i = 0; i < sections.length; i++) {
                Section section = sections[i];
                if ((sections.length - i) * (SQUARE_ANIM_LENGTH / sections.length) >= animationTimer)
                    GuiHelper.drawBorderedRectangle(graphics,
                            section.x,
                            section.y,
                            section.width,
                            section.height,
                            BORDER_WIDTH,
                            hintHighlighted.contains(section) ? 0xFF00FFFF : 0xFFFFCC00,
                            lastClicked == i ? 0x66FFCC00 : section.color);
            }
        graphics.pose().popMatrix();
    }

    private void drawAllLinks(GuiGraphicsExtractor graphics) {
        for (int i = 0; i < currentGuess.length; i++) {
            if (currentGuess[i] != -1) {
                Section start = sections[i];
                Section end = sections[currentGuess[i]+sectionPairs];
                drawLineBetween(graphics, start.getCenter()[0] + getX(), start.getCenter()[1] + getY(), end.getCenter()[0]+getX(), end.getCenter()[1]+getY());
            }
        }
    }

    private void drawLineBetween(GuiGraphicsExtractor graphics, int x, int y, int x2, int y2) {
        int dist = (int)Math.sqrt(Math.pow(x2-x, 2) + Math.pow(y2-y, 2));
        graphics.pose().pushMatrix();
        graphics.pose().rotateAbout((float)-Math.atan2(x2-x,y2-y), x, y);
        graphics.fill(x-1, y-1, x+1, y+dist+1, 0xFFFFFFFF);
        graphics.pose().popMatrix();
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
    public void onClick(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        double pMouseX=event.x(), pMouseY=event.y();
        super.onClick(event, doubleClick);
        clickedSquare = getSquare(pMouseX, pMouseY);
        if (animationTimer > 0) {
            animationTimer = 0;
            return;
        }
        if (clickedSquare == -1 || gameState != MicroscopeGameClientbound.GameState.ONGOING) {
            lastClicked = -1;
            if (gameState == MicroscopeGameClientbound.GameState.FAILED || gameState == MicroscopeGameClientbound.GameState.WON)
                reset();
            return;
        }
        hintHighlighted.clear();
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
        net.neoforged.neoforge.client.network.ClientPacketDistributor.sendToServer(new MicroscopeGameServerbound(guess));
    }

    @Override
    public void setGameState(MicroscopeGameClientbound.GameState state) {
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
        getScreen().clearGame();
        animationTimer = BG_ANIM_LENGTH + SQUARE_ANIM_LENGTH;
        maxSectionWidth = width/sectionPairs;
        Arrays.fill(currentGuess, (byte)-1);
        sendGuess(currentGuess);
        gameState = MicroscopeGameClientbound.GameState.ONGOING;
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
