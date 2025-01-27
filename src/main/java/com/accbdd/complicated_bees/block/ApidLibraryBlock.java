package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.screen.LibraryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class ApidLibraryBlock extends Block {
    public ApidLibraryBlock() {
        super(Properties.of()
                .strength(1.5f)
                .explosionResistance(1.5f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL)
                .mapColor(MapColor.COLOR_GRAY));
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider(((pContainerId, pPlayerInventory, pPlayer) -> new LibraryMenu(pContainerId, pPlayer)), Component.empty());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, pState.getMenuProvider(pLevel, pPos));
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }
}
