package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.multiblock.GyrofugeLogic;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.Util;
import com.accbdd.complicated_bees.util.forge.LazyOptional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

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
            this.gyrofugeOutput = logic.getController().map(GyrofugeControllerBlockEntity::getOutputItemHandler).orElse(LazyOptional.empty());
        else
            this.gyrofugeOutput = LazyOptional.empty();
    }

    @Override
    public void onTick() {
        if (gyrofugeOutput != null && gyrofugeOutput.isPresent() && tickCount++ % 4 == 0) {
            for (Direction dir : Direction.values()) {
                BlockEntity blockEntity = getLevel().getBlockEntity(getBlockPos().relative(dir));
                if (blockEntity == null || blockEntity instanceof AbstractGyrofugeBlockEntity)
                    continue;
                IItemHandler itemCap = getLevel().getCapability(Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, dir.getOpposite());
                IItemHandler output = gyrofugeOutput.resolve().orElse(null);
                if (itemCap != null && output != null) {
                    Util.moveInventoryItems(output, itemCap);
                }
            }
            tickCount = 0;
        }
    }
}
