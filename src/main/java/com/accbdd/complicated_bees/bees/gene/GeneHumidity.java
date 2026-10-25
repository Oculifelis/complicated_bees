package com.accbdd.complicated_bees.bees.gene;

import com.accbdd.complicated_bees.bees.gene.enums.EnumHumidity;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class GeneHumidity extends GeneTolerant<EnumHumidity> {
    public static final String TAG = "humidity";
    public static final Identifier ID = Identifier.fromNamespaceAndPath(MODID, TAG);

    public GeneHumidity() {
        this(EnumHumidity.NORMAL, EnumTolerance.NONE, true);
    }

    public GeneHumidity(EnumHumidity enumHumidity, EnumTolerance enumTolerance, boolean dominant) {
        super(enumHumidity, enumTolerance, dominant);
    }

    @Override
    public GeneHumidity deserialize(CompoundTag tag) {
        return new GeneHumidity(EnumHumidity.getFromString(tag.getStringOr(DATA, "")), EnumTolerance.getFromString(tag.getStringOr(TOLERANCE, "")), tag.getBooleanOr(DOMINANT, false));
    }

    @Override
    public MutableComponent getTranslationKey() {
        return geneData.getTranslationKey();
    }

    @Override
    public IGene<EnumHumidity> copy() {
        return new GeneHumidity(this.get(), this.getTolerance(), this.isDominant());
    }
}
