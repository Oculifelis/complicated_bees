package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GyrofugeAdvancedEfficiencyUnitBlockEntity extends AbstractGyrofugePoweredModifierBlockEntity {
    public static final MachineModifier MODIFIER = new MachineModifier.Builder().speed(0.85f).efficiency(3f).build();

    public GyrofugeAdvancedEfficiencyUnitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_ADVANCED_EFFICIENCY_UNIT_BLOCK_ENTITY.get(),
                pPos,
                pBlockState,
                MODIFIER,
                25);
    }
}
