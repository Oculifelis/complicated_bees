package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumOutputHatchBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MellariumOutputHatchBlock extends MellariumBlock {
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MellariumOutputHatchBlockEntity(pPos, pState);
    }
}
