package com.accbdd.complicated_bees.screen.widget;

import net.minecraft.client.Minecraft;
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

    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.active && this.visible) {
            if (pButton < 2) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    state = BeeTypeState.values()[(state.ordinal() + (pButton == 0 ? 1 : BeeTypeState.values().length - 1)) % BeeTypeState.values().length];
                    stateConsumer.accept(state);
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.blit(GUI, getX(), getY(), 176, state.ordinal() * 16, 16, 16);
    }

    public enum BeeTypeState {
        NONE,
        DRONE,
        PRINCESS,
        QUEEN,
        ANY_BEE,
        NOT_BEE
    }
}
