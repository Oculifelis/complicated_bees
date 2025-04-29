package com.accbdd.complicated_bees.screen.slot;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FakeSpeciesSlot extends Slot {
    public FakeSpeciesSlot(int pX, int pY, String initialSpecies) {
        super(new SimpleContainer(1), 0, pX, pY);
        Species species = initialSpecies == null ? null : SpeciesRegistration.getFromResourceLocation(ResourceLocation.tryParse(initialSpecies));
        if (species != null)
            this.set(species.toStack(ItemsRegistration.DRONE.get()));
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
    }

    @Override
    public ItemStack remove(int pAmount) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return false;
    }

    public void set(ItemStack pStack, boolean primary) {
        if (pStack.is(ItemTagGenerator.BEE)) {
            Species species = GeneticHelper.getSpecies(pStack, primary);
            pStack = species.toStack(ItemsRegistration.DRONE.get());
        } else {
            pStack = ItemStack.EMPTY;
        }
        super.set(pStack);
    }

    @Override
    public boolean mayPickup(Player pPlayer) {
        return false;
    }
}
