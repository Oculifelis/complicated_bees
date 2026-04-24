package com.accbdd.complicated_bees.block.entity;


import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public interface ICentrifuge {
    /**
     * @return The side-agnostic item handler for this centrifuge
     */
    IItemHandler getItemHandler();

    IItemHandler getInputItemHandler();

    IItemHandler getOutputItemHandler();

    IItemHandler getUpgradeItemHandler();

    IEnergyStorage getEnergyHandler();

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
