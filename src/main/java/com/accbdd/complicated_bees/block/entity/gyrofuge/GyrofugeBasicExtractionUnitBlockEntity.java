package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GyrofugeBasicExtractionUnitBlockEntity extends AbstractGyrofugePoweredModifierBlockEntity {
    public GyrofugeBasicExtractionUnitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_BASIC_EXTRACTION_UNIT_BLOCK_ENTITY.get(),
                pPos,
                pBlockState,
                new MachineModifier.Builder().output(1.1f).speed(0.95f).efficiency(0.85f).build(),
                25);
    }
}
