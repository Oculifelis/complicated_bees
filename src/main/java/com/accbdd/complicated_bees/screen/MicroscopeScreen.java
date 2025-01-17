package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.screen.widget.microscope.ConnectWiresGame;
import com.accbdd.complicated_bees.screen.widget.microscope.IMicroscopeGame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {
    private final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/microscope/base.png");
    private IMicroscopeGame game;

    public MicroscopeScreen(MicroscopeMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageWidth = 248;
        this.imageHeight = 216;
    }

    @Override
    protected void init() {
        super.init();
        this.game = addRenderableWidget(new ConnectWiresGame(leftPos + 8, topPos + 8, 215, 120, 5, this));
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
        if (getMenu().researchedMutations > -1)
            graphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("gui.complicated_bees.microscope.mutation_count", getMenu().researchedMutations, getMenu().totalMutations), leftPos, topPos - 10, 0xFFFFFFFF);
        renderTooltip(graphics, mousex, mousey);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mousex, int mousey) {
    }

    public void renderGlassSlotOverlay(GuiGraphics graphics) {
        graphics.blit(GUI, leftPos + 224, topPos + 60, 0, 230, 16, 16);
    }

    public IMicroscopeGame getGame() {
        return game;
    }
}
