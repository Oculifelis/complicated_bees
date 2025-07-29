package com.accbdd.complicated_bees.block.entity.mellarium;

/**
 * Interface for mellarium blocks that do something every bee tick
 */
public interface IMellariumTickable {
    /**
     * Called every bee tick by the controller
     */
    default void onBeeTick() {
    }

    /**
     * Called when the queen dies by the controller
     */
    default void onDeath() {
    }

    /**
     * Called every server tick by the controller
     */
    default void onTick() {
    }
}
