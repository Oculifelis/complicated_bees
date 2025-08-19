package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MellariumRainShieldBlockEntity extends AbstractMellariumBlockEntity implements IMellariumModifier {
    private static final BeeHousingModifier MODIFIER = new BeeHousingModifier.Builder().rainOverride(true).build();

    public MellariumRainShieldBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_RAIN_SHIELD_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public BeeHousingModifier getModifier() {
        return MODIFIER;
    }
}
