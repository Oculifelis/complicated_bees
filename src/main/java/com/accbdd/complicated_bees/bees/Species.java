package com.accbdd.complicated_bees.bees;


import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.bees.gene.Gene;
import com.accbdd.complicated_bees.bees.gene.GeneSpecies;
import com.accbdd.complicated_bees.item.BeeItem;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;
import static com.accbdd.complicated_bees.util.ComplicatedBeesCodecs.HEX_STRING_CODEC;
import static com.accbdd.complicated_bees.util.ComplicatedBeesCodecs.HEX_STRING_STREAM_CODEC;

/**
 * Defines the color and products of a bee, as well as the default genes for things like JEI display and world drops.
 */
public class Species {
    private final int color;
    private final int nest_color;
    private List<ResourceLocation> models;
    private final List<Product> products;
    private final List<Product> specialty_products;
    private final Chromosome default_chromosome;
    private final boolean dominant;
    private final boolean foil;

    public ResourceLocation builderOverride; //used in datagen, ignore otherwise

    public static List<ResourceLocation> DEFAULT_MODELS = new ArrayList<>() {{
        add(ResourceLocation.fromNamespaceAndPath(MODID, "bee/base_drone"));
        add(ResourceLocation.fromNamespaceAndPath(MODID, "bee/base_princess"));
        add(ResourceLocation.fromNamespaceAndPath(MODID, "bee/base_queen"));
    }};
    public static final Species INVALID = new Species();

