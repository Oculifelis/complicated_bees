package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.mutation.Mutation;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

//custom registry for species
public class SpeciesRegistration {
    public static final ResourceKey<Registry<Species>> SPECIES_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MODID, "species"));
    private static final Map<ResourceLocation, Integer> complexities = new HashMap<>();

    public static Species getFromResourceLocation(ResourceLocation resourceLocation) {
        return GeneticHelper.getRegistryAccess().registry(SPECIES_REGISTRY_KEY).get().get(resourceLocation);
    }

    public static ResourceLocation getResourceLocation(Species species) {
        if (species.equals(Species.INVALID))
            return new ResourceLocation("complicated_bees:invalid");
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
        RegistryAccess registryAccess = GeneticHelper.getRegistryAccess();
        return calculateComplexity(species, visited, registryAccess.registry(MutationRegistration.MUTATION_REGISTRY_KEY).get());
    }

    public static int calculateComplexity(ResourceLocation species, Set<Mutation> visited, Registry<Mutation> mutationRegistry) {
        List<Mutation> x = mutationRegistry.stream().filter(mutation -> mutation.getResult().equals(species) && !visited.contains(mutation)).toList();
        if (x.isEmpty())
            return complexities.getOrDefault(species, 1);
        x.forEach(mutation -> {
            visited.add(mutation);
            complexities.put(species,
                    Math.min(
                            Math.max(
                                    calculateComplexity(mutation.getFirst(), visited, mutationRegistry),
                                    calculateComplexity(mutation.getSecond(), visited, mutationRegistry)
                            ) + 1,
                            complexities.getOrDefault(species, Integer.MAX_VALUE)
                    )
            );
        });
        return complexities.get(species);
    }
}
