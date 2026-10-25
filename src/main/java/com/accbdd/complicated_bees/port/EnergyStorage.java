package com.accbdd.complicated_bees.port;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;

/** Keeps the upstream integer energy save field and simulation semantics. */
public class EnergyStorage extends net.neoforged.neoforge.energy.EnergyStorage {
    public void restoreStored(int amount) { energy = Math.clamp(amount, 0, capacity); }
    public EnergyStorage(int capacity) { super(capacity); }
    public EnergyStorage(int capacity, int maxTransfer) { super(capacity, maxTransfer); }
    public EnergyStorage(int capacity, int maxReceive, int maxExtract) { super(capacity, maxReceive, maxExtract); }
    public EnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) { super(capacity, maxReceive, maxExtract, energy); }
    public Tag serializeNBT(HolderLookup.Provider registries) { return IntTag.valueOf(energy); }
    public void deserializeNBT(HolderLookup.Provider registries, Tag tag) {
        energy = Math.clamp(tag instanceof NumericTag number ? number.intValue() : 0, 0, capacity);
    }
}
