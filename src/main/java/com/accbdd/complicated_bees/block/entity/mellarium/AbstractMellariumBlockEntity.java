package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.block.AbstractMellariumBlock;
import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * All mellarium blocks should extend this class
 */
public abstract class AbstractMellariumBlockEntity extends BlockEntity {
    private MellariumLogic logic;
    private BlockPos center;

    public AbstractMellariumBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public MellariumLogic getLogic() {
        return logic;
    }

    public void setLogic(MellariumLogic logic) {
        this.logic = logic;
        if (logic != null) {
            this.center = logic.getCenter();
            if (getBlockPos().getY() > center.getY())
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.top), 3);
            else
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.side), 3);
        } else {
            this.center = null;
            if (getLevel().getBlockState(getBlockPos()).getBlock() instanceof AbstractMellariumBlock) {
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
        if (this.center != null && getLevel().getBlockEntity(center) instanceof MellariumControllerBlockEntity controller) {
            setLogic(controller.getMellariumLogic());
        }
    }
}
