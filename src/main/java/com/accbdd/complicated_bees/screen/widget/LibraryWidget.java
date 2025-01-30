package com.accbdd.complicated_bees.screen.widget;

import com.accbdd.complicated_bees.genetics.GeneticHelper;
import com.accbdd.complicated_bees.genetics.Species;
import com.accbdd.complicated_bees.genetics.mutation.Mutation;
import com.accbdd.complicated_bees.genetics.tracking.BreedingTracker;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.registry.MutationRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import com.accbdd.complicated_bees.screen.LibraryMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.accbdd.complicated_bees.screen.LibraryScreen.GUI;

public class LibraryWidget extends AbstractScrollWidget {
    LibraryMenu menu;
    protected List<Mutation> possibleMutations = List.of();
    protected List<Mutation> researchedMutations = List.of();
    protected BreedingTracker tracker;
    public ItemStack hoveredStack = null;
    int mouseX = 0;
    int mouseY = 0;

    public LibraryWidget(int pX, int pY, int pWidth, int pHeight, LibraryMenu menu) {
        super(pX, pY, pWidth - 8, pHeight, Component.empty());
        this.menu = menu;
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
    protected void renderBorder(GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight) {

    }

    @Override
    protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        if (menu.getClientDirty())
            queryTracker();
        graphics.pose().pushPose();
        graphics.pose().translate(getX(), getY(), 0);
        this.hoveredStack = null;
        renderMutations(graphics, 2, 2);
        graphics.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    private void renderMutations(GuiGraphics graphics, int pX, int pY) {
        for (int i = 0; i < possibleMutations.size(); i++) {
            renderEquation(graphics, possibleMutations.get(i), pX, pY + 18 * i);
        }
    }

    private void renderEquation(GuiGraphics graphics, Mutation mutation, int pX, int pY) {
       drawSpecies(graphics, mutation.getFirstSpecies(), pX, pY);
       drawPlus(graphics, pX + 16, pY);
       drawSpecies(graphics, mutation.getSecondSpecies(), pX + 32, pY);
       drawEquals(graphics, pX + 48, pY);
       drawSpecies(graphics, mutation.getResultSpecies(), pX + 64, pY);
    }

    private void drawQuestionMark(GuiGraphics graphics, int pX, int pY) {
        graphics.blit(GUI, pX, pY, 0, 240, 16, 16);
    }

    private void drawPlus(GuiGraphics graphics, int pX, int pY) {
        graphics.blit(GUI, pX, pY, 16, 240, 16, 16);
    }

    private void drawEquals(GuiGraphics graphics, int pX, int pY) {
        graphics.blit(GUI, pX, pY, 32, 240, 16, 16);
    }

    private void drawSpecies(GuiGraphics graphics, Species species, int pX, int pY) {
        if (!tracker.getDiscoveredSpecies().contains(SpeciesRegistration.getResourceLocation(species))) {
            drawQuestionMark(graphics, pX, pY);
        } else {
            ItemStack stack = species.toStack(ItemsRegistration.DRONE.get());
            graphics.renderItem(stack, pX, pY);
            if (mouseX >= pX + getX() && mouseX <= pX + getX() + 16 && mouseY + scrollAmount() >= pY + getY() && mouseY + scrollAmount() <= pY + getY() + 16) {
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
        ResourceLocation species = ResourceLocation.tryParse(bee.getTag().getString(GeneticHelper.SPECIES));
        Registry<Mutation> mutationRegistry = GeneticHelper.getRegistryAccess().registry(MutationRegistration.MUTATION_REGISTRY_KEY).get();
        List<Mutation> mutations = mutationRegistry.stream().filter(
                mutation -> (mutation.getFirst().equals(species) || mutation.getSecond().equals(species))
        ).toList();
        List<Mutation> researched = tracker.getResearchedMutations().stream().filter(
                location -> mutationRegistry.get(location).getFirst().equals(species) || mutationRegistry.get(location).getSecond().equals(species)
        ).map(mutationRegistry::get).toList();

        possibleMutations = mutations;
        researchedMutations = researched;
    }
}
