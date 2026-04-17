package com.accbdd.complicated_bees.bees;

import com.accbdd.complicated_bees.bees.gene.GeneSpecies;
import com.accbdd.complicated_bees.bees.gene.GeneTolerant;
import com.accbdd.complicated_bees.bees.gene.IGene;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import com.accbdd.complicated_bees.bees.mutation.Mutation;
import com.accbdd.complicated_bees.bees.mutation.condition.IMutationCondition;
import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.component.Bee;
import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.item.BeeItem;
import com.accbdd.complicated_bees.item.PrincessItem;
import com.accbdd.complicated_bees.item.QueenItem;
import com.accbdd.complicated_bees.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneticHelper {
    public static final String CHROMOSOME_A = "chromosome_a";
    public static final String CHROMOSOME_B = "chromosome_b";
    public static final String SPECIES = "species";
    public static final String MATE = "mate";
    private static final Random rand = new Random();

    public static Chromosome getChromosome(ItemStack stack, boolean primary) {
        Genome genome = stack.getOrDefault(EsotericRegistration.BEE, Bee.DEFAULT).genome();
        return primary ? genome.primary() : genome.secondary();
    }

    public static Genome getGenome(ItemStack stack) {
        return stack.getOrDefault(EsotericRegistration.BEE, Bee.DEFAULT).genome();
    }

    public static ItemStack setGenome(ItemStack stack, Chromosome chromosome, boolean primary) {
        stack.update(EsotericRegistration.BEE, Bee.DEFAULT, bee -> primary ? bee.withGenome(new Genome(chromosome, bee.genome().secondary())) : bee.withGenome(new Genome(bee.genome().primary(), chromosome)));
        return stack;
    }

    public static ItemStack setGenome(ItemStack stack, Genome genome) {
        stack.update(EsotericRegistration.BEE, Bee.DEFAULT, bee -> bee.withGenome(genome));
        setSpeciesTag(stack);
        return stack;
    }

    public static ItemStack setGenome(ItemStack stack, Chromosome chromosome) {
        setGenome(stack, new Genome(chromosome));
        return stack;
    }

    public static void setMate(ItemStack stack, Genome genome) {
        stack.update(EsotericRegistration.BEE, Bee.DEFAULT, bee -> bee.withMate(genome));
    }

    public static RegistryAccess getRegistryAccess() {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            if (FMLLoader.getDist().equals(Dist.DEDICATED_SERVER) || Minecraft.getInstance() == null) //datagen
                return null;
            if (Minecraft.getInstance().getConnection() == null) {
                return null;
            } else {
                return Minecraft.getInstance().getConnection().registryAccess();
            }
        } else {
            return ServerLifecycleHooks.getCurrentServer().registryAccess();
        }
    }

    public static MutableComponent getTranslationKey(Species species) {
        RegistryAccess registryAccess = getRegistryAccess();
        return Component.translatable("species.complicated_bees." + registryAccess.registry(SpeciesRegistration.SPECIES_REGISTRY_KEY).get().getKey(species));
    }

    public static MutableComponent getGenusTaxonomyKey(Species species) {
        RegistryAccess registryAccess = getRegistryAccess();
        MutableComponent mutableComponent = Component.translatableWithFallback("species.complicated_bees." + registryAccess.registry(SpeciesRegistration.SPECIES_REGISTRY_KEY).get().getKey(species) + ".genus", "null");
        return mutableComponent.getString().equals("null") ? Component.translatable("gui.complicated_bees.no_genus") : mutableComponent;
    }

    public static MutableComponent getSpeciesTaxonomyKey(Species species) {
        RegistryAccess registryAccess = getRegistryAccess();
        MutableComponent mutableComponent = Component.translatableWithFallback("species.complicated_bees." + registryAccess.registry(SpeciesRegistration.SPECIES_REGISTRY_KEY).get().getKey(species) + ".species_taxonomy", "null");
        return mutableComponent.getString().equals("null") ? Component.translatable("gui.complicated_bees.no_species") : mutableComponent;
    }

    public static MutableComponent getFlavorTextKey(Species species) {
        RegistryAccess registryAccess = getRegistryAccess();
        MutableComponent mutableComponent = Component.translatableWithFallback("species.complicated_bees." + registryAccess.registry(SpeciesRegistration.SPECIES_REGISTRY_KEY).get().getKey(species) + ".flavor_text", "null");
        return mutableComponent.getString().equals("null") ? Component.translatable("gui.complicated_bees.no_flavor") : mutableComponent;
    }

    public static MutableComponent getFlavorTextAuthorKey(Species species) {
        RegistryAccess registryAccess = getRegistryAccess();
        MutableComponent mutableComponent = Component.translatableWithFallback("species.complicated_bees." + registryAccess.registry(SpeciesRegistration.SPECIES_REGISTRY_KEY).get().getKey(species) + ".flavor_author", "null");
        return mutableComponent.getString().equals("null") ? Component.translatable("gui.complicated_bees.no_author") : mutableComponent;
    }

    public static MutableComponent getAuthorityKey(Species species) {
        RegistryAccess registryAccess = getRegistryAccess();
        MutableComponent mutableComponent = Component.translatableWithFallback("species.complicated_bees." + registryAccess.registry(SpeciesRegistration.SPECIES_REGISTRY_KEY).get().getKey(species) + ".authority", "null");
        return mutableComponent.getString().equals("null") ? Component.translatable("gui.complicated_bees.no_authority") : mutableComponent;
    }

    public static MutableComponent getTranslationKey(Flower flower) {
        RegistryAccess registryAccess = getRegistryAccess();
        return Component.translatable("flower.complicated_bees." + registryAccess.registry(FlowerRegistration.FLOWER_REGISTRY_KEY).get().getKey(flower));
    }

    public static MutableComponent getSpeciesHybridName(ItemStack stack) {
        if (stack.getItem() instanceof BeeItem) {
            Species primary = GeneticHelper.getSpecies(stack, true);
            Species secondary = GeneticHelper.getSpecies(stack, false);
            if (!primary.equals(secondary)) {
                MutableComponent name = Component.empty();
                name.append(GeneticHelper.getTranslationKey(primary));
                name.append("-").append(GeneticHelper.getTranslationKey(secondary));
                name.append(" ").append(Component.translatable("gene.complicated_bees.hybrid"));
                return name;
            }
        }
        return null;
    }

    public static void setSpeciesTag(ItemStack stack) {
        Species species = getSpecies(stack, true);
        stack.update(EsotericRegistration.BEE, Bee.DEFAULT, bee -> bee.withSpecies(species));
    }

    /**
     * @param stack the stack to test
     * @param id id of the gene
     * @param primary whether to get the primary chromosome or not
     * @return raw data in a gene
     */
    public static CompoundTag getRaw(ItemStack stack, ResourceLocation id, boolean primary) {
        if (stack.has(EsotericRegistration.BEE)) {
            Genome chromosomes = stack.get(EsotericRegistration.BEE).genome();
            CompoundTag chromosomeTag = primary ? chromosomes.primary().serialize() : chromosomes.secondary().serialize();
            if (chromosomeTag.contains(id.toString())) {
                return chromosomeTag.getCompound(id.toString());
            }
        }
        return new CompoundTag();
    }

    public static IGene<?> getGene(ItemStack stack, ResourceLocation id, boolean primary) {
        return getChromosome(stack, primary).getGene(id);
    }

    public static Species getSpecies(ItemStack stack, boolean primary) {
        Species species = (Species) getGene(stack, GeneSpecies.ID, primary).get();
        return species == null ? Species.INVALID : species;
    }

    public static ResourceLocation getSpeciesLoc(ItemStack stack) {
        if (stack.has(EsotericRegistration.BEE))
            return SpeciesRegistration.getResourceLocation(stack.get(EsotericRegistration.BEE).species());
        return null;
    }

    public static Object getGeneValue(ItemStack stack, ResourceLocation id, boolean primary) {
        return getGene(stack, id, primary).get();
    }

    private static Genome mixGenomes(Genome left, Genome right, Level level, BlockPos pos, float... mutationModifiers) {
        Chromosome chromosome_a = new Chromosome();
        Chromosome chromosome_b = new Chromosome();
        float mutationChanceMod = 1;
        for (float f : mutationModifiers) {
            mutationChanceMod *= f;
        }
        List<Mutation> possibleMutations = new ArrayList<>();
        BreedingTracker tracker = null;

        if (level.getBlockEntity(pos) instanceof IBeeHousing housing && housing.getOwner() != null) {
            tracker = BreedingTracker.getTracker(housing.getOwner());
        }

        for (Map.Entry<ResourceLocation, IGene<?>> geneEntry : chromosome_a.getGenes().entrySet()) {
            ResourceLocation key = geneEntry.getKey();
            IGene<?> geneA = (rand.nextFloat() < 0.5 ? left.primary() : left.secondary()).getGene(key);
            IGene<?> geneB = (rand.nextFloat() < 0.5 ? right.primary() : right.secondary()).getGene(key);

            if (geneEntry.getValue() instanceof GeneTolerant) {
                EnumTolerance toleranceA = ((GeneTolerant<?>) (rand.nextFloat() < 0.5 ? left.primary() : left.secondary()).getGene(key)).getTolerance();
                EnumTolerance toleranceB = ((GeneTolerant<?>) (rand.nextFloat() < 0.5 ? right.primary() : right.secondary()).getGene(key)).getTolerance();
                geneA = ((GeneTolerant<?>) geneA).setTolerance(toleranceA);
                geneB = ((GeneTolerant<?>) geneB).setTolerance(toleranceB);
            } else if (geneEntry.getValue() instanceof GeneSpecies) {
                Species speciesA = (Species) geneA.get();
                Species speciesB = (Species) geneB.get();
                for (Mutation mutation : ServerLifecycleHooks.getCurrentServer().registryAccess().registry(MutationRegistration.MUTATION_REGISTRY_KEY).get().stream().toList()) {
                    if ((mutation.getFirstSpecies() == speciesA && mutation.getSecondSpecies() == speciesB) || (mutation.getSecondSpecies() == speciesA && mutation.getFirstSpecies() == speciesB)) {
                        boolean canMutate = true;
                        for (IMutationCondition condition : mutation.getConditions())
                            canMutate = canMutate && condition.check(level, pos);
                        if (canMutate) {
                            float mod = 0;
                            if (tracker != null && tracker.isResearched(mutation))
                                mod = ServerConfig.SERVER_CONFIG.researchBonus.get();
                            if (rand.nextFloat() < (mutation.getChance() * mutationChanceMod) + mod)
                                possibleMutations.add(mutation);
                        }
                    }
                }
            }

            chromosome_a.setGene(key, geneA);
            chromosome_b.setGene(key, geneB);
        }

        //pick a random possible mutation, and assign it to a chromosome, rolling again to see if we double mutate
        if (!possibleMutations.isEmpty()) {
            Mutation selected = possibleMutations.get(rand.nextInt(possibleMutations.size()));
            boolean doubleMutate = (rand.nextFloat() < (selected.getChance() * mutationChanceMod));
            if (tracker != null)
                tracker.discover(selected);

            if (rand.nextFloat() < 0.5) {
                chromosome_a = selected.getResultSpecies().getDefaultChromosome().copy();
                if (doubleMutate)
                    chromosome_b = chromosome_a.copy();
            } else {
                chromosome_b = selected.getResultSpecies().getDefaultChromosome().copy();
                if (doubleMutate)
                    chromosome_a = chromosome_b.copy();
            }
        }

        //sort genome so that dominant genes are always in a
        for (Map.Entry<ResourceLocation, IGene<?>> entry : chromosome_a.getGenes().entrySet()) {
            IGene<?> gene = entry.getValue();
            if (!entry.getValue().isDominant()) {
                chromosome_a.setGene(entry.getKey(), chromosome_b.getGene(entry.getKey()));
                chromosome_b.setGene(entry.getKey(), gene);
            } else if (chromosome_b.getGene(entry.getKey()).isDominant()) {
                //both are dominant, random shuffle
                if (rand.nextFloat() < 0.5) {
                    chromosome_a.setGene(entry.getKey(), chromosome_b.getGene(entry.getKey()));
                    chromosome_b.setGene(entry.getKey(), gene);
                }
            }
        }

        return new Genome(chromosome_a, chromosome_b);
    }

    /**
     * Gets an offspring from an ItemStack with a genome. If the ItemStack also has a mate set, the offspring is mutated according to mixGenomes with mutation modifiers mutationModifiers.
     *
     * @param bee               an ItemStack to get an offspring from
     * @param resultType        the Item an offspring should be
     * @param level             the level the offspring is generating in (for mutation conditions)
     * @param pos               the blockpos the offspring is generating in (for mutation conditions)
     * @param mutationModifiers modifiers to the mutation chance
     * @return an ItemStack of type resultType with a genome set
     */
    public static ItemStack getOffspring(ItemStack bee, Item resultType, Level level, BlockPos pos, float... mutationModifiers) {
        ItemStack result = new ItemStack(resultType);
        Genome eggs = bee.getOrDefault(EsotericRegistration.BEE, Bee.DEFAULT).mate();

        Genome genome = getGenome(bee);
        Genome mate = new Genome(eggs.primary(), eggs.secondary());
        if (!eggs.equals(new Genome(new Chromosome(), new Chromosome()))) {
            setGenome(result, mixGenomes(genome, mate, level, pos, mutationModifiers));
        } else {
            setGenome(result, genome);
        }
        if (resultType instanceof PrincessItem)
            PrincessItem.setGeneration(result, QueenItem.getGeneration(bee) + 1);
        setSpeciesTag(result);
        return result;
    }

    /**
     * Creates a queen from a princess and drone, setting the mate of the queen to be the drone and ensuring analyzed persistence
     *
     * @param princess
     * @param drone
     * @return
     */
    public static ItemStack createQueenFromPrincessAndDrone(ItemStack princess, ItemStack drone) {
        ItemStack queen = new ItemStack(ItemsRegistration.QUEEN.get());
        GeneticHelper.setGenome(queen, GeneticHelper.getGenome(princess));
        GeneticHelper.setMate(queen, GeneticHelper.getGenome(drone));
        QueenItem.setGeneration(queen, PrincessItem.getGeneration(princess));
        if (princess.has(EsotericRegistration.BEE)) {
            queen.update(EsotericRegistration.BEE, Bee.DEFAULT, bee -> bee.withAnalyzed(princess.get(EsotericRegistration.BEE).analyzed()));
        }
        return queen;
    }

}
