package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MellariumAbstractBlockEntity extends BlockEntity {
    private MellariumLogic logic;

    public MellariumAbstractBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public MellariumLogic getLogic() {
        return logic;
    }

    public void setLogic(MellariumLogic logic) {
        this.logic = logic;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (getLogic() == null || getLogic().getController() == null)
            return super.getCapability(cap, side);

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return getLogic().getController().getItemHandler().cast();
            }
            if (side == Direction.DOWN) {
                return getLogic().getController().getOutputItemHandler().cast();
            }
            return getLogic().getController().getBeeItemHandler().cast();
        }

        return super.getCapability(cap, side);
    }
}
