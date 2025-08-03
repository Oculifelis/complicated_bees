package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MellariumEnergyCellBlockEntity extends MellariumAbstractBlockEntity {
    public MellariumEnergyCellBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_ENERGY_CELL_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (getLogic() == null || getLogic().getController() == null)
            return super.getCapability(cap, side);

        if (cap == ForgeCapabilities.ENERGY)
            return getLogic().getController().getEnergyHandler().cast();

        return super.getCapability(cap, side);
    }
}
