package com.accbdd.complicated_bees.bees.gene;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class GeneFlower extends Gene<Identifier> {
    public static final String TAG = "flower";
    public static final Identifier ID = Identifier.fromNamespaceAndPath(MODID, TAG);

    public GeneFlower() {
        super(Identifier.fromNamespaceAndPath(MODID, "invalid"), true);
    }

    public GeneFlower(Identifier flower, boolean dominant) {
        super(flower, dominant);
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put(DATA, StringTag.valueOf(get().toString()));
        tag.put(DOMINANT, ByteTag.valueOf(isDominant()));
        return tag;
    }

    @Override
    public GeneFlower deserialize(CompoundTag tag) {
        return new GeneFlower(Identifier.tryParse(tag.getStringOr(DATA, "")), tag.getBooleanOr(DOMINANT, false));
    }

    @Override
    public MutableComponent getTranslationKey() {
        return Component.translatable("flower.complicated_bees." + geneData.toString());
    }

    @Override
    public IGene<Identifier> copy() {
        return new GeneFlower(this.get(), this.isDominant());
    }
}
