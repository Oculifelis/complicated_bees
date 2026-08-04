package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GyrofugeAdvancedExtractionUnitBlockEntity extends AbstractGyrofugePoweredModifierBlockEntity {
    public static final MachineModifier MODIFIER = new MachineModifier.Builder().output(2f).speed(0.8f).efficiency(0.85f).build();

    public GyrofugeAdvancedExtractionUnitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_ADVANCED_EXTRACTION_UNIT_BLOCK_ENTITY.get(),
                pPos,
                pBlockState,
                MODIFIER,
                50);
    }
}
