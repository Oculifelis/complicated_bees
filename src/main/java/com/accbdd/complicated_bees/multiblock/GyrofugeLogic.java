package com.accbdd.complicated_bees.multiblock;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.block.entity.CombinedEnergyStorage;
import com.accbdd.complicated_bees.block.entity.gyrofuge.GyrofugeAbstractBlockEntity;
import com.accbdd.complicated_bees.block.entity.gyrofuge.GyrofugeControllerBlockEntity;
import com.accbdd.complicated_bees.block.entity.gyrofuge.GyrofugeEnergyCellBlockEntity;
import com.accbdd.complicated_bees.block.entity.gyrofuge.IGyrofugeModifier;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class GyrofugeLogic {
    private final Level level;
    private final BlockPos center;
    private final IEnergyStorage energyStorage;
    private final List<BlockPos> specialBlocks = new ArrayList<>();

    public GyrofugeLogic(Level level, BlockPos center) {
        this.level = level;
        this.center = center;
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        List<IEnergyStorage> energyStorages = new ArrayList<>(); //blocks that store energy
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof GyrofugeAbstractBlockEntity gyrofugeBlock) {
                gyrofugeBlock.setLogic(this);
                BlockState blockState = level.getBlockState(pos);
                if (!blockState.is(BlocksRegistration.GYROFUGE_BASE.get())) {
                    specialBlocks.add(pos);
                }
                if (gyrofugeBlock instanceof GyrofugeEnergyCellBlockEntity cell) {
                    energyStorages.add(cell.getEnergy());
                }
            } else if (level.getBlockEntity(pos) instanceof GyrofugeControllerBlockEntity controller) {
                controller.setLogic(this);
            } else {
                ComplicatedBees.LOGGER.warn("built a gyrofuge with non-gyrofuge block at {}", pos);
            }
        }
        energyStorage = new CombinedEnergyStorage(energyStorages.toArray(IEnergyStorage[]::new));
    }

    public void deconstruct(BlockPos pos) {
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        if (getController() != null) {
            while (getController() != null && !getController().outputBuffer.empty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), getController().outputBuffer.pop());
            }
            IItemHandler handler = getController().getItemHandler().orElseThrow(() -> new RuntimeException("no item handler found!"));
            for (int i = 0; i < handler.getSlots(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
            }
        }
        while (iterator.hasNext()) {
            BlockPos p = iterator.next();
            if (level.getBlockEntity(p) instanceof GyrofugeAbstractBlockEntity gyrofugeBlock) {
                gyrofugeBlock.setLogic(null);
            }
        }
        level.setBlock(center, BlocksRegistration.GYROFUGE_BASE.get().defaultBlockState().setValue(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.none), 3);
    }

    public BlockPos getCenter() {
        return center;
    }

    public GyrofugeControllerBlockEntity getController() {
        if (level.getBlockEntity(center) instanceof GyrofugeControllerBlockEntity controller)
            return controller;
        return null;
    }

    public List<BlockPos> getSpecialBlocks() {
        return specialBlocks;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public MachineModifier getMachineModifier() {
        return MachineModifier.of(getSpecialBlocks().stream().map(level::getBlockEntity).filter(entity -> entity instanceof IGyrofugeModifier).map(entity -> ((IGyrofugeModifier) entity).getMachineModifier()).toArray(MachineModifier[]::new));
    }
}
