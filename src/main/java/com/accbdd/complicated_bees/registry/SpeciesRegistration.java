package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.recipe.mutation.MutationRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

//custom registry for species
public class SpeciesRegistration {
    public static final ResourceKey<Registry<Species>> SPECIES_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "species"));
    private static final Map<ResourceLocation, Integer> complexities = new HashMap<>();

    @Nullable
    public static Species getFromResourceLocation(@Nullable ResourceLocation resourceLocation) {
        return GeneticHelper.getRegistryAccess().registryOrThrow(SPECIES_REGISTRY_KEY).get(resourceLocation);
    }

    public static ResourceLocation getResourceLocation(Species species) {
        if (species == null || species.equals(Species.INVALID))
            return ResourceLocation.fromNamespaceAndPath(MODID, "invalid");
        return GeneticHelper.getRegistryAccess().registryOrThrow(SPECIES_REGISTRY_KEY).getKey(species);
    }

    public static int getComplexity(Species species, Level level) {
        ResourceLocation loc = getResourceLocation(species);
        if (complexities.containsKey(loc))
            return complexities.get(loc);
        return calculateComplexity(loc, level);
    }

    public static int calculateComplexity(ResourceLocation species, Level level) {
        if (complexities.containsKey(species))
            return complexities.get(species);
        Set<MutationRecipe> visited = new HashSet<>();
        return calculateComplexity(species, visited, level);
    }

    public static int calculateComplexity(ResourceLocation species, Set<MutationRecipe> visited, Level level) {
        List<MutationRecipe> x = level.getRecipeManager().getAllRecipesFor(EsotericRegistration.MUTATION_RECIPE.get()).stream().map(RecipeHolder::value).filter(mutation -> mutation.getResult().equals(species) && !visited.contains(mutation)).toList();
        if (x.isEmpty())
            return complexities.getOrDefault(species, 1);
        x.forEach(mutation -> {
            visited.add(mutation);
            complexities.put(species,
                    Math.min(
                            Math.max(
                                    calculateComplexity(mutation.getFirst(), visited, level),
                                    calculateComplexity(mutation.getSecond(), visited, level)
                            ) + 1,
                            complexities.getOrDefault(species, Integer.MAX_VALUE)
                    )
            );
        });
        return complexities.get(species);
    }
}
