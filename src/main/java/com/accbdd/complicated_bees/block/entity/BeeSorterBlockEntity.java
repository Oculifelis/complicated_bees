package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BeeSorterBlockEntity extends BlockEntity {
    public BeeSorterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.BEE_SORTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }


}
