package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.MellariumBaseBlockEntity;
import com.accbdd.complicated_bees.block.entity.MellariumFanBlockEntity;
import com.accbdd.complicated_bees.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MellariumBlock extends BaseEntityBlock {
    private final MellariumBlockType type;

    public MellariumBlock(MellariumBlockType type) {
        super(Properties.of()
                .mapColor(DyeColor.ORANGE)
                .sound(SoundType.WOOD)
                .strength(1));
        this.type = type;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        MultiblockHelper.tryBuildMellarium(pLevel, pPos, pPlacer == null ? null : pPlacer.getUUID());
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.getBlockEntity(pPos) instanceof MellariumBaseBlockEntity mellariumBase && mellariumBase.getLogic() != null) {
            mellariumBase.getLogic().deconstruct();
        }
        super.destroy(pLevel, pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pLevel.getBlockEntity(pPos) instanceof MellariumBaseBlockEntity mellariumBase && mellariumBase.getLogic() != null) {
            mellariumBase.getLogic().deconstruct();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return switch (type) {
            case BASE -> new MellariumBaseBlockEntity(pPos, pState);
            case FAN -> new MellariumFanBlockEntity(pPos, pState);
            default -> null;
        };
    }

    public enum MellariumBlockType {
        BASE,
        FAN,
        HEATER,
        HYDRO,
        FRAME
    }
}
