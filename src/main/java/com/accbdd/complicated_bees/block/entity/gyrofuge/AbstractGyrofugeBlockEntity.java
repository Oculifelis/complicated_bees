package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.block.AbstractGyrofugeBlock;
import com.accbdd.complicated_bees.multiblock.GyrofugeLogic;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractGyrofugeBlockEntity extends BlockEntity {
    private GyrofugeLogic logic;
    private BlockPos center;

    public AbstractGyrofugeBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public GyrofugeLogic getLogic() {
        return logic;
    }

    public void setLogic(GyrofugeLogic logic) {
        this.logic = logic;
        if (logic != null) {
            this.center = logic.getCenter();
            if (getBlockPos().getY() > center.getY())
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.top), 3);
            else
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.side), 3);
        } else {
            this.center = null;
            if (getLevel().getBlockState(getBlockPos()).getBlock() instanceof AbstractGyrofugeBlock) {
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.none), 3);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);
        if (this.logic != null) {
            pTag.putLong("logic_center", getLogic().getCenter().asLong());
        }
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);
        if (pTag.contains("logic_center")) {
            this.center = BlockPos.of(pTag.getLong("logic_center"));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.center != null && getLevel().getBlockEntity(center) instanceof GyrofugeControllerBlockEntity controller) {
            setLogic(controller.getGyrofugeLogic());
        }
    }
}
