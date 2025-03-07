package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.*;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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
        if (pLevel.getBlockEntity(pPos) instanceof MellariumAbstractBlockEntity mellariumBase && mellariumBase.getLogic() != null) {
            mellariumBase.getLogic().deconstruct();
        }
        super.destroy(pLevel, pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pLevel.getBlockEntity(pPos) instanceof MellariumAbstractBlockEntity mellariumBase && mellariumBase.getLogic() != null) {
            if (!pNewState.is(BlocksRegistration.MELLARIUM_CONTROLLER.get()))
                mellariumBase.getLogic().deconstruct();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof MellariumAbstractBlockEntity mellariumAbstractBlock) {
            if (mellariumAbstractBlock.getLogic() == null)
                pPlayer.displayClientMessage(Component.literal("not a valid mellarium!"), true);
            else
                pPlayer.displayClientMessage(Component.literal("valid mellarium"), true);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return switch (type) {
            case BASE -> new MellariumBaseBlockEntity(pPos, pState);
            case FAN -> new MellariumFanBlockEntity(pPos, pState);
            case FRAME -> new MellariumFrameHousingBlockEntity(pPos, pState);
            case CONTROLLER -> new MellariumControllerBlockEntity(pPos, pState, null);
            default -> null;
        };
    }

    public enum MellariumBlockType {
        BASE,
        CONTROLLER,
        FAN,
        HEATER,
        HYDRO,
        FRAME
    }
}
