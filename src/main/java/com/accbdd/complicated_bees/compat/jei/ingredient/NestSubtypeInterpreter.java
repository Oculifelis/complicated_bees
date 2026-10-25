package com.accbdd.complicated_bees.compat.jei.ingredient;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

public class NestSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(ItemStack nest, UidContext context) {
        return com.accbdd.complicated_bees.port.PortData.blockTag(nest).getStringOr("species", "");
    }

    public String getLegacyStringSubtypeInfo(ItemStack nest, UidContext context) {
        return "";
    }
}
