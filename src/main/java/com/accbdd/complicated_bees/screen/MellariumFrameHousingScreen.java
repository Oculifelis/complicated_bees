package com.accbdd.complicated_bees.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MellariumFrameHousingScreen extends AbstractContainerScreen<MellariumFrameHousingMenu> {
    private final Identifier GUI;

    public MellariumFrameHousingScreen(MellariumFrameHousingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 143);
        this.GUI = Identifier.fromNamespaceAndPath(MODID, "textures/gui/mellarium_frame"+(menu.slots.size()-36)+".png");
        this.inventoryLabelY = this.imageHeight - 10000;
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor pGuiGraphicsExtractor, int pMouseX, int pMouseY) {
        pGuiGraphicsExtractor.text(this.font, this.title, this.imageWidth/2 - this.font.width(this.title)/2, this.titleLabelY, 4210752, false);
        pGuiGraphicsExtractor.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        extractTooltip(graphics, mouseX, mouseY);
    }
}
