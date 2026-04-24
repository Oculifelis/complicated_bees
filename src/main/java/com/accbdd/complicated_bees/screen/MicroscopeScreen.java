package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.network.packet.MicroscopeGameClientbound;
import com.accbdd.complicated_bees.network.packet.MicroscopeHintServerbound;
import com.accbdd.complicated_bees.screen.widget.microscope.ConnectWiresGame;
import com.accbdd.complicated_bees.screen.widget.microscope.IMicroscopeGame;
import com.accbdd.complicated_bees.util.GuiHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MicroscopeScreen extends AbstractContainerScreen<MicroscopeMenu> {
    private static final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/microscope/base.png");
    private IMicroscopeGame game = null;
    private PlainTextButton startButton;
    private AnalyzeButton analyzeButton;

    public MicroscopeScreen(MicroscopeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 248;
        this.imageHeight = 216;
    }

    @Override
    protected void init() {
        super.init();
        Component text = Component.translatable("gui.complicated_bees.microscope.start").withStyle(ChatFormatting.WHITE);
        int textWidth = font.width(text) + 10;
        analyzeButton = new AnalyzeButton(leftPos + 225,
                topPos + 26,
                16,
                12,
                GUI,
                (button) -> {
                    if (game != null && menu.canSendHint())
                        PacketDistributor.sendToServer(MicroscopeHintServerbound.INSTANCE);
                });
        startButton = new PlainTextButton(leftPos + 8 + 215/2 - textWidth / 2,
                topPos + 50,
                textWidth,
                font.lineHeight + 8,
                text,
                pButton -> startGame(),
                Minecraft.getInstance().font) {

            private float animCount;

            @Override
            public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
                animCount += pPartialTick;
                animCount %= 40;
                GuiHelper.drawBorderedRectangle(pGuiGraphics,
                        this.getX(),
                        this.getY(),
                        getWidth(),
                        getHeight(),
                        1,
                        isHoveredOrFocused() ? 0xFFFFFFFF : 0xFF00FF00,
                        animCount > 20 ? 0xFF00CC00 : 0x6600CC00);
                pGuiGraphics.drawString(font, text, this.getX()+5, this.getY()+5, 16777215 | Mth.ceil(this.alpha * 255.0F) << 24);
            }
        };
        addRenderableWidget(startButton);
        addRenderableWidget(analyzeButton);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (analyzeButton.isHovered())
            analyzeButton.clicked = true;
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        analyzeButton.clicked = false;
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(GUI, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mousex, int mousey, float partialTick) {
        super.render(graphics, mousex, mousey, partialTick);
        startButton.visible = game == null;
        renderText(graphics);
        renderSlotOverlays(graphics);
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

    public void renderSlotOverlays(GuiGraphics graphics) {
        graphics.blit(GUI, leftPos + 225, topPos + 8, 0, 240, 16, 16);
        for (int i = 0; i < 5; i++) {
            if (!(menu.difficulty < i + 2 || game == null))
                graphics.blit(GUI, leftPos+225, topPos+40+18*i, 16, 240, 16, 16);
        }
    }

    public IMicroscopeGame getGame() {
        return game;
    }

    public void startGame() {
        clearGame();
        if (menu.possibleMutationsCount != menu.researchedMutationsCount) {
            game = addRenderableWidget(new ConnectWiresGame(leftPos + 8, topPos + 8, 215, 120, menu.getDifficulty(), this));
            menu.setState(MicroscopeGameClientbound.GameState.ONGOING);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        if (analyzeButton.isHovered() && game != null) {
            List<Component> components = new ArrayList<>();
            components.add(Component.literal("Analyze material").withStyle(ChatFormatting.GRAY));
            components.add(Component.empty());
            MutableComponent materialComponent;
            ChatFormatting color = menu.canSendHint() ? ChatFormatting.GRAY : ChatFormatting.RED;
            if (menu.difficulty == 1) {
                materialComponent = Component.literal("1 research material").withStyle(color);
            } else {
                materialComponent = Component.literal(Math.min(menu.difficulty, 6) - 1 + " research materials").withStyle(color);
            }
            components.add(materialComponent);
            pGuiGraphics.renderComponentTooltip(font, components, pX, pY);
        }

    }

    public void clearGame() {
        removeWidget((GuiEventListener) this.game);
        game = null;
    }

    // TODO: Migrate to ImageButton and WidgetSprites
    private class AnalyzeButton extends Button {
        private final ResourceLocation resourceLocation;
        private boolean clicked = false;

        protected AnalyzeButton(int x, int y, int width, int height, ResourceLocation resourceLocation, OnPress onPress) {
            super(x, y, width, height, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
            this.resourceLocation = resourceLocation;
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            pGuiGraphics.blit(this.resourceLocation, getX(), getY(), (clicked || !menu.canSendHint() || game == null) ? 0 : isHovered ? 32 : 16, 228, 16, 12);
        }

        @Override
        public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
            if (this.active && this.visible) {
                if (this.isValidClickButton(pButton)) {
                    boolean flag = this.clicked(pMouseX, pMouseY);
                    if (flag && menu.canSendHint() && game != null) {
                        this.playDownSound(Minecraft.getInstance().getSoundManager());
                        this.onClick(pMouseX, pMouseY);
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
