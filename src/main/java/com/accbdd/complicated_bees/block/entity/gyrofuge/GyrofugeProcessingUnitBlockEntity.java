package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GyrofugeProcessingUnitBlockEntity extends GyrofugeAbstractPoweredBlockEntity implements IGyrofugeModifier {
    private static final MachineModifier modifier = new MachineModifier.Builder().processing(1).efficiency(0.4f).build();

    public GyrofugeProcessingUnitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_PROCESSING_UNIT_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public int getPowerUsage() {
        return 50;
    }

    @Override
    public MachineModifier getMachineModifier() {
        return modifier;
    }
}
