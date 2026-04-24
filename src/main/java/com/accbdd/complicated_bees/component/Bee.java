package com.accbdd.complicated_bees.component;

import com.accbdd.complicated_bees.bees.Chromosome;
import com.accbdd.complicated_bees.bees.Genome;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public record Bee(float age, boolean analyzed, int generation, Genome genome, Genome mate, ResourceLocation species) {
	public static final Bee DEFAULT = new Bee(0, false, 0, new Genome(new Chromosome(), new Chromosome()), new Genome(new Chromosome(), new Chromosome()), ResourceLocation.fromNamespaceAndPath(MODID, "invalid"));

	public static final Codec<Bee> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.FLOAT.fieldOf("age").forGetter(Bee::age),
					Codec.BOOL.fieldOf("analyzed").forGetter(Bee::analyzed),
					Codec.INT.fieldOf("generation").forGetter(Bee::generation),
					Genome.CODEC.fieldOf("chromosomes").forGetter(Bee::genome),
					Genome.CODEC.fieldOf("mate").forGetter(Bee::mate),
					ResourceLocation.CODEC.fieldOf("species").forGetter(Bee::species)
			).apply(instance, Bee::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, Bee> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.FLOAT,
			Bee::age,
			ByteBufCodecs.BOOL,
			Bee::analyzed,
			ByteBufCodecs.INT,
			Bee::generation,
			Genome.STREAM_CODEC,
			Bee::genome,
			Genome.STREAM_CODEC,
			Bee::mate,
			ResourceLocation.STREAM_CODEC,
			Bee::species,
			Bee::new
	);

	public Bee withAge(float age) {
		return new Bee(age, this.analyzed, this.generation, this.genome, this.mate, this.species);
	}

	public Bee withAnalyzed(boolean analyzed) {
		return new Bee(this.age, analyzed, this.generation, this.genome, this.mate, this.species);
	}

	public Bee withGeneration(int generation) {
		return new Bee(this.age, this.analyzed, generation, this.genome, this.mate, this.species);
	}

	public Bee withGenome(Genome genome) {
		return new Bee(this.age, this.analyzed, this.generation, genome, this.mate, this.species);
	}

	public Bee withMate(Genome mate) {
		return new Bee(this.age, this.analyzed, this.generation, this.genome, mate, this.species);
	}

	public Bee withSpecies(ResourceLocation species) {
		return new Bee(this.age, this.analyzed, this.generation, this.genome, this.mate, species);
	}
}
