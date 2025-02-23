package com.accbdd.complicated_bees.multiblock;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.block.entity.MellariumBaseBlockEntity;
import com.accbdd.complicated_bees.block.entity.MellariumControllerBlockEntity;
import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class MellariumLogic {
    private final Level level;
    private final BlockPos center;
    private final UUID owner;

    public MellariumLogic(Level level, BlockPos center, UUID owner) {
        this.level = level;
        this.center = center;
        this.owner = owner;
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof MellariumBaseBlockEntity mellariumBase) {
                mellariumBase.setLogic(this);
            } else {
                ComplicatedBees.LOGGER.error("built a mellarium with non-mellarium block at {}", pos);
            }
        }
        ComplicatedBees.LOGGER.debug("built new mellarium with center {}", center);
    }

    public void deconstruct() {
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof MellariumBaseBlockEntity mellariumBase) {
                mellariumBase.setLogic(null);
            }
        }
        ComplicatedBees.LOGGER.debug("deconstructed mellarium with center {}", center);
    }

    public UUID getOwner() {
        return owner;
    }

    public BlockPos getCenter() {
        return center;
    }

    public MellariumControllerBlockEntity getController() {
        if (level.getBlockEntity(center) instanceof MellariumControllerBlockEntity controller)
            return controller;
        return null;
    }
}
