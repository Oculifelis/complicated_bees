package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MellariumTemporalSimulatorBlockEntity extends MellariumAbstractPoweredBlockEntity implements IMellariumModifier, IMellariumTickable {
    private static final int ENERGY_USAGE = 100;
    private static final BeeHousingModifier MODIFIER = new BeeHousingModifier.Builder().sleepOverride(true).build();

    public MellariumTemporalSimulatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_TEMPORAL_SIMULATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void onTick() {
        if (getLogic() != null && getLogic().getController() != null) {
            setPowered(getLogic().getController().getEnergy().extractEnergy(ENERGY_USAGE, false) >= ENERGY_USAGE);
        }
    }

    @Override
    public BeeHousingModifier getModifier() {
        return getPowered() ? MODIFIER : new BeeHousingModifier();
    }
}
