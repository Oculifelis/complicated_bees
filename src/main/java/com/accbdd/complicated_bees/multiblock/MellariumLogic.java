package com.accbdd.complicated_bees.multiblock;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.block.entity.IBeeHousing;
import com.accbdd.complicated_bees.block.entity.MellariumBaseBlockEntity;
import com.accbdd.complicated_bees.genetics.BeeHousingModifier;
import com.accbdd.complicated_bees.util.BlockPosBoxIterator;
import com.accbdd.complicated_bees.util.enums.EnumErrorCodes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.UUID;

public class MellariumLogic implements IBeeHousing {
    private final Level level;
    private final BlockPos center;
    private UUID owner;

    public MellariumLogic(Level level, BlockPos center, UUID owner) {
        this.level = level;
        this.center = center;
        this.owner = owner;
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof MellariumBaseBlockEntity mellariumBase) {
                mellariumBase.setLogic(this);
            } else {
                ComplicatedBees.LOGGER.error("tried to build a mellarium with non-mellarium block at {}", pos);
            }
        }
        ComplicatedBees.LOGGER.debug("built new mellarium with center {}", center);
    }

    public void deconstruct() {
        BlockPosBoxIterator iterator = new BlockPosBoxIterator(center, 1, 1);
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (level.getBlockEntity(pos) instanceof MellariumBaseBlockEntity mellariumBase) {
                mellariumBase.setLogic(null);
            } else {
                ComplicatedBees.LOGGER.error("tried to deconstruct a mellarium with non-mellarium block at {}", pos);
            }
        }
        ComplicatedBees.LOGGER.debug("deconstructed mellarium with center {}", center);
    }

    public BlockPos getCenter() {
        return center;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public LazyOptional<IItemHandler> getItemHandler() {
        return null;
    }

    @Override
    public LazyOptional<IItemHandler> getBeeItemHandler() {
        return null;
    }

    @Override
    public LazyOptional<IItemHandler> getOutputItemHandler() {
        return null;
    }

    @Override
    public LazyOptional<IItemHandler> getFrameItemHandler() {
        return null;
    }

    @Override
    public void doBeeEffect() {
    }

    @Override
    public List<BeeHousingModifier> getHousingModifiers() {
        return null;
    }

    @Override
    public void addToOutput(ItemStack stack) {

    }

    @Override
    public boolean isQueenSatisfied() {
        return false;
    }

    @Override
    public boolean isQueenEcstatic() {
        return false;
    }

    @Override
    public int getErrors() {
        return 0;
    }

    @Override
    public void addError(EnumErrorCodes... error) {

    }

    @Override
    public void removeError(EnumErrorCodes... error) {

    }

    @Override
    public void beeTick() {

    }
}
