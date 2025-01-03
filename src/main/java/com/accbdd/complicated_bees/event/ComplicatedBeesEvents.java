package com.accbdd.complicated_bees.event;

import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.genetics.GeneticHelper;
import com.accbdd.complicated_bees.genetics.tracking.ServerBreedingTracker;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class ComplicatedBeesEvents {
    public static void onItemPickup(EntityItemPickupEvent event) {
        ItemStack stack = event.getItem().getItem();
        if (!stack.isEmpty()) {
            if(stack.is(ItemTagGenerator.BEE)) {
                ServerBreedingTracker.getTracker(event.getEntity()).discover(GeneticHelper.getSpecies(stack, true));
                ServerBreedingTracker.getTracker(event.getEntity()).discover(GeneticHelper.getSpecies(stack, false));
            }
        }
    }
}
