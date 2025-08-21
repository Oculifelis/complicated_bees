package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.bees.MachineModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GyrofugeAdvancedSpeedUnitBlockEntity extends AbstractGyrofugePoweredModifierBlockEntity {

    public static final MachineModifier MODIFIER = new MachineModifier.Builder().speed(1.75f).efficiency(0.75f).build();

    public GyrofugeAdvancedSpeedUnitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_ADVANCED_SPEED_UNIT_BLOCK_ENTITY.get(),
                pPos,
                pBlockState,
                MODIFIER,
                10);
    }
}