    public static final Codec<Species> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("dominant", true).forGetter(Species::isDominant),
            Codec.BOOL.optionalFieldOf("foil", false).forGetter(Species::isFoil),
            ResourceLocation.CODEC.listOf().optionalFieldOf("models", DEFAULT_MODELS).forGetter(Species::getModels),
            HEX_STRING_CODEC.optionalFieldOf("color", -1).forGetter(Species::getColor),
            HEX_STRING_CODEC.optionalFieldOf("nest_color", -1).forGetter(Species::getNestColor),
            Product.CODEC.listOf().optionalFieldOf("products", new ArrayList<>()).forGetter(Species::getProducts),
            Product.CODEC.listOf().optionalFieldOf("specialty_products", new ArrayList<>()).forGetter(Species::getSpecialtyProducts),
            Chromosome.CODEC.optionalFieldOf("default_chromosome", new Chromosome()).forGetter(Species::getDefaultChromosome)
    ).apply(instance, Species::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Species> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public Species decode(RegistryFriendlyByteBuf buffer) {
            boolean dominant = ByteBufCodecs.BOOL.decode(buffer);
            boolean foil = ByteBufCodecs.BOOL.decode(buffer);
            List<ResourceLocation> models = ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
            int color = HEX_STRING_STREAM_CODEC.decode(buffer);
            int nest_color = HEX_STRING_STREAM_CODEC.decode(buffer);
            List<Product> products = Product.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
            List<Product> specialtyProducts = Product.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
            Chromosome default_chromosome = Chromosome.STREAM_CODEC.decode(buffer);
            return new Species(dominant, foil, models, color, nest_color, products, specialtyProducts, default_chromosome);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, Species value) {
            ByteBufCodecs.BOOL.encode(buffer, value.dominant);
            ByteBufCodecs.BOOL.encode(buffer, value.foil);
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, value.models);
            HEX_STRING_STREAM_CODEC.encode(buffer, value.color);
            HEX_STRING_STREAM_CODEC.encode(buffer, value.nest_color);
            Product.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, value.products);
            Product.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, value.specialty_products);
            Chromosome.STREAM_CODEC.encode(buffer, value.default_chromosome);
        }
    };

    public Species() {
        this(false, false, new ArrayList<>(), 0xFFFFFF, 0xFFFFFF, new ArrayList<>(), new ArrayList<>(), new Chromosome());
        this.models = DEFAULT_MODELS;
    }

    public Species(boolean dominant, boolean foil, List<ResourceLocation> models, int color, int nest_color, List<Product> products, List<Product> specialtyProducts, Chromosome default_chromosome) {
        this.dominant = dominant;
        this.foil = foil;
        this.models = models;
        this.color = color;
        this.nest_color = nest_color;
        this.products = products;
        this.specialty_products = specialtyProducts;
        this.default_chromosome = default_chromosome.setGene(GeneSpecies.ID, new GeneSpecies(this, dominant));
    }

    public Species(boolean dominant, boolean foil, List<ResourceLocation> models, int color, int nest_color, List<Product> products, List<Product> specialtyProducts, CompoundTag defaultGenomeAsTag) {
        this(dominant, foil, models, color, nest_color, products, specialtyProducts, new Chromosome(defaultGenomeAsTag));
        default_chromosome.setGene(GeneSpecies.ID, new GeneSpecies(this, dominant));
    }

    public boolean isFoil() {
        return this.foil;
    }

    public boolean isDominant() {
        return this.dominant;
    }

    public List<ResourceLocation> getModels() {
        return this.models;
    }

    public int getColor() {
        return this.color;
    }

    public int getNestColor() {
        return this.nest_color == -1 ? this.color : this.nest_color;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getSpecialtyProducts() {
        return specialty_products;
    }

    public Chromosome getDefaultChromosome() {
        return default_chromosome.copy();
    }

    public ItemStack toStack(Item item) {
        ItemStack stack = new ItemStack(item);
        stack.getOrCreateTag().putBoolean(BeeItem.ANALYZED_TAG, true);
        return GeneticHelper.setGenome(stack, new Genome(getDefaultChromosome(), getDefaultChromosome()));
    }

    public List<ItemStack> toMembers() {
        List<ItemStack> members = new ArrayList<>();
        members.add(this.toStack(ItemsRegistration.QUEEN.get()));
        members.add(this.toStack(ItemsRegistration.PRINCESS.get()));
        members.add(this.toStack(ItemsRegistration.DRONE.get()));
        return members;
    }

    public static class Builder {
        private boolean dominant;
        private boolean foil;
        private List<ResourceLocation> models;
        private int color;
        private int nest_color;
        private List<Product> products;
        private List<Product> specialty_products;
        private CompoundTag default_chromosome;
        private final ResourceLocation builderOverride;

        public static Builder of(Species copied, ResourceLocation builderOverride) {
            Builder builder = new Builder(builderOverride);
            builder.dominant = copied.dominant;
            builder.foil = copied.foil;
            builder.models = copied.models;
            builder.color = copied.color;
            builder.nest_color = copied.nest_color;
            builder.products = copied.products;
            builder.specialty_products = copied.specialty_products;
            builder.default_chromosome = copied.default_chromosome.serialize();
            return builder;
        }

        public Builder(ResourceLocation builderOverride) {
            this.dominant = true;
            this.foil = false;
            this.models = DEFAULT_MODELS;
            this.color = -1;
            this.nest_color = -1;
            this.products = new ArrayList<>();
            this.specialty_products = new ArrayList<>();
            this.default_chromosome = new CompoundTag();
            this.builderOverride = builderOverride;
        }

        public Species build() {
            Species species = new Species(dominant, foil, models, color, nest_color, products, specialty_products, default_chromosome);
            species.builderOverride = this.builderOverride;
            return species;
        }

        public Builder dominant(boolean value) {
            this.dominant = value;
            return this;
        }

        public Builder foil(boolean value) {
            this.foil = value;
            return this;
        }

        public Builder models(List<ResourceLocation> models) {
            if (models.size() != 3)
                throw new IllegalArgumentException("models should be a list of size 3");
            this.models = models;
            return this;
        }

        public Builder colors(int color, int nest_color) {
            this.color = color;
            this.nest_color = nest_color;
            return this;
        }

        public Builder colors(int color) {
            this.color = color;
            this.nest_color = color;
            return this;
        }

        public Builder products(List<Product> products) {
            this.products = products;
            return this;
        }

        public Builder specialtyProducts(List<Product> specialty_products) {
            this.specialty_products = specialty_products;
            return this;
        }

        public Builder defaultChromosome(Chromosome chromosome) {
            this.default_chromosome = chromosome.serialize();
            return this;
        }

        public <T extends Gene<?>> Builder gene(Supplier<T> gene, T geneValue) {
            default_chromosome.put(ComplicatedBees.GENE_REGISTRY.get().getKey(gene.get()).toString(), geneValue.serialize());
            return this;
        }
    }
}
