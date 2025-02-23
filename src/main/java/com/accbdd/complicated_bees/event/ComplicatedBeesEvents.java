package com.accbdd.complicated_bees.event;

import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class ComplicatedBeesEvents {
    public static void onItemPickup(EntityItemPickupEvent event) {
        ItemStack stack = event.getItem().getItem();
        if (!stack.isEmpty()) {
            if(stack.is(ItemTagGenerator.BEE)) {
                BreedingTracker.getTracker(event.getEntity()).discoverIndividual(stack);
            }
        }
    }
}
