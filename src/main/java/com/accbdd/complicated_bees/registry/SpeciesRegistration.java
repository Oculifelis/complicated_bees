package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.genetics.GeneticHelper;
import com.accbdd.complicated_bees.genetics.Species;
import com.accbdd.complicated_bees.genetics.mutation.Mutation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

//custom registry for species
public class SpeciesRegistration {
    public static final ResourceKey<Registry<Species>> SPECIES_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MODID, "species"));
    private static final Map<ResourceLocation, Integer> complexities = new HashMap<>();

    public static Species getFromResourceLocation(ResourceLocation resourceLocation) {
        return GeneticHelper.getRegistryAccess().registry(SPECIES_REGISTRY_KEY).get().get(resourceLocation);
    }

    public static ResourceLocation getResourceLocation(Species species) {
        return GeneticHelper.getRegistryAccess().registry(SPECIES_REGISTRY_KEY).get().getKey(species);
    }

    public static int getComplexity(Species species) {
        ResourceLocation loc = getResourceLocation(species);
        if (complexities.containsKey(loc))
            return complexities.get(loc);
        return calculateComplexity(loc) ;
    }

    public static int calculateComplexity(ResourceLocation species) {
        if (complexities.containsKey(species))
            return complexities.get(species);
        Set<Mutation> visited = new HashSet<>();
        var registryAccess = GeneticHelper.getRegistryAccess();
        return calculateComplexity(species, visited, registryAccess.registry(MutationRegistration.MUTATION_REGISTRY_KEY).get());
    }

    public static int calculateComplexity(ResourceLocation species, Set<Mutation> visited, Registry<Mutation> mutationRegistry) {
        var x = mutationRegistry.stream().filter(mutation -> mutation.getResult().equals(species) && !visited.contains(mutation)).toList();
        if (x.isEmpty())
            complexities.put(species, 1);
        x.forEach(mutation -> {
            visited.add(mutation);
            complexities.put(species,
                    Math.min(
                            complexities.getOrDefault(species, Integer.MAX_VALUE),
                            Math.max(
                                    calculateComplexity(mutation.getFirst(), visited, mutationRegistry) + 1,
                                    calculateComplexity(mutation.getSecond(), visited, mutationRegistry) + 1
                            )
                    )
            );
        });
        ComplicatedBees.LOGGER.debug("calculated complexity {} for species {}", complexities.get(species), species);
        return complexities.get(species);
    }
}
