package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.screen.BeeSorterScreen;
import com.accbdd.complicated_bees.screen.slot.FakeSpeciesSlot;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BeeSorterDragDropJEI implements IGhostIngredientHandler<BeeSorterScreen> {

    @Override
    public <I> List<Target<I>> getTargetsTyped(BeeSorterScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        List<Target<I>> targets = new ArrayList<>();
        if (ingredient.getItemStack().get().is(ItemTagGenerator.BEE)) {
            for (int i = 0; i < 36; i++) {
                Slot slot = gui.getMenu().getSlot(i);
                if (slot instanceof FakeSpeciesSlot fakeSlot)
                    targets.add(new Target<>() {
                        @Override
                        public Rect2i getArea() {
                            return new Rect2i(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 16, 16);
                        }

                        @Override
                        public void accept(I ingredient) {
                            fakeSlot.set(GeneticHelper.getSpecies((ItemStack) ingredient, true).toStack(ItemsRegistration.DRONE.get()));
                        }
                    });
            }
        }
        return targets;
    }

    @Override
    public void onComplete() {

    }
}
