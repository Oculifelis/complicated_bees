package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.gene.GeneSpecies;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.screen.widget.BeeTypeWidget;
import com.accbdd.complicated_bees.util.Util;
import com.accbdd.complicated_bees.util.forge.LazyOptional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BeeSorterBlockEntity extends BlockEntity {
    public static final String TYPES = "types";
    public static final int TRANSFER_TICKS = 5;
    private byte[] typeFilters; //down, up, north, south, east, west
    private String[] speciesFilters;
    private final ItemStackHandler item;
    private final List<LazyOptional<IItemHandler>> handlers;
    private int transferCooldown;

    public BeeSorterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.BEE_SORTER_BLOCK_ENTITY.get(), pPos, pBlockState);
        typeFilters = new byte[6];
        item = new ItemStackHandler(1);
        handlers = new ArrayList<>();
        for (int i = 0; i < typeFilters.length; i++) {
            handlers.add(createFilterHandler(i));
        }
        speciesFilters = new String[36];
        transferCooldown = TRANSFER_TICKS;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return side == null ? handlers.get(0).cast() : handlers.get(side.ordinal()).cast();
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag, registries);
        pTag.putByteArray(TYPES, typeFilters);
        pTag.put("item", item.serializeNBT(registries));
        CompoundTag filterTag = new CompoundTag();
        for (int i = 0; i < 36; i++) {
            if (speciesFilters[i] != null)
                filterTag.putString(String.valueOf(i), speciesFilters[i]);
        }
        pTag.put("filters", filterTag);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);
        if (pTag.contains(TYPES) && pTag.getByteArray(TYPES).length == 6)
            typeFilters = pTag.getByteArray(TYPES);
        if (pTag.contains("item"))
            item.deserializeNBT(registries, pTag.getCompound("item"));
        if (pTag.contains("filters")) {
            CompoundTag filterTag = pTag.getCompound("filters");
            for (String key : filterTag.getAllKeys()) {
                try {
                    speciesFilters[Integer.parseInt(key)] = filterTag.getString(key);
                } catch (NumberFormatException e) {
                    ComplicatedBees.LOGGER.error("tried to load bee sorter with illegal filter key at position {}", getBlockPos());
                    break;
                }
            }
        }
    }

    public void serverTick() {
        if (!item.getStackInSlot(0).isEmpty() & getBlockState().getValue(BlockStateProperties.ENABLED))
            transferCooldown--;
        if (transferCooldown <= 0) {
            transferCooldown = TRANSFER_TICKS;
            int maxSpecificity = 1;
            List<Direction> possibleDirections = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                if (typeFilters[direction.ordinal()] != BeeTypeWidget.BeeTypeState.NONE.ordinal() & getLevel() != null) {
                    int curSpecificity = filterSpecificity(direction);
                    if (curSpecificity > maxSpecificity) {
                        maxSpecificity = curSpecificity;
                        possibleDirections.clear();
                    }
                    if (canInsertItem(direction) & curSpecificity >= maxSpecificity) {
                        possibleDirections.add(direction);
                    }
                }
            }
            if (!possibleDirections.isEmpty()) {
                Direction dir = possibleDirections.get(getLevel().getRandom().nextInt(possibleDirections.size()));
                Util.moveInventoryItems(item, getHandler(dir).get());
            }
        }
    }

    private boolean canInsertItem(Direction dir) {
        boolean flag = false;
        var dest = getHandler(dir);
        if (dest.isPresent()) {
            ItemStack extracted = item.extractItem(0, 1, true);
            if (!extracted.isEmpty())
                flag = ItemHandlerHelper.insertItem(dest.get(), extracted, true).isEmpty();
        }
        return flag;
    }

    private Optional<IItemHandler> getHandler(Direction dir) {
        BlockEntity transferTo = getLevel().getBlockEntity(getBlockPos().relative(dir));
        if (transferTo != null) {
            return Optional.ofNullable(getLevel().getCapability(Capabilities.ItemHandler.BLOCK, transferTo.getBlockPos(), transferTo.getBlockState(), transferTo, dir.getOpposite()));
        }
        return Optional.empty();
    }

    public void setTypeFilters(byte[] typeFilters) {
        this.typeFilters = typeFilters;
        setChanged();
    }

    public void setTypeFilters(int index, byte value) {
        this.typeFilters[index] = value;
        setChanged();
    }

    public byte[] getTypeFilters() {
        return typeFilters;
    }

    public void setSpeciesFilters(String[] speciesFilters) {
        if (speciesFilters.length != 36) {
            ComplicatedBees.LOGGER.error("tried to set species filter on bee sorter with incorrect array size at position {}", getBlockPos());
            return;
        }
        this.speciesFilters = speciesFilters;
        setChanged();
    }

    public String[] getSpeciesFilters() {
        return speciesFilters;
    }

    private boolean testFilter(int index, ItemStack stack) {
        return switch (typeFilters[index]) {
            case 1 -> stack.is(ItemsRegistration.DRONE.get());
            case 2 -> stack.is(ItemsRegistration.PRINCESS.get());
            case 3 -> stack.is(ItemsRegistration.QUEEN.get());
            case 4 -> stack.is(ItemTagGenerator.BEE);
            case 5 -> !stack.is(ItemTagGenerator.BEE);
            default -> false;
        };
    }

    private LazyOptional<IItemHandler> createFilterHandler(int index) {
        return LazyOptional.of(() -> new AdaptedItemHandler(item) {
            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (filterSpecificity(Direction.values()[index]) > 0) {
                    return super.extractItem(slot, amount, simulate);
                }
                return ItemStack.EMPTY;
            }
        });
    }

    private boolean matchFilter(String species, String filter) {
        return Objects.equals(species, filter) || filter.isEmpty();
    }


    /**
     * @param dir the side to check
     * @return an integer representing the filter level of the side
     * 0 - not valid
     * 1 - type and empty filter match
     * 2 - type and one species filter match
     * 3 - type and two species filter match
     */
    private int filterSpecificity(Direction dir) {
        int level = 0;
        ItemStack stack = item.getStackInSlot(0);
        if (!stack.isEmpty() & testFilter(dir.ordinal(), stack)) {
            if (!hasFilters(dir)) //no species filters, purely on type filter
                return 1;
            for (int i = 0; i < 3; i++) {
                String primarySpeciesFilter = speciesFilters[dir.ordinal()*6+i*2];
                String secondarySpeciesFilter = speciesFilters[dir.ordinal()*6+i*2+1];
                String primarySpecies = GeneticHelper.getRaw(stack, GeneSpecies.ID, true).getString(GeneSpecies.DATA);
                String secondarySpecies = GeneticHelper.getRaw(stack, GeneSpecies.ID, false).getString(GeneSpecies.DATA);
                if (primarySpecies.isEmpty() || secondarySpecies.isEmpty()) {
                    ComplicatedBees.LOGGER.error("tried filtering item with missing species tag at pos {}", getBlockPos());
                    break;
                }
                if (matchFilter(primarySpecies, primarySpeciesFilter) & matchFilter(secondarySpecies, secondarySpeciesFilter)) {
                    if (primarySpeciesFilter.isEmpty() ^ secondarySpeciesFilter.isEmpty()) //xor, if only one filter is set
                        level = Math.max(2, level);
                    else if (!primarySpeciesFilter.isEmpty() & !secondarySpeciesFilter.isEmpty())
                        return 3; //matches both primary and secondary
                }
            }
        }
        return level;
    }

    private boolean hasFilters(Direction dir) {
        return !(speciesFilters[dir.ordinal()*6].isEmpty()
                & speciesFilters[dir.ordinal()*6+1].isEmpty()
                & speciesFilters[dir.ordinal()*6+2].isEmpty()
                & speciesFilters[dir.ordinal()*6+3].isEmpty()
                & speciesFilters[dir.ordinal()*6+4].isEmpty()
                & speciesFilters[dir.ordinal()*6+5].isEmpty());
    }

    public ItemStackHandler getItem() {
        return item;
    }
}
