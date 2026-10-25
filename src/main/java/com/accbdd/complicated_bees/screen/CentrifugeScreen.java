package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.block.entity.CentrifugeBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class CentrifugeScreen extends AbstractContainerScreen<CentrifugeMenu> {
    private final Identifier GUI = Identifier.tryBuild(MODID, "textures/gui/centrifuge.png");

    public CentrifugeScreen(CentrifugeMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        renderProgressArrow(graphics, relX, relY);
        renderPowerBar(graphics, relX, relY);
    }

    private void renderProgressArrow(GuiGraphicsExtractor graphics, int x, int y) {
        if (menu.isCrafting()) {
            com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, x + 60, y + 35, 176, 0, menu.getScaledProgress(), 18);
        }
    }

    private void renderPowerBar(GuiGraphicsExtractor graphics, int relX, int relY) {
        int powerScaled = getScaled(menu.getPower(), CentrifugeBlockEntity.CAPACITY, 70);
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI,
                relX + 163,
                relY + 8 + (70 - powerScaled),
                196,
                70 - powerScaled,
                5,
                powerScaled);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        extractTooltip(graphics, mouseX, mouseY);
    }

    public int getScaled(int value, int max, int scaleTo) {
        if (max == 0) return 0;
        return (int) Math.ceil((double) (value * scaleTo) / max);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int pX, int pY) {
        super.extractTooltip(graphics, pX, pY);
        if (pX >= leftPos + 163 && pX < leftPos + 163 + 5 && pY >= topPos + 8 && pY < topPos + 8 + 70) {
            int power = menu.getPower();
            com.accbdd.complicated_bees.port.PortGui.tooltip(graphics,
                    this.font,
                    List.of(Component.literal(power + " RF"),
                            Component.literal(menu.getPowerUsage() + " RF/t")
                                    .withStyle(ChatFormatting.GRAY)),
                    pX,
                    pY);
        }
    }
}
