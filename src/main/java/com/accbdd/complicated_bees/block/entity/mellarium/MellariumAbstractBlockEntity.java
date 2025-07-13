package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.block.AbstractMellariumBlock;
import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * All mellarium blocks should extend this class
 */
public abstract class MellariumAbstractBlockEntity extends BlockEntity {
    private MellariumLogic logic;
    private BlockPos center;

    public MellariumAbstractBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (getLogic() == null || getLogic().getController() == null)
            return super.getCapability(cap, side);

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return getLogic().getController().getItemHandler().cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (this.logic != null) {
            pTag.putLong("logic_center", getLogic().getCenter().asLong());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
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
