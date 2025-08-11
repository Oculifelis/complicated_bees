package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MellariumBaseBlockEntity extends AbstractMellariumBlockEntity {
    public MellariumBaseBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_BASE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}
