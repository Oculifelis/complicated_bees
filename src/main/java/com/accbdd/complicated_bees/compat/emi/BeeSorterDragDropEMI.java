package com.accbdd.complicated_bees.compat.emi;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.screen.BeeSorterScreen;
import com.accbdd.complicated_bees.screen.slot.FakeSpeciesSlot;
import dev.emi.emi.api.EmiDragDropHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;

public class BeeSorterDragDropEMI implements EmiDragDropHandler<BeeSorterScreen> {
    @Override
    public boolean dropStack(BeeSorterScreen screen, EmiIngredient stack, int x, int y) {
        if (screen.getSlotUnderMouse() instanceof FakeSpeciesSlot fakeSlot) {
            for (EmiStack emiStack : stack.getEmiStacks()) {
                if (emiStack.getItemStack().is(ItemTagGenerator.BEE)) {
                    fakeSlot.set(GeneticHelper.getSpecies(emiStack.getItemStack(), true).toStack(ItemsRegistration.DRONE.get()));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(BeeSorterScreen screen, EmiIngredient dragged, GuiGraphics draw, int mouseX, int mouseY, float delta) {
        for (int i = 0; i < 36; i++) {
            Slot slot = screen.getMenu().getSlot(i);
            draw.fill(screen.getGuiLeft() + slot.x, screen.getGuiTop() + slot.y, screen.getGuiLeft()+ slot.x + 16, screen.getGuiTop() + slot.y + 16, 0x8822BB33);
        }
    }
}
