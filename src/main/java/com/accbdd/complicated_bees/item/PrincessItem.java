package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.component.Bee;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.world.item.ItemStack;

public class PrincessItem extends BeeItem {
    public PrincessItem(Properties prop) {
        super(prop.stacksTo(1));
    }

    public static int getGeneration(ItemStack stack) {
        return stack.getOrDefault(EsotericRegistration.BEE, Bee.DEFAULT).generation();
    }

    public static void setGeneration(ItemStack stack, int gen) {
        stack.update(EsotericRegistration.BEE, Bee.DEFAULT, bee -> bee.withGeneration(gen));
    }
}
