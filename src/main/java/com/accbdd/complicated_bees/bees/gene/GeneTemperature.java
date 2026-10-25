package com.accbdd.complicated_bees.bees.gene;

import com.accbdd.complicated_bees.bees.gene.enums.EnumTemperature;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class GeneTemperature extends GeneTolerant<EnumTemperature> {
    public static final String TAG = "temperature";
    public static final Identifier ID = Identifier.fromNamespaceAndPath(MODID, TAG);

    public GeneTemperature() {
        super(EnumTemperature.NORMAL, EnumTolerance.NONE, true);
    }

    public GeneTemperature(EnumTemperature temperature, EnumTolerance tolerance, boolean dominant) {
        super(temperature, tolerance, dominant);
    }

    @Override
    public GeneTolerant<EnumTemperature> deserialize(CompoundTag tag) {
        return new GeneTemperature(EnumTemperature.getFromString(tag.getStringOr(DATA, "")), EnumTolerance.getFromString(tag.getStringOr(TOLERANCE, "")), !tag.contains(DOMINANT) || tag.getBooleanOr(DOMINANT, false));
    }

    @Override
    public MutableComponent getTranslationKey() {
        return geneData.getTranslationKey();
    }

    @Override
    public IGene<EnumTemperature> copy() {
        return new GeneTemperature(this.get(), this.getTolerance(), this.isDominant());
    }
}
