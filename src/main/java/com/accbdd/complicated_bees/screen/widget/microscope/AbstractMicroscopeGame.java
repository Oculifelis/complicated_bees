package com.accbdd.complicated_bees.screen.widget.microscope;

import com.accbdd.complicated_bees.screen.MicroscopeScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

/**
 * The abstract for a microscope game.
 */
public abstract class AbstractMicroscopeGame extends AbstractWidget implements IMicroscopeGame {
    int difficulty;
    final MicroscopeScreen screen;

    /**
     * @param pX the x of the widget
     * @param pY the y of the widget
     * @param pWidth the width of the widget
     * @param pHeight the height of the widget
     * @param difficulty the difficulty of the widget (3-8)
     * @param screen the screen this widget is attached to
     */
    public AbstractMicroscopeGame(int pX, int pY, int pWidth, int pHeight, int difficulty, MicroscopeScreen screen) {
        super(pX, pY, pWidth, pHeight, Component.literal(""));
        this.difficulty = difficulty;
        this.screen = screen;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public MicroscopeScreen getScreen() {
        return screen;
    }
}
