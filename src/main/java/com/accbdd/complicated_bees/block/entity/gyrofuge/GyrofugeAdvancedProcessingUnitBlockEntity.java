package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GyrofugeAdvancedProcessingUnitBlockEntity extends AbstractGyrofugePoweredModifierBlockEntity {

    public static final MachineModifier MODIFIER = new MachineModifier.Builder().processing(4).efficiency(0.25f).build();

    public GyrofugeAdvancedProcessingUnitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_ADVANCED_PROCESSING_UNIT_BLOCK_ENTITY.get(),
                pPos,
                pBlockState,
                MODIFIER,
                100);
    }
}
