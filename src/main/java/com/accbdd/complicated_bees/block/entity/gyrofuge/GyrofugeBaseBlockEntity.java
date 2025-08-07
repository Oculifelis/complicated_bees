package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GyrofugeBaseBlockEntity extends GyrofugeAbstractBlockEntity {
    public GyrofugeBaseBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_BASE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}
