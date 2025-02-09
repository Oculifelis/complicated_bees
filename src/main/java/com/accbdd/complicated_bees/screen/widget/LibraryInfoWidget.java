package com.accbdd.complicated_bees.screen.widget;

import com.accbdd.complicated_bees.genetics.mutation.Mutation;
import com.accbdd.complicated_bees.screen.LibraryMenu;
import com.accbdd.complicated_bees.util.GuiHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class LibraryInfoWidget extends AbstractScrollWidget {
    private final LibraryMenu menu;
    private final LibraryMutationWidget mutationWidget;
    protected int infoPanelX, infoPanelY, innerHeight;

    public LibraryInfoWidget(int pX, int pY, int pWidth, int pHeight, LibraryMenu menu, LibraryMutationWidget mutationWidget) {
        super(pX, pY, pWidth - 8, pHeight, Component.empty());
        this.menu = menu;
        this.mutationWidget = mutationWidget;
        infoPanelX = getX() + getWidth() / 2;
        infoPanelY = getY() + 3;
        innerHeight = pHeight - 8;
    }

    @Override
    protected int getInnerHeight() {
        return innerHeight;
    }

    @Override
    protected double scrollRate() {
        return 8;
    }

    @Override
    protected void renderContents(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (!menu.getSlot(0).hasItem()) {
            GuiHelper.drawTopAlignedCenteredWrappedText(pGuiGraphics,
                    infoPanelX,
                    infoPanelY,
                    0xFFFFFF,
                    12,
                    98,
                    3,
                    Component.literal("Place a bee in the top right slot to see its mutations."));
            innerHeight = getHeight() - 8;
        } else if (mutationWidget.selected != -1 && mutationWidget.selected < mutationWidget.possibleMutations.size()) {
            renderMutationInfo(pGuiGraphics, mutationWidget.possibleMutations.get(mutationWidget.selected));
        } else {
            GuiHelper.drawTopAlignedCenteredWrappedText(pGuiGraphics,
                    infoPanelX,
                    infoPanelY,
                    0xFFFFFF,
                    12,
                    98,
                    3,
                    Component.literal("Select a mutation on the right to view detailed information."));
            innerHeight = getHeight() - 8;
        }
    }
    private void renderMutationInfo(GuiGraphics graphics, Mutation mutation) {
        int nextY = GuiHelper.drawTopAlignedCenteredWrappedText(graphics,
                infoPanelX,
                infoPanelY,
                0xFFFFFF,
                12,
                106,
                3,
                Component.literal("Info").withStyle(ChatFormatting.UNDERLINE));

        nextY -= 6;
        nextY = drawTextAndValue(graphics,
                nextY,
                0xFFFFFF,
                mutationWidget.selectedDiscovered ? 0x00FF00 : 0xFF0000,
                Component.literal("Discovered:"),
                Component.literal(mutationWidget.selectedDiscovered ? "✔" : "✘"));

        nextY -= 6;
        nextY = drawTextAndValue(graphics,
                nextY,
                0xFFFFFF,
                mutationWidget.selectedResearched ? 0x00FF00 : 0xFF0000,
                Component.literal("Researched:"),
                Component.literal(mutationWidget.selectedResearched ? "✔" : "✘"));

        nextY = drawTextAndValue(graphics,
                nextY,
                0xFFFFFF,
                0xFFFFFF,
                Component.literal("Chance:"),
                Component.literal(String.format("%.0f%%", Math.min(100, (mutationWidget.possibleMutations.get(mutationWidget.selected).getChance() * 100) + (mutationWidget.selectedResearched ? 20 : 0)))));

        nextY = GuiHelper.drawTopAlignedCenteredWrappedText(graphics,
                infoPanelX,
                nextY,
                0xFFFFFF,
                12,
                98,
                3,
                Component.literal("Conditions").withStyle(ChatFormatting.UNDERLINE));
        nextY -= 6;
        nextY = GuiHelper.drawWrappedText(graphics,
                getX(),
                nextY,
                0xFFFFFF,
                12,
                98,
                3,
                mutationWidget.possibleMutations.get(mutationWidget.selected).getConditions().stream().map(iMutationCondition -> Component.literal("⏵ ").append(iMutationCondition.getDescription())).toArray(Component[]::new));
        innerHeight = nextY - getY();
    }

    private int drawTextAndValue(GuiGraphics graphics, int y, int leftColor, int rightColor, Component left, Component right) {
        GuiHelper.drawRightAlignedText(graphics,
                getX() + getWidth() - 2,
                y,
                rightColor,
                right);
        return GuiHelper.drawWrappedText(graphics,
                getX(),
                y,
                leftColor,
                12,
                98,
                3,
                left);
    }

    @Override
    protected void renderBorder(GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight) {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
