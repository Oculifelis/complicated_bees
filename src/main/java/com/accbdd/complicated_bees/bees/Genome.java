package com.accbdd.complicated_bees.bees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

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
        this(chromosome, chromosome);
    }

    public Chromosome primary() {
        return primary;
    }

    public Chromosome secondary() {
        return secondary;
    }
}
