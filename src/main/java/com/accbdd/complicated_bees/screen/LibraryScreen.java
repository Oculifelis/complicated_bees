package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.screen.widget.LibraryWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class LibraryScreen extends AbstractContainerScreen<LibraryMenu> {
    public static final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/library.png");
    private LibraryWidget widget;

    public LibraryScreen(LibraryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 249;
        this.imageHeight = 216;
    }

    @Override
    protected void init() {
        super.init();
        this.widget = addRenderableWidget(new LibraryWidget(leftPos + 8, topPos + 8, 215, 120, getMenu()));
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int pMouseX, int pMouseY) {

    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(graphics);
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(graphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mousex, int mousey) {
        if (this.menu.getCarried().isEmpty()) {
            if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
                ItemStack itemstack = this.hoveredSlot.getItem();
                graphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, mousex, mousey);
            } else if (widget.hoveredStack != null) {
                graphics.renderTooltip(this.font, this.getTooltipFromContainerItem(widget.hoveredStack), widget.hoveredStack.getTooltipImage(), widget.hoveredStack, mousex, mousey);
            }
        }
    }
}
