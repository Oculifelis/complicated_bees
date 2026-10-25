package com.accbdd.complicated_bees.port;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.*;

/** Persistence adapter; inventory mutation and slot validation stay in NeoForge. */
public class ItemStackHandler extends net.neoforged.neoforge.items.ItemStackHandler {
    public ItemStackHandler() { super(); }
    public ItemStackHandler(int size) { super(size); }
    public ItemStackHandler(NonNullList<ItemStack> stacks) { super(stacks); }
    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        serialize(output);
        return output.buildResult();
    }
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag tag) {
        deserialize(TagValueInput.create(ProblemReporter.DISCARDING, registries, tag));
    }
}
