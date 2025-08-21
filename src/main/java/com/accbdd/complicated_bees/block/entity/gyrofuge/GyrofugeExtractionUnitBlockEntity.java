package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GyrofugeExtractionUnitBlockEntity extends AbstractGyrofugePoweredModifierBlockEntity {
    public static final MachineModifier MODIFIER = new MachineModifier.Builder().output(1.25f).speed(0.85f).efficiency(0.75f).build();

    public GyrofugeExtractionUnitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_EXTRACTION_UNIT_BLOCK_ENTITY.get(),
                pPos,
                pBlockState,
                MODIFIER,
                50);
    }
}
