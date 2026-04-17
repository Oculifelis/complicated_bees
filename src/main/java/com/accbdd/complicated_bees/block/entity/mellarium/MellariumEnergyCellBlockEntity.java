package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.forge.LazyOptional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MellariumEnergyCellBlockEntity extends AbstractMellariumBlockEntity {

    public static final int BASE_STORAGE = ServerConfig.SERVER_CONFIG.mellariumCellStorage.get();
    public static final int BASE_TRANSFER = ServerConfig.SERVER_CONFIG.mellariumCellTransfer.get();
    public static final String ENERGY_TAG = "energy";
    private final LazyOptional<IEnergyStorage> energyHandler;
    private final EnergyStorage energy;

    public MellariumEnergyCellBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_ENERGY_CELL_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.energy = new EnergyStorage(BASE_STORAGE, BASE_TRANSFER);
        this.energyHandler = LazyOptional.of(() -> energy);
    }

    public EnergyStorage getEnergy() {
        return energy;
    }
    
    public LazyOptional<IEnergyStorage> getEnergyHandler() {
        return energyHandler;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY)
            return this.energyHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ENERGY_TAG, energy.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(ENERGY_TAG))
            energy.deserializeNBT(registries, tag.get(ENERGY_TAG));
    }
}
