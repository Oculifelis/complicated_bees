package com.accbdd.complicated_bees.bees;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.bees.gene.Gene;
import com.accbdd.complicated_bees.bees.gene.IGene;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Chromosome {
    public static final Codec<Chromosome> CODEC = CompoundTag.CODEC.xmap(Chromosome::deserialize, Chromosome::serialize);
    public static final StreamCodec<ByteBuf, Chromosome> STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(Chromosome::deserialize, Chromosome::serialize);

    private Map<Identifier, IGene<?>> genes;

    public Chromosome() {
        this.genes = new HashMap<>();
        if (ComplicatedBees.GENE_REGISTRY.get() != null)
            for (Map.Entry<ResourceKey<IGene<?>>, IGene<?>> entry : ComplicatedBees.GENE_REGISTRY.get().entrySet()) {
                genes.put(entry.getKey().identifier(), ComplicatedBees.GENE_REGISTRY.get().getValue(entry.getKey().identifier()));
            }
    }

    public Chromosome(CompoundTag genomeAsTag) {
        this.genes = new HashMap<>();
        for (Map.Entry<ResourceKey<IGene<?>>, IGene<?>> entry : ComplicatedBees.GENE_REGISTRY.get().entrySet()) {
            Identifier geneLocation = entry.getKey().identifier();
            if (genomeAsTag.contains(geneLocation.toString())) {
                CompoundTag serializedGene = genomeAsTag.getCompoundOrEmpty(geneLocation.toString());
                if (!serializedGene.contains(Gene.DOMINANT))
                    serializedGene.putBoolean(Gene.DOMINANT, true);
                genes.put(geneLocation, entry.getValue().deserialize(serializedGene));
            } else {
                genes.put(entry.getKey().identifier(), ComplicatedBees.GENE_REGISTRY.get().getValue(entry.getKey().identifier()));
            }
        }
    }

    public Chromosome(Species species) {
        this.genes = species.getDefaultChromosome().getGenes();
    }

    public Chromosome(Map<Identifier, IGene<?>> genes) {
        this.genes = genes;
    }

    public Map<Identifier, IGene<?>> getGenes() {
        return genes;
    }

    public Chromosome copy() {
        return new Chromosome(this.serialize());
    }

    public Chromosome setGenes(Map<Identifier, IGene<?>> genes) {
        this.genes = genes;
        return this;
    }

    public IGene<?> getGene(Identifier id) {
        return this.genes.getOrDefault(id, ComplicatedBees.GENE_REGISTRY.get().getValue(id));
    }

    public Chromosome setGene(Identifier id, IGene<?> gene) {
        this.genes.put(id, gene);
        return this;
    }

    public Chromosome removeGene(Identifier id) {
        this.genes.remove(id);
        return this;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        Map<Identifier, IGene<?>> genes = this.getGenes();
        for (Identifier key : genes.keySet()) {
            tag.put(key.toString(), genes.get(key).serialize());
        }
        return tag;
    }

    public static Chromosome deserialize(CompoundTag tag) {
        Map<Identifier, IGene<?>> genes = new HashMap<>();
        for (Map.Entry<ResourceKey<IGene<?>>, IGene<?>> entry : ComplicatedBees.GENE_REGISTRY.get().entrySet()) {
            Identifier id = entry.getKey().identifier();
            CompoundTag geneData = tag.getCompoundOrEmpty(id.toString());
            if (!geneData.equals(new CompoundTag())) {
                if (!geneData.contains(Gene.DOMINANT))
                    geneData.putBoolean(Gene.DOMINANT, true);
                genes.put(id, Objects.requireNonNull(ComplicatedBees.GENE_REGISTRY.get().getValue(id)).deserialize(geneData));

            }
        }
        return new Chromosome(genes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chromosome that)) return false;
        return Objects.equals(getGenes(), that.getGenes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGenes());
    }
}
