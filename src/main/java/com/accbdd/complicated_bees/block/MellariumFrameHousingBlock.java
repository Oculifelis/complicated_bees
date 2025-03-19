package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.mellarium.MellariumFrameHousingBlockEntity;
import com.accbdd.complicated_bees.screen.MellariumFrameHousingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class MellariumFrameHousingBlock extends MellariumBlock {
    public static final String SCREEN_MELLARIUM = "gui.complicated_bees.mellarium";
    private final int slotCount;

    public MellariumFrameHousingBlock(int slotCount) {
        super();
        this.slotCount = slotCount;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!(pNewState.getBlock() instanceof MellariumFrameHousingBlock) && pLevel.getBlockEntity(pPos) instanceof MellariumFrameHousingBlockEntity frameHousingBlockEntity) {
            IItemHandler handler = frameHousingBlockEntity.getFrameItemHandler().resolve().get();
            for (int i = 0; i < handler.getSlots(); i++) {
                Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), handler.getStackInSlot(i));
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            if (pLevel.getBlockEntity(pPos) instanceof MellariumFrameHousingBlockEntity) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable(SCREEN_MELLARIUM);
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
                        return new MellariumFrameHousingMenu(windowId, player, pPos, slotCount);
                    }
                };

                NetworkHooks.openScreen((ServerPlayer) pPlayer, containerProvider, pPos);
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MellariumFrameHousingBlockEntity(pPos, pState, slotCount);
    }
}
