package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GyrofugeAdvancedExtractionUnitBlockEntity extends AbstractGyrofugePoweredModifierBlockEntity {
    public GyrofugeAdvancedExtractionUnitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_ADVANCED_EXTRACTION_UNIT_BLOCK_ENTITY.get(),
                pPos,
                pBlockState,
                new MachineModifier.Builder().output(1.5f).speed(0.8f).efficiency(0.7f).build(),
                100);
    }
}
