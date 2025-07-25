package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.item.UpgradeItem;
import com.accbdd.complicated_bees.util.UpgradeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public abstract class BaseGeneratorBlockEntity extends BlockEntity {
    public static final String ITEMS_TAG = "items";
    public static final String UPGRADES_TAG = "upgrades";
    public static final String ENERGY_TAG = "energy";
    public static final String BURN_TIME_TAG = "burn_time";

    public final int baseGenerate;
    public final int baseTransfer;
    public final int baseStorage;

    public static final int SLOT_COUNT = 4;
    public static final int SLOT = 0;

    private int generate;
    private float burnTimeMod = 1;

    private final ItemStackHandler items;
    private final ItemStackHandler upgradeItems;
    private final LazyOptional<IItemHandler> itemHandler;
    private final LazyOptional<IItemHandler> upgradeItemHandler;

    private final EnergyStorage energy;
    private final LazyOptional<IEnergyStorage> energyHandler;

    private int burnTime;
    private int maxBurnTime;

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
        upgradeItemHandler.invalidate();
        energyHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return getItemHandler().cast();
        if (cap == ForgeCapabilities.ENERGY)
            return getEnergyHandler().cast();

        return super.getCapability(cap, side);
    }

    public BaseGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int baseGenerate, int baseTransfer, int baseStorage) {
        super(type, pos, state);
        this.baseGenerate = baseGenerate;
        this.baseTransfer = baseTransfer;
        this.baseStorage = baseStorage;
        this.generate = baseGenerate;
        this.items = createItemHandler();
        this.itemHandler = LazyOptional.of(() -> new AdaptedItemHandler(items) {
            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }
        });
        this.upgradeItems = createUpgradeHandler(3);
        this.upgradeItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(upgradeItems) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.getItem() instanceof UpgradeItem;
            }
        });
        this.energy = createEnergyStorage();
        this.energyHandler = LazyOptional.of(() -> new AdaptedEnergyStorage(energy) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return 0;
            }

            @Override
            public boolean canReceive() {
                return false;
            }
        });
    }

    public void tickServer() {
        generateEnergy();
        distributeEnergy();
    }

    private void generateEnergy() {
        if (energy.getEnergyStored() < energy.getMaxEnergyStored()) {
            if (burnTime <= 0) {
                ItemStack fuel = items.getStackInSlot(SLOT);
                if (fuel.isEmpty()) {
                    return;
                }
                int burnTime = getBurnTime(fuel);
                maxBurnTime = Math.round(burnTime * burnTimeMod);
                setBurnTime(maxBurnTime);
                if (burnTime <= 0) {
                    return;
                }
                items.extractItem(SLOT, 1, false);
            } else {
                setBurnTime(burnTime - 1);
                energy.receiveEnergy(generate, false);
            }
            setChanged();
        }
    }

    private void setBurnTime(int bt) {
        if (bt == burnTime) {
            return;
        }
        burnTime = bt;
        if (getBlockState().getValue(BlockStateProperties.POWERED) != burnTime > 0) {
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, burnTime > 0));
        }
        setChanged();
    }

    private void distributeEnergy() {
        // Check all sides of the block and send energy if that block supports the energy capability
        for (Direction direction : Direction.values()) {
            if (energy.getEnergyStored() <= 0) {
                return;
            }
            BlockEntity be = getLevel().getBlockEntity(getBlockPos().relative(direction));
            if (be != null) {
                IEnergyStorage energy = be.getCapability(ForgeCapabilities.ENERGY).orElse(null);
                if (energy != null) {
                    if (energy.canReceive()) {
                        int received = energy.receiveEnergy(Math.min(this.energy.getEnergyStored(), baseTransfer), false);
                        this.energy.extractEnergy(received, false);
                        setChanged();
                    }
                }
            }
        }
    }

    public int getCurrentBurnTime() {
        return this.burnTime;
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public int getStoredPower() {
        return energy.getEnergyStored();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(ITEMS_TAG, items.serializeNBT());
        tag.put(UPGRADES_TAG, upgradeItems.serializeNBT());
        tag.put(ENERGY_TAG, energy.serializeNBT());
        tag.put(BURN_TIME_TAG, IntTag.valueOf(burnTime));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(ITEMS_TAG)) {
            items.deserializeNBT(tag.getCompound(ITEMS_TAG));
        }
        if (tag.contains(UPGRADES_TAG)) {
            upgradeItems.deserializeNBT(tag.getCompound(UPGRADES_TAG));
        }
        if (tag.contains(ENERGY_TAG)) {
            energy.deserializeNBT(tag.get(ENERGY_TAG));
        }
        calculateUpgradeStats();
        if (tag.contains(BURN_TIME_TAG)) {
            burnTime = tag.getInt(BURN_TIME_TAG);
            maxBurnTime = burnTime;
        }
    }

    @Nonnull
    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler() {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return super.isItemValid(slot, stack) && isValidInput(stack);
            }
        };

    }

    private ItemStackHandler createUpgradeHandler(int slots) {
        return new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                calculateUpgradeStats();
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    @Nonnull
    private EnergyStorage createEnergyStorage() {
        return new EnergyStorage(baseStorage, baseTransfer, baseTransfer);
    }

    public LazyOptional<IItemHandler> getItemHandler() {
        return itemHandler;
    }

    public LazyOptional<IItemHandler> getUpgradeItemHandler() {
        return upgradeItemHandler;
    }

    public LazyOptional<IEnergyStorage> getEnergyHandler() {
        return energyHandler;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    public void calculateUpgradeStats() {
        generate = Math.round(baseGenerate * UpgradeHelper.getSpeedMod(upgradeItems));
        burnTimeMod = UpgradeHelper.getEfficiencyMod(upgradeItems) / UpgradeHelper.getSpeedMod(upgradeItems);
        setBurnTime(0);
    }

    /**
     * @param stack the stack to test
     * @return whether the given stack is a valid input for this generator
     */
    public abstract boolean isValidInput(ItemStack stack);

    /**
     * @param stack the stack to test
     * @return the time the given stack will burn for, before upgrades
     */
    public abstract int getBurnTime(ItemStack stack);
}
