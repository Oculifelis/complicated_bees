package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.gyrofuge.GyrofugeBasicProcessingUnitBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GyrofugeBasicProcessingUnitBlock extends AbstractGyrofugePoweredBlock {
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GyrofugeBasicProcessingUnitBlockEntity(pPos, pState);
    }
}
