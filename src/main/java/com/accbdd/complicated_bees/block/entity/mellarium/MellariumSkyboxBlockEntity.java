package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.bees.BeeHousingModifier;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MellariumSkyboxBlockEntity extends MellariumAbstractBlockEntity implements IMellariumModifier, IMellariumTickable {
    private static final int ENERGY_USAGE = 10;
    private static final BeeHousingModifier MODIFIER = new BeeHousingModifier.Builder().skyOverride(true).build();
    private boolean powered;

    public MellariumSkyboxBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_SKYBOX_BLOCK_ENTITY.get(), pPos, pBlockState);
        powered = false;
    }

    @Override
    public void onTick() {
        if (getLogic() != null && getLogic().getController() != null) {
            setPowered(getLogic().getController().getEnergy().extractEnergy(ENERGY_USAGE, false) >= ENERGY_USAGE);
        }
    }

    public void setPowered(boolean value) {
        if (powered == value)
            return;
        powered = value;
        if (getLevel() != null) {
            if (value) {
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, true), 3);
            } else {
                getLevel().setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, false), 3);
            }
        }
    }

    @Override
    public BeeHousingModifier getModifier() {
        return powered ? MODIFIER : new BeeHousingModifier();
    }
}
