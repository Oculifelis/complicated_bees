package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GyrofugeBasicProcessingUnitBlockEntity extends AbstractGyrofugeProcessingUnitBlockEntity {
    public GyrofugeBasicProcessingUnitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_BASIC_PROCESSING_UNIT_BLOCK_ENTITY.get(), pPos, pBlockState, 1);
    }
}
