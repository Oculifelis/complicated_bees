package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.bees.BeeLogic;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Stack;

public class MellariumControllerBlockEntity extends BaseBeeHousing {
    public MellariumControllerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_CONTROLLER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public ItemStackHandler getBeeItems() {
        return null;
    }

    @Override
    public ItemStackHandler getOutputItems() {
        return null;
    }

    @Override
    public ItemStackHandler getFrameItems() {
        return null;
    }

    @Override
    public LazyOptional<IItemHandler> getItemHandler() {
        return null;
    }

    @Override
    public LazyOptional<IItemHandler> getBeeItemHandler() {
        return null;
    }

    @Override
    public LazyOptional<IItemHandler> getOutputItemHandler() {
        return null;
    }

    @Override
    public LazyOptional<IItemHandler> getFrameItemHandler() {
        return null;
    }

    @Override
    public Stack<ItemStack> getOutputBuffer() {
        return null;
    }

    @Override
    public BeeLogic getLogic() {
        return null;
    }
}
