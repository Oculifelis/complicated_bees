package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.item.UpgradeItem;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.util.UpgradeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GeneratorBlockEntity extends BlockEntity {
    public static final String ITEMS_TAG = "items";
    public static final String ENERGY_TAG = "energy";
    public static final String BURN_TIME_TAG = "burn_time";

    public static final int BASE_GENERATE = ServerConfig.SERVER_CONFIG.generatorBaseEnergy.get();
    public static final int BASE_TRANSFER = ServerConfig.SERVER_CONFIG.generatorBaseTransfer.get();
    public static final int BASE_STORAGE = ServerConfig.SERVER_CONFIG.generatorBaseStorage.get();

    public static final int SLOT_COUNT = 4;
    public static final int SLOT = 0;

    private int generate = BASE_GENERATE;
    private float burnTimeMod = 1;

    private final ItemStackHandler items = createItemHandler();
    private final ItemStackHandler upgradeItems = createUpgradeHandler(3);
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new AdaptedItemHandler(items) {
        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    });
    private final LazyOptional<IItemHandler> upgradeItemHandler = LazyOptional.of(() -> new AdaptedItemHandler(upgradeItems) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof UpgradeItem;
        }
    });

    private final EnergyStorage energy = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> new AdaptedEnergyStorage(energy) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    });

    private int burnTime;
    private int maxBurnTime;

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
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

    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistration.GENERATOR_BLOCK_ENTITY.get(), pos, state);
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
                int burnTime = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING);
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
                        int received = energy.receiveEnergy(Math.min(this.energy.getEnergyStored(), BASE_TRANSFER), false);
                        this.energy.extractEnergy(received, false);
                        setChanged();
                    }
                }
            }
        }
    }

    public int getBurnTime() {
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
        tag.put(ENERGY_TAG, energy.serializeNBT());
        tag.put(BURN_TIME_TAG, IntTag.valueOf(burnTime));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(ITEMS_TAG)) {
            items.deserializeNBT(tag.getCompound(ITEMS_TAG));
        }
        if (tag.contains(ENERGY_TAG)) {
            energy.deserializeNBT(tag.get(ENERGY_TAG));
        }
        if (tag.contains(BURN_TIME_TAG)) {
            burnTime = tag.getInt(BURN_TIME_TAG);
            maxBurnTime = burnTime;
        }
    }

    @Nonnull
    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
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
        return new EnergyStorage(BASE_STORAGE, BASE_TRANSFER, BASE_TRANSFER);
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
        generate = Math.round(BASE_GENERATE * UpgradeHelper.getSpeedMod(upgradeItems));
        burnTimeMod = UpgradeHelper.getEfficiencyMod(upgradeItems) / UpgradeHelper.getSpeedMod(upgradeItems);
        setBurnTime(0);
    }
}
