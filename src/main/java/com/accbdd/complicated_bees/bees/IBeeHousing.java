package com.accbdd.complicated_bees.bees;

import com.accbdd.complicated_bees.util.enums.EnumErrorCodes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;
import java.util.UUID;

/**
 * Interface that exposes common functions and utilities for doing bee stuff as related to housing them
 */
public interface IBeeHousing {
    void setOwner(UUID owner);
    UUID getOwner();

    LazyOptional<IItemHandlerModifiable> getItemHandler();
    LazyOptional<IItemHandlerModifiable> getBeeItemHandler();
    LazyOptional<IItemHandlerModifiable> getOutputItemHandler();
    LazyOptional<IItemHandlerModifiable> getFrameItemHandler();

    void doBeeEffect();

    List<BeeHousingModifier> getHousingModifiers();

    //hook for effects to add to output
    void addToOutput(ItemStack stack);

    boolean isQueenSatisfied();
    boolean isQueenEcstatic();

    BeeLogic getLogic();

    int getErrors();

    void addError(EnumErrorCodes... error);

    void removeError(EnumErrorCodes... error);

    void beeTick();
}
