package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.screen.widget.LibraryInfoWidget;
import com.accbdd.complicated_bees.screen.widget.LibraryMutationWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class LibraryScreen extends AbstractContainerScreen<LibraryMenu> {
    public static final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/library.png");
    private LibraryMutationWidget mutationWidget;
    private LibraryInfoWidget infoWidget;

    public LibraryScreen(LibraryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 249;
        this.imageHeight = 216;
    }

    @Override
    protected void init() {
        super.init();
        this.mutationWidget = addRenderableWidget(new LibraryMutationWidget(leftPos + 8, topPos + 8, 107, 120, getMenu()));
        this.infoWidget = addRenderableWidget(new LibraryInfoWidget(leftPos + 117, topPos + 8, 106, 120, getMenu(), mutationWidget));
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
            } else if (mutationWidget.hoveredStack != null) {
                graphics.renderTooltip(this.font, this.getTooltipFromContainerItem(mutationWidget.hoveredStack), mutationWidget.hoveredStack.getTooltipImage(), mutationWidget.hoveredStack, mousex, mousey);
            }
        }
    }
}
