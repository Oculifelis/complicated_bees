package com.accbdd.complicated_bees.bees.gene;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class GeneTerritory extends Gene<int[]> {
    public static final String TAG = "territory";
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MODID, TAG);

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
        if (tag.contains(DATA, Tag.TAG_INT_ARRAY))
            return new GeneTerritory(tag.getIntArray(DATA), tag.getBoolean(DOMINANT));
        else if (tag.contains(DATA, Tag.TAG_BYTE_ARRAY))
            return new GeneTerritory(tag.getByteArray(DATA)[0], tag.getByteArray(DATA)[1], tag.getBoolean(DOMINANT));
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
}
