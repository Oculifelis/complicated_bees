package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SignBlockEntityCB extends SignBlockEntity {
    public SignBlockEntityCB(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.CB_SIGN_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BlockEntitiesRegistration.CB_SIGN_ENTITY.get();
    }
}
