package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.SignBlockEntityCB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class StandingSignBlockCB extends StandingSignBlock {
    public StandingSignBlockCB(Properties pProperties, WoodType pType) {
        super(pProperties, pType);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SignBlockEntityCB(pPos, pState);
    }
}
