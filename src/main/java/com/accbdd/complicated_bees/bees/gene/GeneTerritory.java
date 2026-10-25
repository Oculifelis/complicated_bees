package com.accbdd.complicated_bees.bees.gene;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import java.util.Arrays;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class GeneTerritory extends Gene<int[]> {
    public static final String TAG = "territory";
    public static final Identifier ID = Identifier.fromNamespaceAndPath(MODID, TAG);

    public GeneTerritory() {
        this(4, 2, true);
    }

    public GeneTerritory(int hRadius, int vRadius, boolean dominant) {
        this(new int[]{hRadius, vRadius}, dominant);
    }

    public GeneTerritory(int[] dimensions, boolean dominant) {
        super(dimensions, dominant);
    }

    @Override
    public GeneTerritory deserialize(CompoundTag tag) {
        if (tag.get(DATA) instanceof net.minecraft.nbt.IntArrayTag)
            return new GeneTerritory(tag.getIntArray(DATA).orElse(new int[]{1,1}), tag.getBooleanOr(DOMINANT, false));
        else if (tag.get(DATA) instanceof net.minecraft.nbt.ByteArrayTag)
            return new GeneTerritory(tag.getByteArray(DATA).orElse(new byte[]{1,1})[0], tag.getByteArray(DATA).orElse(new byte[]{1,1})[1], tag.getBooleanOr(DOMINANT, false));
        else return new GeneTerritory();
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putIntArray(DATA, get());
        tag.putBoolean(DOMINANT, isDominant());
        return tag;
    }

    @Override
    public MutableComponent getTranslationKey() {
        return Component.translatable("gene.complicated_bees.territory_value", get()[0] * 2 + 1, get()[1] * 2 + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneTerritory gene)) return false;
        return isDominant() == gene.isDominant() && Arrays.equals(get(), gene.get());
    }

    @Override
    public IGene<int[]> copy() {
        return new GeneTerritory(this.get(), this.isDominant());
    }
}
