package com.accbdd.complicated_bees.multiblock;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.block.entity.CombinedEnergyStorage;
import com.accbdd.complicated_bees.block.entity.gyrofuge.*;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GyrofugeLogic {
    private final Level level;
    private final BlockPos center;
    private final IEnergyStorage energyStorage;
    private final List<BlockPos> specialBlocks = new ArrayList<>();
    private int idleUsage;

    public GyrofugeLogic(Level level, BlockPos center) {
        this.level = level;
        this.center = center;
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        List<IEnergyStorage> energyStorages = new ArrayList<>(); //blocks that store energy
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof AbstractGyrofugeBlockEntity gyrofugeBlock) {
                gyrofugeBlock.setLogic(this);
                BlockState blockState = level.getBlockState(pos);
                if (!blockState.is(BlocksRegistration.GYROFUGE_BASE.get())) {
                    specialBlocks.add(pos);
                }
                if (gyrofugeBlock instanceof GyrofugeEnergyCellBlockEntity cell) {
                    energyStorages.add(cell.getEnergy());
                }
                if (gyrofugeBlock instanceof AbstractPoweredGyrofugeBlockEntity power) {
                    idleUsage += power.getIdleUsage();
                }
            } else {
                if (!(level.getBlockEntity(pos) instanceof GyrofugeControllerBlockEntity))
                    ComplicatedBees.LOGGER.warn("built a gyrofuge with non-gyrofuge block at {}", pos);
            }
        }
        energyStorage = new CombinedEnergyStorage(energyStorages.toArray(IEnergyStorage[]::new));

        if (level.getBlockEntity(center) instanceof GyrofugeControllerBlockEntity controller)
            controller.setLogic(this);

    }

    public void deconstruct(BlockPos pos) {
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        getController().ifPresent(controller -> {
            while (!controller.outputBuffer.empty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), controller.outputBuffer.pop());
            }
            IItemHandler handler = controller.getItemHandler().orElseThrow(() -> new RuntimeException("no item handler found!"));
            for (int i = 0; i < handler.getSlots(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
            }
        });
        while (iterator.hasNext()) {
            BlockPos p = iterator.next();
            if (level.getBlockEntity(p) instanceof AbstractGyrofugeBlockEntity gyrofugeBlock) {
                gyrofugeBlock.setLogic(null);
            }
        }
        level.setBlock(center, BlocksRegistration.GYROFUGE_BASE.get().defaultBlockState().setValue(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.none), 3);
    }

    public BlockPos getCenter() {
        return center;
    }

    public Optional<GyrofugeControllerBlockEntity> getController() {
        if (level.getBlockEntity(center) instanceof GyrofugeControllerBlockEntity controller)
            return Optional.of(controller);
        return Optional.empty();
    }

    public List<BlockPos> getSpecialBlocks() {
        return specialBlocks;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public MachineModifier getMachineModifier() {
        return MachineModifier.of(new MachineModifier(1, 1, 1.5f, 0), MachineModifier.of(getSpecialBlocks().stream().map(level::getBlockEntity).filter(entity -> entity instanceof IGyrofugeModifier).map(entity -> ((IGyrofugeModifier) entity).getMachineModifier()).toArray(MachineModifier[]::new)));
    }

    public int getIdleUsage() {
        return idleUsage;
    }
}
