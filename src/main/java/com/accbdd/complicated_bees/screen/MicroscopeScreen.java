package com.accbdd.complicated_bees.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {
    private final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/microscope.png");

    public MicroscopeScreen(MicroscopeMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageWidth = 231;
        this.imageHeight = 216;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        renderBackground(graphics);
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mousex, int mousey, float partialTick) {
        super.render(graphics, mousex, mousey, partialTick);
        renderTooltip(graphics, mousex, mousey);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mousex, int mousey) {
    }
}
