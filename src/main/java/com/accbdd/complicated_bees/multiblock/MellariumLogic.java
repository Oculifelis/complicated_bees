package com.accbdd.complicated_bees.multiblock;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.block.entity.MellariumAbstractBlockEntity;
import com.accbdd.complicated_bees.block.entity.MellariumControllerBlockEntity;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MellariumLogic {
    private final Level level;
    private final BlockPos center;
    private final List<BlockPos> frameHousingBlocks = new ArrayList<>();
    private UUID owner;

    public MellariumLogic(Level level, BlockPos center, UUID owner) {
        this.level = level;
        this.center = center;
        this.owner = owner;
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof MellariumAbstractBlockEntity mellariumBlock) {
                mellariumBlock.setLogic(this);
                if (level.getBlockState(pos).is(BlocksRegistration.MELLARIUM_FRAME_HOUSING.get())) {
                    frameHousingBlocks.add(pos);
                }
            } else if (level.getBlockEntity(pos) instanceof MellariumControllerBlockEntity controller) {
                controller.setMellariumLogic(this);
                controller.setOwner(owner);
            } else {
                ComplicatedBees.LOGGER.error("built a mellarium with non-mellarium block at {}", pos);
            }
        }
        ComplicatedBees.LOGGER.debug("created new mellarium with center {}", center);
    }

    public void deconstruct() {
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof MellariumAbstractBlockEntity mellariumBlock) {
                mellariumBlock.setLogic(null);
            }
        }
        level.setBlock(center, BlocksRegistration.MELLARIUM_BASE.get().defaultBlockState(), 3);
        ComplicatedBees.LOGGER.debug("deconstructed mellarium with center {}", center);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public BlockPos getCenter() {
        return center;
    }

    public MellariumControllerBlockEntity getController() {
        if (level.getBlockEntity(center) instanceof MellariumControllerBlockEntity controller)
            return controller;
        return null;
    }

    public List<BlockPos> getFrameHousingBlocks() {
        return frameHousingBlocks;
    }
}
