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

public class ApiaryScreen extends AbstractContainerScreen<ApiaryMenu> {
    private final Identifier GUI = Identifier.tryBuild(MODID, "textures/gui/apiary.png");

    public ApiaryScreen(ApiaryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 187);
        this.inventoryLabelY = this.imageHeight - 10000;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        renderStatusBar(graphics, relX, relY);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        extractTooltip(graphics, mouseX, mouseY);
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
    }
}
