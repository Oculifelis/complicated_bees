package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.multiblock.GyrofugeLogic;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class GyrofugeOutputHatchBlockEntity extends AbstractGyrofugeBlockEntity implements IGyrofugeTickable {
    private LazyOptional<IItemHandler> gyrofugeOutput;
    private int tickCount;

    public GyrofugeOutputHatchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_OUTPUT_HATCH_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void setLogic(GyrofugeLogic logic) {
        super.setLogic(logic);
        if (logic != null)
            this.gyrofugeOutput = logic.getController().getOutputItemHandler();
        else
            this.gyrofugeOutput = null;
    }

    @Override
    public void onTick() {
        if (gyrofugeOutput != null && tickCount++ % 4 == 0) {
            for (Direction dir : Direction.values()) {
                BlockEntity blockEntity = getLevel().getBlockEntity(getBlockPos().relative(dir));
                if (blockEntity == null || blockEntity instanceof AbstractGyrofugeBlockEntity)
                    continue;
                IItemHandler itemCap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, dir.getOpposite()).resolve().orElse(null);
                IItemHandler output = gyrofugeOutput.resolve().orElse(null);
                if (itemCap != null && output != null) {
                    Util.moveInventoryItems(output, itemCap);
                }
            }
            tickCount = 0;
        }
    }
}
