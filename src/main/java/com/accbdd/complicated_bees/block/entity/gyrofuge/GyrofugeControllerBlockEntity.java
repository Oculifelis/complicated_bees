package com.accbdd.complicated_bees.block.entity.gyrofuge;

import com.accbdd.complicated_bees.block.entity.AbstractCentrifugeBlockEntity;
import com.accbdd.complicated_bees.block.entity.AdaptedEnergyStorage;
import com.accbdd.complicated_bees.block.entity.AdaptedItemHandler;
import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.item.UpgradeItem;
import com.accbdd.complicated_bees.multiblock.GyrofugeLogic;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GyrofugeControllerBlockEntity extends AbstractCentrifugeBlockEntity {
    public static final int INPUT_SLOT_COUNT = 4;
    public static final int OUTPUT_SLOT_COUNT = 9;
    public static final int UPGRADE_SLOT_COUNT = 0;
    public static final int SLOT_COUNT = INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT + UPGRADE_SLOT_COUNT;

    private GyrofugeLogic gyrofugeLogic;

    public static final int BASE_USAGE = ServerConfig.SERVER_CONFIG.gyrofugeBaseEnergy.get();
    public static final int BASE_MAX_PROGRESS = ServerConfig.SERVER_CONFIG.gyrofugeBaseSpeed.get();

    private final LazyOptional<IItemHandler> inputItemHandler;
    private final LazyOptional<IItemHandler> outputItemHandler;
    public final LazyOptional<IItemHandler> upgradeItemHandler;
    private final LazyOptional<IItemHandler> itemHandler;
    private final LazyOptional<IEnergyStorage> energyHandler;
    private int processingCount = 3;

    public GyrofugeControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntitiesRegistration.GYROFUGE_CONTROLLER_BLOCK_ENTITY.get(), pos, blockState);
        this.inputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(inputItems) {
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }
        });
        this.outputItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(outputItems) {
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return stack;
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return false;
            }
        });
        this.upgradeItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(upgradeItems) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.getItem() instanceof UpgradeItem;
            }
        });
        this.energyHandler = LazyOptional.of(() -> new AdaptedEnergyStorage(getGyrofugeLogic().getEnergyStorage()));
        this.itemHandler = LazyOptional.of(() -> new CombinedInvWrapper((IItemHandlerModifiable) outputItemHandler.resolve().get(), (IItemHandlerModifiable) inputItemHandler.resolve().get()));
    }

    public void setLogic(GyrofugeLogic logic) {
        this.gyrofugeLogic = logic;
    }

    public GyrofugeLogic getGyrofugeLogic() {
        if (gyrofugeLogic == null && MultiblockHelper.isValidGyrofuge(getLevel(), getBlockPos())) {
            MultiblockHelper.buildGyrofuge(getLevel(), getBlockPos());
        }
        return gyrofugeLogic;
    }

    @Override
    protected ItemStackHandler createInputHandler() {
        return new ItemStackHandler(INPUT_SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return getRecipe(stack) != null;
            }
        };
    }

    @Override
    protected ItemStackHandler createOutputHandler() {
        return new ItemStackHandler(OUTPUT_SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Override
    protected ItemStackHandler createUpgradeHandler() {
        return new ItemStackHandler(0);
    }

    @Override
    protected EnergyStorage createEnergyStorage() {
        return new EnergyStorage(0);
    }

    @Override
    public List<ItemStack> getCurrentlyProcessing() {
        List<ItemStack> toProcess = new ArrayList<>();
        int processed = 0;
        for (int i = 0; i < inputItems.getSlots(); i++) {
            ItemStack stack = inputItems.getStackInSlot(i);
            if (getRecipe(stack) != null) {
                int stackProcessed = 0;
                while (processed < processingCount && stack.getCount() - stackProcessed > 0) {
                    toProcess.add(stack);
                    stackProcessed++;
                    processed++;
                }
            }
        }
        return toProcess;
    }

    @Override
    public float getOutputMod() {
        return 1.5f;
    }

    @Override
    public int getMaxProgress() {
        return 100;
    }

    @Override
    public LazyOptional<IItemHandler> getItemHandler() {
        return itemHandler;
    }

    @Override
    public LazyOptional<IItemHandler> getInputItemHandler() {
        return inputItemHandler;
    }

    @Override
    public LazyOptional<IItemHandler> getOutputItemHandler() {
        return outputItemHandler;
    }

    @Override
    public LazyOptional<IItemHandler> getUpgradeItemHandler() {
        return upgradeItemHandler;
    }

    @Override
    public LazyOptional<IEnergyStorage> getEnergyHandler() {
        return energyHandler;
    }

    public ItemStackHandler getInputItems() {
        return this.inputItems;
    }
}
