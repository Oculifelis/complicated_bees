package com.accbdd.complicated_bees.multiblock;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.block.entity.CombinedEnergyStorage;
import com.accbdd.complicated_bees.block.entity.mellarium.AbstractMellariumBlockEntity;
import com.accbdd.complicated_bees.block.entity.mellarium.MellariumControllerBlockEntity;
import com.accbdd.complicated_bees.block.entity.mellarium.MellariumEnergyCellBlockEntity;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import java.util.*;

public class MellariumLogic {
    private final Level level;
    private final BlockPos center;
    private final IEnergyStorage energyStorage;
    private UUID owner;
    private final List<BlockPos> specialBlocks = new ArrayList<>();

    public MellariumLogic(Level level, BlockPos center, UUID owner) {
        this.level = level;
        this.center = center;
        this.owner = owner;
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        List<IEnergyStorage> energyStorages = new ArrayList<>(); //blocks that store energy
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof AbstractMellariumBlockEntity mellariumBlock) {
                mellariumBlock.setLogic(this);
                BlockState blockState = level.getBlockState(pos);
                if (!blockState.is(BlocksRegistration.MELLARIUM_BASE.get())) {
                    specialBlocks.add(pos);
                }
                if (mellariumBlock instanceof MellariumEnergyCellBlockEntity cell) {
                    energyStorages.add(cell.getEnergy());
                }
            } else if (level.getBlockEntity(pos) instanceof MellariumControllerBlockEntity controller) {
                controller.setMellariumLogic(this);
                controller.setOwner(owner);
            } else {
                ComplicatedBees.LOGGER.warn("built a mellarium with non-mellarium block at {}", pos);
            }
        }
        energyStorage = new CombinedEnergyStorage(energyStorages.toArray(IEnergyStorage[]::new));
    }

    public void deconstruct(BlockPos pos) {
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        getController().ifPresent(controller -> {
            while (!controller.getOutputBuffer().empty()) {
                ItemStack stack = controller.getOutputBuffer().pop();
                if (stack.is(ItemTagGenerator.BEE))
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
            IItemHandler handler = controller.getItemHandler().orElseThrow(() -> new RuntimeException("no item handler found!"));
            for (int i = 0; i < handler.getSlots(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
            }
        });
        while (iterator.hasNext()) {
            BlockPos p = iterator.next();
            if (level.getBlockEntity(p) instanceof AbstractMellariumBlockEntity mellariumBlock) {
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

    public Optional<MellariumControllerBlockEntity> getController() {
        if (level.getBlockEntity(center) instanceof MellariumControllerBlockEntity controller)
            return Optional.of(controller);
        return Optional.empty();
    }

    public List<BlockPos> getSpecialBlocks() {
        return specialBlocks;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }
}
