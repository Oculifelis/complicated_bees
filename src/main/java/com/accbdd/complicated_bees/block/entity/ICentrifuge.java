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

    /**
     * @return the rf/t this centrifuge uses while active
     */
    int getActiveEnergyUsage();

    /**
     * @return the rf/t this centrifuge uses while idle
     */
    int getIdleEnergyUsage();

    /**
     * @return the rf/t this centrifuge is currently using
     */
    int getEnergyUsage();

    /**
     * @return whether this centrifuge is crafting
     */
    boolean isCrafting();

    void setActiveEnergyUsage(int value);

    void setIdleEnergyUsage(int value);

    void setEnergyUsage(int value);
}
