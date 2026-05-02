package com.accbdd.complicated_bees.compat.jei.ingredient;

import com.accbdd.complicated_bees.component.Bee;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BeeSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(ItemStack bee, UidContext context) {
        return bee.getOrDefault(EsotericRegistration.BEE.get(), Bee.DEFAULT).species();
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack bee, UidContext context) {
        return "";
    }
}
