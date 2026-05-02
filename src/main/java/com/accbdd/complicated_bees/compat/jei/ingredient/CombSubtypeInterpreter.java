package com.accbdd.complicated_bees.compat.jei.ingredient;

import com.accbdd.complicated_bees.item.CombItem;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CombSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(ItemStack comb, UidContext context) {
        return CombItem.getComb(comb);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack comb, UidContext context) {
        return "";
    }
}
