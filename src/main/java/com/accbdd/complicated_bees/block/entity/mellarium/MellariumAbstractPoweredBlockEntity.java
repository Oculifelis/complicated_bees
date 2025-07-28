package com.accbdd.complicated_bees.block.entity.mellarium;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class MellariumAbstractPoweredBlockEntity extends MellariumAbstractBlockEntity {
    private boolean powered;

    public MellariumAbstractPoweredBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public void setPowered(boolean value) {
        if (powered == value)
            return;
        powered = value;
        if (getLevel() != null) {
            if (value) {
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true), 3);
            } else {
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, false), 3);
            }
        }
    }

    public boolean getPowered() {
        return powered;
    }
}
