package com.accbdd.complicated_bees.block.entity.gyrofuge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class AbstractPoweredGyrofugeBlockEntity extends AbstractGyrofugeBlockEntity implements IGyrofugeTickable {
    private boolean powered;

    public AbstractPoweredGyrofugeBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    /**
     * @return the amount of rf this block uses every tick
     */
    public abstract int getPowerUsage();

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
