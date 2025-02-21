package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MellariumBaseBlockEntity extends BlockEntity {
    private MellariumLogic logic;

    public MellariumBaseBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_BASE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }


    public MellariumLogic getLogic() {
        return logic;
    }

    public void setLogic(MellariumLogic logic) {
        this.logic = logic;
    }
}
