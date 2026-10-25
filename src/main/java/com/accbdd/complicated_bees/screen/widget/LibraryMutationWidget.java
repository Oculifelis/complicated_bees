package com.accbdd.complicated_bees.screen.widget;

import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.component.Bee;
import com.accbdd.complicated_bees.recipe.mutation.MutationRecipe;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import com.accbdd.complicated_bees.screen.LibraryMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import com.accbdd.complicated_bees.port.ScrollWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

import static com.accbdd.complicated_bees.screen.LibraryScreen.GUI;

public class LibraryMutationWidget extends ScrollWidget {
    LibraryMenu menu;
    protected List<RecipeHolder<MutationRecipe>> possibleMutations = List.of();
    protected List<RecipeHolder<MutationRecipe>> researchedMutations = List.of();
    protected BreedingTracker tracker;
    protected LibraryInfoWidget infoWidget;
    public ItemStack hoveredStack = null;
    int mouseX = 0;
    int mouseY = 0;
    protected int selected;
    protected boolean selectedResearched, selectedDiscovered;

    public LibraryMutationWidget(int pX, int pY, int pWidth, int pHeight, LibraryMenu menu) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.menu = menu;
        this.selected = -1;
        selectedResearched = false;
        selectedDiscovered = false;
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        double pMouseX=event.x(), pMouseY=event.y(); int pButton=event.button();
        this.selected = getMutationIndexAt(pMouseX - getX(), pMouseY - getY());
        if (selected != -1) {
            this.selectedDiscovered = tracker.getDiscoveredMutations().contains(possibleMutations.get(selected).id().identifier());
            this.selectedResearched = tracker.getResearchedMutations().contains(possibleMutations.get(selected).id().identifier());
        }
        return super.mouseClicked(event, doubleClick);
    }

    private int getMutationIndexAt(double x, double y) {
        int indexClicked = (int) ((y + scrollAmount()) / 18);
        if (indexClicked >= 0 && 0 < x && x < 107 && indexClicked < possibleMutations.size()) {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            return indexClicked;
        }
        if (selected != -1)
            this.playDownSound(Minecraft.getInstance().getSoundManager());
        return -1;
    }

    @Override
    protected int getInnerHeight() {
        return possibleMutations.size() * 18;
    }

    @Override
    protected double scrollRate() {
        return 8;
    }

    @Override
    protected void renderBorder(GuiGraphicsExtractor pGuiGraphicsExtractor, int pX, int pY, int pWidth, int pHeight) {

    }

    @Override
    protected void renderDecorations(GuiGraphicsExtractor pGuiGraphicsExtractor) {

    }

    @Override
    protected void renderContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        if (menu.getClientDirty())
            queryTracker();
        graphics.pose().pushMatrix();
        graphics.pose().translate(getX(), getY());
        this.hoveredStack = null;
        renderMutations(graphics, 2, 2);
        graphics.pose().popMatrix();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    private void renderMutations(GuiGraphicsExtractor graphics, int pX, int pY) {
        //todo: render different colored question marks for species status (discovered, unknown, etc)
        for (int i = 0; i < possibleMutations.size(); i++) {
            if (this.selected == i) {
                graphics.fill(pX - 1, pY + 18 * i - 1, pX + getWidth(), pY + 18 * (i + 1) - 1, 0xFF666666);
                com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, pX + 93, pY + 18 * i, 48, 240, 9, 16);
            }
            renderEquation(graphics, possibleMutations.get(i), pX, pY + 18 * i);
        }
    }

    private void renderEquation(GuiGraphicsExtractor graphics, RecipeHolder<MutationRecipe> mutation, int pX, int pY) {
        boolean flag = tracker.getResearchedMutations().contains(mutation.id().identifier());
        MutationRecipe recipe = mutation.value();
        drawSpecies(graphics, recipe.getFirstSpecies(), pX, pY, flag);
        drawPlus(graphics, pX + 16, pY);
        drawSpecies(graphics, recipe.getSecondSpecies(), pX + 32, pY, flag);
        drawEquals(graphics, pX + 48, pY);
        drawSpecies(graphics, recipe.getResultSpecies(), pX + 64, pY, flag);
    }

    private void drawQuestionMark(GuiGraphicsExtractor graphics, int pX, int pY) {
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, pX, pY, 0, 240, 16, 16);
    }

    private void drawPlus(GuiGraphicsExtractor graphics, int pX, int pY) {
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, pX, pY, 16, 240, 16, 16);
    }

    private void drawEquals(GuiGraphicsExtractor graphics, int pX, int pY) {
        com.accbdd.complicated_bees.port.PortGui.blit(graphics,GUI, pX, pY, 32, 240, 16, 16);
    }

    private void drawSpecies(GuiGraphicsExtractor graphics, Species species, int pX, int pY, boolean flag) {
        if (!tracker.getDiscoveredSpecies().contains(SpeciesRegistration.getIdentifier(species)) && !flag) {
            drawQuestionMark(graphics, pX, pY);
        } else {
            ItemStack stack = species.toStack(ItemsRegistration.DRONE.get());
            graphics.item(stack, pX, pY);
            if (mouseX >= pX + getX() && mouseX <= pX + getX() + 16 &&
                    mouseY + scrollAmount() >= pY + getY() && mouseY + scrollAmount() <= pY + getY() + 16 &&
                    mouseY >= getY() && mouseY <= getY() + getHeight()) {
                this.hoveredStack = stack;
            }
        }
    }

    private void queryTracker() {
        menu.setClientDirty(false);
        if (Minecraft.getInstance().player == null)
            return;
        this.tracker = BreedingTracker.getTracker(Minecraft.getInstance().player.getUUID());
        if (tracker == null)
            return;
        ItemStack bee = menu.getSlot(0).getItem();
        if (bee.isEmpty()) {
            possibleMutations = List.of();
            researchedMutations = List.of();
            return;
        }
        Identifier species = bee.getOrDefault(EsotericRegistration.BEE, Bee.DEFAULT).species();
        List<RecipeHolder<MutationRecipe>> mutationRecipes = com.accbdd.complicated_bees.port.PortRecipes.manager(Minecraft.getInstance().level).getAllRecipesFor(EsotericRegistration.MUTATION_RECIPE.get());

        possibleMutations = mutationRecipes.stream().filter(holder ->
                holder.value() instanceof MutationRecipe mutation && (mutation.getFirst().equals(species) || mutation.getSecond().equals(species))).toList();
        researchedMutations = possibleMutations.stream().filter(holder ->
                tracker.getResearchedMutations().contains(holder.id().identifier())).toList();
    }
}
