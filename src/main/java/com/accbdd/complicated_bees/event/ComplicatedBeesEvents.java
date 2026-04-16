package com.accbdd.complicated_bees.event;

import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

public class ComplicatedBeesEvents {
    public static void onItemPickup(ItemEntityPickupEvent.Post event) {
        ItemStack stack = event.getCurrentStack();
        if (!stack.isEmpty()) {
            if(stack.is(ItemTagGenerator.BEE)) {
                BreedingTracker.getTracker(event.getPlayer()).discoverIndividual(stack);
            }
        }
    }
}
