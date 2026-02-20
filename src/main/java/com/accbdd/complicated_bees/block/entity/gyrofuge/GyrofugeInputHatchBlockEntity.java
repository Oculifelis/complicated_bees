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

public class GyrofugeInputHatchBlockEntity extends AbstractGyrofugeBlockEntity implements IGyrofugeTickable {
    private LazyOptional<IItemHandler> gyrofugeInput;
    private int tickCount;

    public GyrofugeInputHatchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.GYROFUGE_INPUT_HATCH_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void setLogic(GyrofugeLogic logic) {
        super.setLogic(logic);
        if (logic != null)
            this.gyrofugeInput = logic.getController().map(GyrofugeControllerBlockEntity::getInputItemHandler).orElse(LazyOptional.empty());
        else
            this.gyrofugeInput = LazyOptional.empty();
    }

    @Override
    public void onTick() {
        if (gyrofugeInput != null && gyrofugeInput.isPresent() && tickCount++ % 4 == 0) {
            for (Direction dir : Direction.values()) {
                BlockEntity blockEntity = getLevel().getBlockEntity(getBlockPos().relative(dir));
                if (blockEntity == null || blockEntity instanceof AbstractGyrofugeBlockEntity)
                    continue;
                IItemHandler itemCap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, dir.getOpposite()).resolve().orElse(null);
                IItemHandler input = gyrofugeInput.resolve().orElse(null);
                if (itemCap != null && input != null) {
                    Util.moveInventoryItems(itemCap, input);
                }
            }
            tickCount = 0;
        }
    }
}
