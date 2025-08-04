package com.accbdd.complicated_bees.block.entity;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

public interface ICentrifuge {
    /**
     * @return The side-agnostic item handler for this centrifuge
     */
    LazyOptional<IItemHandler> getItemHandler();

    LazyOptional<IItemHandler> getInputItemHandler();

    LazyOptional<IItemHandler> getOutputItemHandler();

    LazyOptional<IItemHandler> getUpgradeItemHandler();

    LazyOptional<IEnergyStorage> getEnergyHandler();

    int getProgress();

    void setProgress(int value);

    int getMaxProgress();

    void setMaxProgress(int value);

    int getEnergyUsage();

    void setEnergyUsage(int value);
}
