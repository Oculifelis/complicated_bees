package com.accbdd.complicated_bees.block.entity.mellarium;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class AbstractPoweredMellariumBlockEntity extends AbstractMellariumBlockEntity implements IMellariumTickable {
    private boolean powered;

    public AbstractPoweredMellariumBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    /**
     * @return the amount of rf/tick this block uses
     */
    abstract int getPowerUsage();

    @Override
    public void onTick() {
        if (getLogic() != null && getLogic().getController() != null) {
            setPowered(getLogic().getEnergyStorage().extractEnergy(getPowerUsage(), false) >= getPowerUsage());
        }
    }

    public void setPowered(boolean value) {
        if (powered == value)
            return;
        powered = value;
        if (getLevel() != null) {
            if (value) {
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true), 3);
            } else {
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, false), 3);
            }
        }
    }

    public boolean getPowered() {
        return powered;
    }
}
