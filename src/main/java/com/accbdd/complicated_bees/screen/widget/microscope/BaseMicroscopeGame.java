package com.accbdd.complicated_bees.screen.widget.microscope;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public abstract class BaseMicroscopeGame extends AbstractWidget implements IMicroscopeGame {
    public BaseMicroscopeGame(int pX, int pY, int pWidth, int pHeight) {
        super(pX, pY, pWidth, pHeight, Component.literal(""));
    }
}
