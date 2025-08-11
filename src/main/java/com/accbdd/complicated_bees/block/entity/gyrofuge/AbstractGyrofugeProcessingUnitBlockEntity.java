package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractGyrofugeProcessingUnitBlockEntity extends AbstractPoweredGyrofugeBlockEntity implements IGyrofugeModifier {
    private final MachineModifier modifier;

    public AbstractGyrofugeProcessingUnitBlockEntity(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState, int processingMod) {
        super(type, pPos, pBlockState);
         modifier = new MachineModifier.Builder().processing(processingMod).efficiency((float) Math.pow(0.8, processingMod)).build();
    }

    @Override
    public int getIdleUsage() {
        return 50 * getMachineModifier().getProcessingMod();
    }

    @Override
    public MachineModifier getMachineModifier() {
        return modifier;
    }
}
