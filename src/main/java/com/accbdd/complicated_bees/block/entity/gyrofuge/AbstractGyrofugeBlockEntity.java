package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.block.AbstractGyrofugeBlock;
import com.accbdd.complicated_bees.multiblock.GyrofugeLogic;
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

import java.util.Optional;

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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        GyrofugeLogic logic = getLogic();
        if (logic == null) {
            return super.getCapability(cap, side);
        }
        Optional<GyrofugeControllerBlockEntity> controller = logic.getController();
        if (controller.isEmpty()) {
            return super.getCapability(cap, side);
        }

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return controller.map(GyrofugeControllerBlockEntity::getItemHandler).orElse(LazyOptional.empty()).cast();
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
        if (this.center != null && getLevel().getBlockEntity(center) instanceof GyrofugeControllerBlockEntity controller) {
            setLogic(controller.getGyrofugeLogic());
        }
    }
}
