package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MellariumSkyboxBlockEntity extends AbstractPoweredMellariumBlockEntity implements IMellariumModifier, IMellariumTickable {
    private static final int ENERGY_USAGE = 10;
    private static final BeeHousingModifier MODIFIER = new BeeHousingModifier.Builder().skyOverride(true).build();

    public MellariumSkyboxBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_SKYBOX_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    int getPowerUsage() {
        return ENERGY_USAGE;
    }

    @Override
    public BeeHousingModifier getModifier() {
        return getPowered() ? MODIFIER : new BeeHousingModifier();
    }
}
