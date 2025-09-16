package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MellariumOutputHatchBlockEntity extends AbstractMellariumBlockEntity implements IMellariumTickable {
    private LazyOptional<IItemHandlerModifiable> mellariumOutput;
    private int tickCount;

    public MellariumOutputHatchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_OUTPUT_HATCH_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void setLogic(MellariumLogic logic) {
        super.setLogic(logic);
        if (logic != null)
            this.mellariumOutput = logic.getController().getOutputItemHandler();
        else
            this.mellariumOutput = null;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (getLogic() == null || getLogic().getController() == null)
            return super.getCapability(cap, side);

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return getLogic().getController().getItemHandler().cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onTick() {
        if (mellariumOutput != null && tickCount++ % 4 == 0) {
            for (Direction dir : Direction.values()) {
                BlockEntity blockEntity = getLevel().getBlockEntity(getBlockPos().relative(dir));
                if (blockEntity == null || blockEntity instanceof AbstractMellariumBlockEntity || blockEntity instanceof MellariumControllerBlockEntity)
                    continue;
                IItemHandler itemCap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, dir.getOpposite()).resolve().orElse(null);
                IItemHandler output = mellariumOutput.resolve().orElse(null);
                if (itemCap != null && output != null) {
                    Util.moveInventoryItems(output, itemCap);
                }
            }
            tickCount = 0;
        }
    }
}
