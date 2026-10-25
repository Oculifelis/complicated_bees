package com.accbdd.complicated_bees.screen;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.gene.GeneLifespan;
import com.accbdd.complicated_bees.bees.gene.enums.EnumLifespan;
import com.accbdd.complicated_bees.item.BeeItem;
import com.accbdd.complicated_bees.util.enums.EnumErrorCodes;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MellariumScreen extends AbstractContainerScreen<MellariumMenu> {
    private final Identifier GUI = Identifier.tryBuild(MODID, "textures/gui/mellarium.png");

    public MellariumScreen(MellariumMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 187);
        this.inventoryLabelY = this.imageHeight - 10000;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        renderStatusBar(graphics, relX, relY);
        if (menu.getMaxPower() > 0)
            renderPowerBar(graphics, relX, relY);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        extractTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor pGuiGraphicsExtractor, int pMouseX, int pMouseY) {
        pGuiGraphicsExtractor.text(this.font, this.title, this.imageWidth/2 - this.font.width(this.title)/2, this.titleLabelY, 4210752, false);
        pGuiGraphicsExtractor.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    public void renderStatusBar(GuiGraphicsExtractor graphics, int x, int y) {
        if (menu.hasQueen()) {
            ItemStack queen = menu.getQueen();
            int lifespan = ((EnumLifespan) GeneticHelper.getChromosome(queen, true).getGene(GeneLifespan.ID).get()).value;
            int progress = menu.getScaledProgress(BeeItem.getAge(queen), lifespan);
            int errorCode = menu.getData().get(2);
            boolean errorStatus = errorCode > 0 && errorCode != EnumErrorCodes.ECSTATIC.value;
            int barColor = errorStatus ? 182 : errorCode == 128 ? 176 : 179;
            com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI,
                    x + 18,
                    y + 36 + (errorStatus ? 0 : progress),
                    barColor,
                    errorStatus ? 0 : progress,
                    3,
                    45 - (errorStatus ? 0 : progress));
        } else if (menu.isBreeding()) {
            int progress = menu.getScaledProgress(menu.getData().get(0), menu.getData().get(1));
            com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI,
                    x + 18,
                    y + 81 - progress,
                    179,
                    0,
                    3,
                    progress);
        } else if (menu.hasQueue()) {
            com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI,
                    x + 18,
                    y + 36,
                    182,
                    0,
                    3,
                    45);
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor pGuiGraphicsExtractor, int pX, int pY) {
        super.extractTooltip(pGuiGraphicsExtractor, pX, pY);
        int errorFlags = menu.getData().get(2);
        int relMouseX = pX - (this.width - this.imageWidth) / 2;
        int relMouseY = pY - (this.height - this.imageHeight) / 2;
        if (menu.hasQueen() && (16 < relMouseX) && (relMouseX < 22) && (34 < relMouseY) && (relMouseY < 82)) {
            if (errorFlags > 0) {
                List<Component> errors = new ArrayList<>();
                for (EnumErrorCodes errorCode : EnumErrorCodes.values()) {
                    if ((errorFlags & errorCode.value) != 0)
                        errors.add(errorCode.detailGetter.apply(menu.getQueen(), menu.getConditions()));
                }
                com.accbdd.complicated_bees.port.PortGui.tooltip(pGuiGraphicsExtractor,this.font, errors, Optional.empty(), pX, pY);
            } else {
                com.accbdd.complicated_bees.port.PortGui.tooltip(pGuiGraphicsExtractor,this.font, Component.translatable("gui.complicated_bees.error.none"), pX, pY);
            }
        }
        if (pX >= leftPos + 163 && pX < leftPos + 163 + 5 && pY >= topPos + 23 && pY < topPos + 23 + 70) {
            int power = menu.getPower();
            List<Component> powerTooltip = new ArrayList<>();
            powerTooltip.add(Component.literal(power + " RF"));
//            powerTooltip.add(Component.literal(menu.getPowerUsage() + " RF/t")
//                    .withStyle(ChatFormatting.GRAY));
//            powerTooltip.addAll(menu.modifier.getTooltipComponents());
            com.accbdd.complicated_bees.port.PortGui.tooltip(pGuiGraphicsExtractor,
                    this.font,
                    powerTooltip,
                    pX,
                    pY);
        }
    }


    private void renderPowerBar(GuiGraphicsExtractor graphics, int relX, int relY) {
        int powerScaled = getScaled(menu.getPower(), menu.getMaxPower(), 70);
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI,
                relX + 163,
                relY + 23,
                190,
                0,
                7,
                72);
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI,
                relX + 164,
                relY + 24 + (70 - powerScaled),
                185,
                70 - powerScaled,
                5,
                powerScaled);
    }

    public int getScaled(int value, int max, int scaleTo) {
        if (max == 0) return 0;
        return (int) Math.ceil((double) (value * scaleTo) / max);
    }
}
