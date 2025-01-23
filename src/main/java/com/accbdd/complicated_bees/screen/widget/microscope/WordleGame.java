package com.accbdd.complicated_bees.screen.widget.microscope;

import com.accbdd.complicated_bees.network.packet.MicroscopeGamePacketClientbound;
import com.accbdd.complicated_bees.screen.MicroscopeScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public class WordleGame extends AbstractMicroscopeGame {
    /**
     * @param pX         the x of the widget
     * @param pY         the y of the widget
     * @param pWidth     the width of the widget
     * @param pHeight    the height of the widget
     * @param difficulty the difficulty of the widget (3-10)
     * @param screen     the screen this widget is attached to
     */
    public WordleGame(int pX, int pY, int pWidth, int pHeight, int difficulty, MicroscopeScreen screen) {
        super(pX, pY, pWidth, pHeight, difficulty, screen);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void sendGuess(byte[] guess) {

    }

    @Override
    public void setGameState(MicroscopeGamePacketClientbound.GameState state) {

    }

    @Override
    public void hint(byte index, byte hint) {

    }

    @Override
    public void reset() {

    }
}
