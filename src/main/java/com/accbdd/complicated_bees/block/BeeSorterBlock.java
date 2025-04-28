package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.block.entity.BeeSorterBlockEntity;
import com.accbdd.complicated_bees.screen.BeeSorterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BeeSorterBlock extends BaseEntityBlock {
    public static final String SCREEN_BEE_SORTER = "gui.complicated_bees.bee_sorter";

    public BeeSorterBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(3.5F)
                .sound(SoundType.WOOD));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BeeSorterBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof BeeSorterBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer,
                        new SimpleMenuProvider((id, inv, player) -> new BeeSorterMenu(id, inv, pPos), Component.translatable(SCREEN_BEE_SORTER)),
                        buf -> {
                            buf.writeBlockPos(pPos);
                            buf.writeByteArray(((BeeSorterBlockEntity) be).getTypeFilters());
                        });
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
}
