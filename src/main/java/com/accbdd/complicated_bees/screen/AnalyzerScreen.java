package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.screen.widget.AnalyzerScrollWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class AnalyzerScreen extends AbstractContainerScreen<AbstractAnalyzerMenu> {
    private final Identifier GUI = Identifier.tryBuild(MODID, "textures/gui/analyzer.png");
    private AnalyzerScrollWidget widget;

    public AnalyzerScreen(AbstractAnalyzerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title, 249, 216);
    }

    @Override
    protected void init() {
        super.init();
        this.widget = addRenderableWidget(new AnalyzerScrollWidget(leftPos + 8, topPos + 8, 207, 120, getMenu()));
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor pGuiGraphicsExtractor, int pMouseX, int pMouseY) {
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mousex, int mousey, float partialTick) {
        super.extractRenderState(graphics, mousex, mousey, partialTick);
        extractTooltip(graphics, mousex, mousey);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mousex, int mousey) {
        if (this.menu.getCarried().isEmpty()) {
            if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
                ItemStack itemstack = this.hoveredSlot.getItem();
                com.accbdd.complicated_bees.port.PortGui.tooltip(graphics,this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, mousex, mousey);
            } else if (widget.hoveredStack != null) {
                com.accbdd.complicated_bees.port.PortGui.tooltip(graphics,this.font, this.getTooltipFromContainerItem(widget.hoveredStack), widget.hoveredStack.getTooltipImage(), widget.hoveredStack, mousex, mousey);
            } else if (widget.geneTooltip != null) {
                com.accbdd.complicated_bees.port.PortGui.tooltip(graphics,this.font, widget.geneTooltip, mousex, mousey);
            }
        }
    }
}
