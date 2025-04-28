package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.network.PacketHandler;
import com.accbdd.complicated_bees.network.packet.UpdateSorterServerbound;
import com.accbdd.complicated_bees.screen.widget.BeeTypeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeSorterScreen extends AbstractContainerScreen<BeeSorterMenu> {
    private static final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/bee_sorter.png");
    private byte[] beeTypes;
    public BeeSorterScreen(BeeSorterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageHeight = 232;
        this.inventoryLabelY = this.imageHeight - 94;
        beeTypes = new byte[6];
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < 6; i++) {
            beeTypes[i] = (byte)menu.getData().get(i);
            int finalI = i;
            addRenderableWidget(new BeeTypeWidget(leftPos + 9, topPos + 19 + i*18, 16, 16, beeTypes[i], state -> beeTypes[finalI] = (byte) state.ordinal()));
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pGuiGraphics);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    public void onClose() {
        super.onClose();
        PacketHandler.CHANNEL.sendToServer(new UpdateSorterServerbound(menu.getPos(), beeTypes));
    }
}
