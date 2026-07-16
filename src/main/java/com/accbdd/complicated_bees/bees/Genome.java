package com.accbdd.complicated_bees.bees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public record Genome(Chromosome primary, Chromosome secondary) {
    public static final Codec<Genome> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Chromosome.CODEC.fieldOf("primary").forGetter(Genome::primary),
                    Chromosome.CODEC.fieldOf("secondary").forGetter(Genome::secondary)
            ).apply(instance, Genome::new)
    );
    public static final StreamCodec<ByteBuf, Genome> STREAM_CODEC = StreamCodec.composite(
            Chromosome.STREAM_CODEC,
            Genome::primary,
            Chromosome.STREAM_CODEC,
            Genome::secondary,
            Genome::new
    );

    public Genome(Chromosome chromosome) {
        this(chromosome.copy(), chromosome.copy());
    }

    public Chromosome primary() {
        return primary;
    }

    public Chromosome secondary() {
        return secondary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genome genome)) return false;
        return Objects.equals(primary, genome.primary) && Objects.equals(secondary, genome.secondary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primary, secondary);
    }
}
