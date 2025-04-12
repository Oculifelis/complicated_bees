package com.accbdd.complicated_bees.bees;

import com.accbdd.complicated_bees.registry.CombRegistration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;
import static com.accbdd.complicated_bees.util.ComplicatedBeesCodecs.HEX_STRING_CODEC;

public class Comb {
    public static final Codec<Comb> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    HEX_STRING_CODEC.fieldOf("outer_color").forGetter(Comb::getOuterColor),
                    HEX_STRING_CODEC.fieldOf("inner_color").forGetter(Comb::getInnerColor)
            ).apply(instance, Comb::new)
    );

    private final int outerColor;
    private final int innerColor;

    public static final Comb NULL = new Comb(0xe7d46a, 0xfea02b);

    public Comb(int outerColor, int innerColor) {
        this.outerColor = outerColor;
        this.innerColor = innerColor;
    }

    public ResourceLocation getId() {
        ResourceLocation id;
        try {
            id = Minecraft.getInstance().getConnection().registryAccess().registry(CombRegistration.COMB_REGISTRY_KEY).get().getKey(this);
        } catch (NullPointerException e) {
            return new ResourceLocation(MODID, "null");
        }
        return id == null ? new ResourceLocation(MODID, "null") : id;
    }

    public int getOuterColor() {
        return this.outerColor;
    }

    public int getInnerColor() {
        return this.innerColor;
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}
