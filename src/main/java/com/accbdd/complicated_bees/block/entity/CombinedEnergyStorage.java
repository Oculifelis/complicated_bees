package com.accbdd.complicated_bees.block.entity;

import net.minecraftforge.energy.IEnergyStorage;

/**
 * Combines multiple IEnergyStorages into one interface
 */
public class CombinedEnergyStorage implements IEnergyStorage {
    private final IEnergyStorage[] storages;

    public CombinedEnergyStorage(IEnergyStorage... storages) {
        this.storages = storages;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int toInsert = maxReceive;
        for (IEnergyStorage storage : storages) {
            if (maxReceive <= 0)
                break;
            maxReceive -= storage.receiveEnergy(maxReceive, simulate);;
        }
        return toInsert - maxReceive;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = maxExtract;
        for (IEnergyStorage storage : storages) {
            if (maxExtract <= 0)
                break;
            maxExtract -= storage.extractEnergy(maxExtract, simulate);
        }
        return extracted - maxExtract;
    }

    @Override
    public int getEnergyStored() {
        int stored = 0;
        for (IEnergyStorage storage : storages) {
            stored += storage.getEnergyStored();
        }
        return stored;
    }

    @Override
    public int getMaxEnergyStored() {
        int maxStored = 0;
        for (IEnergyStorage storage : storages) {
            maxStored += storage.getMaxEnergyStored();
        }
        return maxStored;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
