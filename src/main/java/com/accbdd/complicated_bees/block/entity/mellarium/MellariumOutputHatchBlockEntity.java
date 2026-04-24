package com.accbdd.complicated_bees.block.entity.mellarium;

import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class MellariumOutputHatchBlockEntity extends AbstractMellariumBlockEntity implements IMellariumTickable {
    private IItemHandlerModifiable mellariumOutput;
    private int tickCount;

    public MellariumOutputHatchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.MELLARIUM_OUTPUT_HATCH_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void setLogic(MellariumLogic logic) {
        super.setLogic(logic);
        if (logic != null)
            logic.getController().ifPresent(controller ->
                this.mellariumOutput = controller.getOutputItemHandler()
            );
        else
            this.mellariumOutput = null;
    }

    @Override
    public void onTick() {
        if (mellariumOutput != null && tickCount++ % 4 == 0) {
            for (Direction dir : Direction.values()) {
                BlockEntity blockEntity = getLevel().getBlockEntity(getBlockPos().relative(dir));
                if (blockEntity == null || blockEntity instanceof AbstractMellariumBlockEntity || blockEntity instanceof MellariumControllerBlockEntity)
                    continue;
                IItemHandler itemCap = getLevel().getCapability(Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, dir.getOpposite());
                IItemHandler output = mellariumOutput;
                if (itemCap != null && output != null) {
                    Util.moveInventoryItems(output, itemCap);
                }
            }
            tickCount = 0;
        }
    }
}
