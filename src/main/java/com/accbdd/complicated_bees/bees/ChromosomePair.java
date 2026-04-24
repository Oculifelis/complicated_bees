package com.accbdd.complicated_bees.bees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ChromosomePair(Chromosome chromosomeA, Chromosome chromosomeB) {
	public static final Codec<ChromosomePair> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Chromosome.CODEC.fieldOf("chromosome_a").forGetter(ChromosomePair::chromosomeA),
					Chromosome.CODEC.fieldOf("chromosome_b").forGetter(ChromosomePair::chromosomeB)
			).apply(instance, ChromosomePair::new)
	);
	public static final StreamCodec<ByteBuf, ChromosomePair> STREAM_CODEC = StreamCodec.composite(
			Chromosome.STREAM_CODEC,
			ChromosomePair::chromosomeA,
			Chromosome.STREAM_CODEC,
			ChromosomePair::chromosomeB,
			ChromosomePair::new
	);
}
