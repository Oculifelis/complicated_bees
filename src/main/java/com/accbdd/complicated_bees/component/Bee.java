package com.accbdd.complicated_bees.component;

import com.accbdd.complicated_bees.bees.ChromosomePair;
import com.accbdd.complicated_bees.bees.Species;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Bee(float age, boolean analyzed, ChromosomePair chromosomes, ChromosomePair mate, Species species) {
	public static final Codec<Bee> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.FLOAT.fieldOf("age").forGetter(Bee::age),
					Codec.BOOL.fieldOf("analyzed").forGetter(Bee::analyzed),
					ChromosomePair.CODEC.fieldOf("chromosomes").forGetter(Bee::chromosomes),
					ChromosomePair.CODEC.fieldOf("mate").forGetter(Bee::mate),
					Species.CODEC.fieldOf("species").forGetter(Bee::species)
			).apply(instance, Bee::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, Bee> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT,
			Bee::age,
			ByteBufCodecs.BOOL,
			Bee::analyzed,
			ChromosomePair.STREAM_CODEC,
			Bee::chromosomes,
			ChromosomePair.STREAM_CODEC,
			Bee::mate,
			Species.STREAM_CODEC,
			Bee::species,
			Bee::new
	);
	
}
