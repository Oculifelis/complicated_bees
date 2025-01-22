package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.screen.widget.microscope.ConnectWiresGame;
import com.accbdd.complicated_bees.screen.widget.microscope.IMicroscopeGame;
import com.accbdd.complicated_bees.util.GuiHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {
    private final ResourceLocation GUI = new ResourceLocation(MODID, "textures/gui/microscope/base.png");
    private IMicroscopeGame game = null;
    private PlainTextButton startButton;

    public MicroscopeScreen(MicroscopeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 248;
        this.imageHeight = 216;
    }

    @Override
    protected void init() {
        super.init();
        Component text = Component.translatable("gui.complicated_bees.microscope.start").withStyle(ChatFormatting.WHITE);
        int textWidth = font.width(text);
        startButton = new PlainTextButton(leftPos + 8 + 215/2 - textWidth / 2,
                0,
                textWidth,
                font.lineHeight,
                text,
                pButton -> startGame(),
                Minecraft.getInstance().font) {

            private float animCount;

            @Override
            public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
                animCount += pPartialTick;
                animCount %= 40;
                GuiHelper.drawBorderedRectangle(pGuiGraphics,
                        getX()-5,
                        getY()-5,
                        getWidth()+10,
                        getHeight()+8,
                        1,
                        isHoveredOrFocused() ? 0xFFFFFFFF : 0xFF00FF00,
                        animCount > 20 ? 0xFF00CC00 : 0x6600CC00);
                pGuiGraphics.drawString(font, text, this.getX(), this.getY(), 16777215 | Mth.ceil(this.alpha * 255.0F) << 24);
            }
        };
        addRenderableWidget(startButton);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        renderBackground(graphics);
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mousex, int mousey, float partialTick) {
        super.render(graphics, mousex, mousey, partialTick);
        startButton.visible = game == null;
        renderText(graphics);
        renderGlassSlotOverlay(graphics);
        renderTooltip(graphics, mousex, mousey);
    }

    public void renderText(GuiGraphics graphics) {
        if (game == null) {
            graphics.pose().pushPose();
            graphics.pose().translate(leftPos+8, topPos+8-font.lineHeight/2, 0);
            Component pText = null;
            if (menu.researchedMutationsCount == -1) {
                pText = Component.translatable("gui.complicated_bees.microscope.place");
            } else if (menu.possibleMutationsCount == menu.researchedMutationsCount || menu.possibleMutationsCount == 0) {
                pText = Component.translatable("gui.complicated_bees.microscope.complete").withStyle(ChatFormatting.GREEN);
            }
            startButton.setY(topPos + GuiHelper.drawCenteredWrappedText(graphics,
                    215 / 2,
                    120 / 2,
                    0xFFFFFF,
                    12,
                    215,
                    3,
                    menu.researchedMutationsCount > -1 ? Component.translatable("gui.complicated_bees.microscope.mutation_count", getMenu().researchedMutationsCount, getMenu().possibleMutationsCount) : null,
                    pText,
                    menu.researchedMutationsCount < menu.possibleMutationsCount ? Component.empty() : null));
            startButton.visible = pText == null;
            graphics.pose().popPose();
        }
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
        if (menu.possibleMutationsCount != menu.researchedMutationsCount) {
            game = addRenderableWidget(new ConnectWiresGame(leftPos + 8, topPos + 8, 215, 120, menu.getDifficulty(), this));
        }
    }

    public void clearGame() {
        removeWidget((GuiEventListener) this.game);
        game = null;
    }
}
