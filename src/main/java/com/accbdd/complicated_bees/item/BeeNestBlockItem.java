package com.accbdd.complicated_bees.item;

import com.accbdd.complicated_bees.registry.BlocksRegistration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class BeeNestBlockItem extends BlockItem {
    public BeeNestBlockItem(Properties prop) {
        super(BlocksRegistration.BEE_NEST.get(), prop);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return Component.translatable("species.complicated_bees." + pStack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).getUnsafe().getString("species"))
                .append(" ")
                .append(super.getName(pStack));
    }
}
