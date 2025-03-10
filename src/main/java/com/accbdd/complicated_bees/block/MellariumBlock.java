package com.accbdd.complicated_bees.block;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.block.entity.*;
import com.accbdd.complicated_bees.multiblock.MellariumLogic;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.screen.MellariumMenu;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class MellariumBlock extends BaseEntityBlock {
    public static final String SCREEN_APIARY = "gui.complicated_bees.mellarium";
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
        if (!pLevel.isClientSide) {
            if (pLevel.getBlockEntity(pPos) instanceof MellariumAbstractBlockEntity mellarium) {
                if (mellarium.getLogic() == null) {
                    pPlayer.displayClientMessage(Component.literal("not a valid mellarium!"), true);
                    return InteractionResult.CONSUME;
                }

                MellariumLogic logic = mellarium.getLogic();
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable(SCREEN_APIARY);
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
                        return new MellariumMenu(windowId, player, logic.getCenter(), logic.getController().getData());
                    }
                };

                NetworkHooks.openScreen((ServerPlayer) pPlayer, containerProvider, logic.getCenter());
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
            ComplicatedBees.LOGGER.debug("got ticker for {}", state);
            return (lvl, pos, st, blockEntity) -> {
                if (blockEntity instanceof MellariumControllerBlockEntity be) {
                    be.tickServer();
                }
            };
        }
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
