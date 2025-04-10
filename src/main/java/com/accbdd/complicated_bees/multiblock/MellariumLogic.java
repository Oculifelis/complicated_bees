package com.accbdd.complicated_bees.multiblock;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.block.entity.mellarium.MellariumAbstractBlockEntity;
import com.accbdd.complicated_bees.block.entity.mellarium.MellariumControllerBlockEntity;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MellariumLogic {
    private final Level level;
    private final BlockPos center;
    private final List<BlockPos> specialBlocks = new ArrayList<>();
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
                if (!level.getBlockState(pos).is(BlocksRegistration.MELLARIUM_BASE.get())) {
                    specialBlocks.add(pos);
                }
            } else if (level.getBlockEntity(pos) instanceof MellariumControllerBlockEntity controller) {
                controller.setMellariumLogic(this);
                controller.setOwner(owner);
            } else {
                ComplicatedBees.LOGGER.warn("built a mellarium with non-mellarium block at {}", pos);
            }
        }
    }

    public void deconstruct(BlockPos pos) {
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        if (getController() != null) {
            while (getController() != null && !getController().getOutputBuffer().empty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), getController().getOutputBuffer().pop());
            }
            IItemHandler handler = getController().getItemHandler().orElseThrow(() -> new RuntimeException("no item handler found!"));
            for (int i = 0; i < handler.getSlots(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
            }
        }
        while (iterator.hasNext()) {
            BlockPos p = iterator.next();
            if (level.getBlockEntity(p) instanceof MellariumAbstractBlockEntity mellariumBlock) {
                mellariumBlock.setLogic(null);
            }
        }
        level.setBlock(center, BlocksRegistration.MELLARIUM_BASE.get().defaultBlockState().setValue(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.none), 3);
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

    public List<BlockPos> getSpecialBlocks() {
        return specialBlocks;
    }
}
