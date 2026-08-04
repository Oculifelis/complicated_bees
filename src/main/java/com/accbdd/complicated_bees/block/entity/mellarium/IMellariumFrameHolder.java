package com.accbdd.complicated_bees.block.entity.mellarium;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * interface for mellarium blocks that hold frames
 */
public interface IMellariumFrameHolder {
    /**
     * @return a list of all frames stored in this frame holder
     */
    List<ItemStack> getFrames();

    /**
     * damage every frame in this frame holder
     *
     * @param damageAmount the amount to damgge each frame
     */
    void damageFrames(int damageAmount);

    /**
     * @param item the frame type to damage
     * @param damageAmount the amount to damage
     */
    void damageFrames(Item item, int damageAmount);
}
