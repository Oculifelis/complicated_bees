package com.accbdd.complicated_bees.component;

import com.accbdd.complicated_bees.bees.Chromosome;
import com.accbdd.complicated_bees.bees.Genome;
import com.accbdd.complicated_bees.bees.Species;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Bee(float age, boolean analyzed, Genome genome, Genome mate, Species species) {
	public static final Bee DEFAULT = new Bee(0, false, new Genome(new Chromosome(), new Chromosome()), new Genome(new Chromosome(), new Chromosome()), Species.INVALID);

	public static final Codec<Bee> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.FLOAT.fieldOf("age").forGetter(Bee::age),
					Codec.BOOL.fieldOf("analyzed").forGetter(Bee::analyzed),
					Genome.CODEC.fieldOf("chromosomes").forGetter(Bee::genome),
					Genome.CODEC.fieldOf("mate").forGetter(Bee::mate),
					Species.CODEC.fieldOf("species").forGetter(Bee::species)
			).apply(instance, Bee::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, Bee> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT,
			Bee::age,
			ByteBufCodecs.BOOL,
			Bee::analyzed,
			Genome.STREAM_CODEC,
			Bee::genome,
			Genome.STREAM_CODEC,
			Bee::mate,
			Species.STREAM_CODEC,
			Bee::species,
			Bee::new
	);

	public Bee withAge(float age) {
		return new Bee(age, this.analyzed, this.genome, this.mate, this.species);
	}

	public Bee withAnalyzed(boolean analyzed) {
		return new Bee(this.age, analyzed, this.genome, this.mate, this.species);
	}

	public Bee withGenome(Genome genome) {
		return new Bee(this.age, this.analyzed, genome, this.mate, this.species);
	}

	public Bee withMate(Genome mate) {
		return new Bee(this.age, this.analyzed, this.genome, mate, this.species);
	}

	public Bee withSpecies(Species species) {
		return new Bee(this.age, this.analyzed, this.genome, this.mate, species);
	}
}
