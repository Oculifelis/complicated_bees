package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumSkyboxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MellariumSkyboxBlock extends AbstractMellariumPoweredBlock {
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MellariumSkyboxBlockEntity(pPos, pState);
    }
}
