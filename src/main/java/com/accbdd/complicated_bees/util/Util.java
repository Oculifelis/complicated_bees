package com.accbdd.complicated_bees.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Objects;

public class Util {
    public static boolean canSeeSky(Level level, BlockPos pos) {
        if (level.canSeeSky(pos)) {
            //vanilla behaviour (check if sky light at position = max light in dimension)
            return true;
        } else if (level.dimensionType().hasCeiling()) { //dimension has a ceiling
            return false;
        } else if (!level.dimensionType().hasSkyLight()) { //no ceiling, but no sky light
            for (int y = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY(); y > pos.getY(); y--) {//loop through top block down, looking for skylight blocking blocks
                if (!level.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).propagatesSkylightDown(level, pos)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param match the compoundtag to match
     * @param pattern the pattern the compoundtag should match
     * @return whether all keys in pattern are equivalent to all keys in match
     */
    public static boolean weakNbtMatch(CompoundTag match, CompoundTag pattern) {
        for (String key : pattern.getAllKeys()) {
            if (!Objects.equals(match.get(key), pattern.get(key)))
                return false;
        }
        return true;
    }

    /**
     * Moves as many items as possible from the source to the target
     */
    public static void moveInventoryItems(IItemHandler sourceInventory, IItemHandler targetInventory) {
        for (int srcIndex = 0; srcIndex < sourceInventory.getSlots(); srcIndex++) {
            ItemStack sourceStack = sourceInventory.extractItem(srcIndex, Integer.MAX_VALUE, true);
            if (sourceStack.isEmpty()) {
                continue;
            }
            ItemStack remainder = insertItem(targetInventory, sourceStack, true);
            int amountToInsert = sourceStack.getCount() - remainder.getCount();
            if (amountToInsert > 0) {
                sourceStack = sourceInventory.extractItem(srcIndex, amountToInsert, false);
                insertItem(targetInventory, sourceStack, false);
            }
        }
    }

    /**
     * Inserts items by trying to fill slots with the same item first, and then fill empty slots.
     */
    public static ItemStack insertItem(IItemHandler handler, ItemStack stack, boolean simulate) {
        if (handler == null || stack.isEmpty()) {
            return stack;
        }

        IntList emptySlots = new IntArrayList();
        int slots = handler.getSlots();

        for (int i = 0; i < slots; i++) {
            ItemStack slotStack = handler.getStackInSlot(i);
            if (slotStack.isEmpty()) {
                emptySlots.add(i);
            } else if (ItemHandlerHelper.canItemStacksStack(stack, slotStack)) {
                stack = handler.insertItem(i, stack, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
        }

        for (int slot : emptySlots) {
            stack = handler.insertItem(slot, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }
}
