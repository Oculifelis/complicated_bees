package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.gyrofuge.AbstractGyrofugeBlockEntity;
import com.accbdd.complicated_bees.block.entity.gyrofuge.GyrofugeControllerBlockEntity;
import com.accbdd.complicated_bees.datagen.BlockTagGenerator;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.screen.GyrofugeMenu;
import com.accbdd.complicated_bees.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGyrofugeBlock extends BaseEntityBlock {
    public static final String SCREEN_GYROFUGE = "gui.complicated_bees.gyrofuge";

    public AbstractGyrofugeBlock(Properties prop) {
        super(prop);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        MultiblockHelper.tryBuildGyrofuge(pLevel, pPos);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pLevel.getBlockEntity(pPos) instanceof AbstractGyrofugeBlockEntity gyrofugeBase && gyrofugeBase.getLogic() != null) {
            if (!pNewState.is(BlockTagGenerator.GYROFUGE))
                gyrofugeBase.getLogic().deconstruct(pPos);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            if (pLevel.getBlockEntity(pPos) instanceof AbstractGyrofugeBlockEntity gyrofuge) {
                if (gyrofuge.getLogic() == null) {
                    pPlayer.displayClientMessage(Component.translatable("gui.complicated_bees.invalid_gyrofuge"), true);
                    return InteractionResult.CONSUME;
                }

                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable(SCREEN_GYROFUGE);
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
                        return gyrofuge.getLogic().getController().
                                map(gyrofugeControllerBlockEntity -> new GyrofugeMenu(windowId, player, pPos, gyrofugeControllerBlockEntity.getData())).
                                orElseGet(() -> new GyrofugeMenu(windowId, player, pPos));
                    }
                };

                NetworkHooks.openScreen((ServerPlayer) pPlayer, containerProvider, pPos);
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        } else {
            return (lvl, pos, st, blockEntity) -> {
                if (blockEntity instanceof GyrofugeControllerBlockEntity be) {
                    be.tickServer();
                }
            };
        }
    }

    public abstract BlockEntity newBlockEntity(BlockPos pPos, BlockState pState);

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.none);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(EsotericRegistration.ASSEMBLED);
    }
}
