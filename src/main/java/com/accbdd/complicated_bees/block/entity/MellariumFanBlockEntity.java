package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MellariumFanBlockEntity extends MellariumAbstractBlockEntity {
    public MellariumFanBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_FAN_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}
