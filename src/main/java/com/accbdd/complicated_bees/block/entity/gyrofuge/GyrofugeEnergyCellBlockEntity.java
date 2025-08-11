package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GyrofugeEnergyCellBlockEntity extends AbstractGyrofugeBlockEntity {

    public static final int BASE_STORAGE = ServerConfig.SERVER_CONFIG.gyrofugeCellStorage.get();
    public static final int BASE_TRANSFER = ServerConfig.SERVER_CONFIG.gyrofugeCellStorage.get();
    public static final String ENERGY_TAG = "energy";
    private final LazyOptional<IEnergyStorage> energyHandler;
    private final EnergyStorage energy;

    public GyrofugeEnergyCellBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_ENERGY_CELL_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.energy = new EnergyStorage(BASE_STORAGE, BASE_TRANSFER);
        this.energyHandler = LazyOptional.of(() -> energy);
    }

    public EnergyStorage getEnergy() {
        return energy;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY)
            return this.energyHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(ENERGY_TAG, energy.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(ENERGY_TAG))
            energy.deserializeNBT(tag.get(ENERGY_TAG));
    }
}
