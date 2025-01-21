package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.screen.widget.microscope.ConnectWiresGame;
import com.accbdd.complicated_bees.screen.widget.microscope.IMicroscopeGame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {
    private final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/microscope/base.png");
    private IMicroscopeGame game = null;

    public MicroscopeScreen(MicroscopeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 248;
        this.imageHeight = 216;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new ExtendedButton(leftPos - 50,
                topPos,
                40,
                20,
                Component.literal("play game!"),
                pButton -> startGame()));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        renderBackground(graphics);
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mousex, int mousey, float partialTick) {
        super.render(graphics, mousex, mousey, partialTick);
        renderText(graphics);
        renderGlassSlotOverlay(graphics);
        if (getMenu().researchedMutationsCount > -1)
            graphics.drawString(Minecraft.getInstance().font, Component.translatable("gui.complicated_bees.microscope.mutation_count", getMenu().researchedMutationsCount, getMenu().possibleMutationsCount), leftPos, topPos - 10, 0xFFFFFFFF);
        renderTooltip(graphics, mousex, mousey);
    }

    public void renderText(GuiGraphics graphics) {
        if (game == null) {
            graphics.pose().pushPose();
            graphics.pose().translate(leftPos+8, topPos+8-font.lineHeight/2, 0);
            Component pText = Component.empty();
            if (menu.researchedMutationsCount == -1)
                pText = Component.translatable("gui.complicated_bees.microscope.place");
            else if (menu.possibleMutationsCount == menu.researchedMutationsCount || menu.possibleMutationsCount == 0)
                pText = Component.translatable("gui.complicated_bees.microscope.complete");
            drawLinesText(graphics,
                    pText,
                    215/2,
                    120/2,
                    0xFFFFFF);
            graphics.pose().popPose();
        }
    }

    public void drawLinesText(GuiGraphics graphics, Component text, int x, int y, int color) {
        int curY = y;
        int lineHeight = 12;
        int width = 215;
        int padding = 3;
        String[] linebroken = text.getString().split("\\r?\\n");
        for (String prewrap : linebroken) {
            List<FormattedCharSequence> lines = Minecraft.getInstance().font.split(Component.literal(prewrap).withStyle(text.getStyle()), width - 3 * 2);
            for (FormattedCharSequence line : lines) {
                graphics.drawCenteredString(Minecraft.getInstance().font, line, x, curY, color);
                curY += lineHeight;
            }
        }
        curY += lineHeight / 2;
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mousex, int mousey) {
    }

    public void renderGlassSlotOverlay(GuiGraphics graphics) {
        graphics.blit(GUI, leftPos + 225, topPos + 8, 0, 230, 16, 16);
    }

    public IMicroscopeGame getGame() {
        return game;
    }

    public void startGame() {
        clearGame();
        if (game == null) {
            game = addRenderableWidget(new ConnectWiresGame(leftPos + 8, topPos + 8, 215, 120, 5, this));
        }
    }

    public void clearGame() {
        removeWidget((GuiEventListener) this.game);
        game = null;
    }
}
