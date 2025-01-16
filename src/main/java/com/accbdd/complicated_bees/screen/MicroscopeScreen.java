package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.screen.widget.microscope.ConnectWiresGame;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {
    private final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/microscope.png");
    ConnectWiresGame game;

    public MicroscopeScreen(MicroscopeMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageWidth = 231;
        this.imageHeight = 216;

    }

    @Override
    protected void init() {
        super.init();
        game = addRenderableWidget(new ConnectWiresGame(leftPos + 8, topPos + 8, 215, 120, 5, this));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        renderBackground(graphics);
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

    }

    @Override
    public void render(GuiGraphics graphics, int mousex, int mousey, float partialTick) {
        super.render(graphics, mousex, mousey, partialTick);
        renderGlassSlotOverlay(graphics);
        renderTooltip(graphics, mousex, mousey);
        if (game.isWon()) {
            getMinecraft().player.sendSystemMessage(Component.literal("you win!"));
            onClose();
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mousex, int mousey) {
    }

    public void renderGlassSlotOverlay(GuiGraphics graphics) {
        graphics.blit(GUI, leftPos + 108, topPos + 59, 0, 230, 16, 16);
    }
}
