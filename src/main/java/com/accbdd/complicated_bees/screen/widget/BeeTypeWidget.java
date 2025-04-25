package com.accbdd.complicated_bees.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeTypeWidget extends AbstractButton {
    private static final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/bee_sorter.png");
    private final Consumer<BeeTypeState> stateConsumer;
    BeeTypeState state;

    public BeeTypeWidget(int pX, int pY, int pWidth, int pHeight, byte initialState, Consumer<BeeTypeState> stateConsumer) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.stateConsumer = stateConsumer;
        state = BeeTypeState.values()[initialState % BeeTypeState.values().length];
    }

    @Override
    public void onPress() {
        state = BeeTypeState.values()[(state.ordinal() + 1) % BeeTypeState.values().length];
        stateConsumer.accept(state);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.blit(GUI, getX(), getY(), 176, state.ordinal() * 16, 16, 16);
    }

    public enum BeeTypeState {
        DRONE,
        PRINCESS,
        QUEEN,
        ANY,
        NONE;
    }
}
