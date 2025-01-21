package com.accbdd.complicated_bees.screen.widget.microscope;

import com.accbdd.complicated_bees.network.packet.MicroscopeGamePacketClientbound;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public class WordleGame extends BaseMicroscopeGame {
    public WordleGame(int pX, int pY, int pWidth, int pHeight) {
        super(pX, pY, pWidth, pHeight);
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
