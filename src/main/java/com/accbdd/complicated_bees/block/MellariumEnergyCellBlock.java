package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumEnergyCellBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MellariumEnergyCellBlock extends MellariumBlock {
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MellariumEnergyCellBlockEntity(pPos, pState);
    }
}
