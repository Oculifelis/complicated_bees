package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.HangingSignBlockEntityCB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class CeilingHangingSignBlockCB extends CeilingHangingSignBlock {
    public CeilingHangingSignBlockCB(Properties pProperties, WoodType pType) {
        super(pProperties, pType);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new HangingSignBlockEntityCB(pPos, pState);
    }
}
