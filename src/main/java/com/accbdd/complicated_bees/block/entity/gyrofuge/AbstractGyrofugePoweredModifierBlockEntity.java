package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractGyrofugePoweredModifierBlockEntity extends AbstractPoweredGyrofugeBlockEntity implements IGyrofugeModifier {
    private final MachineModifier modifier;
    private final int idleUsage;

    public AbstractGyrofugePoweredModifierBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, MachineModifier modifier, int idleUsage) {
        super(pType, pPos, pBlockState);
        this.modifier = modifier;
        this.idleUsage = idleUsage;
    }

    @Override
    public int getIdleUsage() {
        return idleUsage;
    }


    @Override
    public MachineModifier getMachineModifier() {
        return modifier;
    }
}
