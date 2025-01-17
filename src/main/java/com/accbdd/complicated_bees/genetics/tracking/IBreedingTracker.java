package com.accbdd.complicated_bees.genetics.tracking;

import com.accbdd.complicated_bees.genetics.Species;
import com.accbdd.complicated_bees.genetics.mutation.Mutation;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

public interface IBreedingTracker {
    /**
     * @return a collection of discovered species
     */
    Collection<ResourceLocation> getDiscoveredSpecies();

    /**
     * @return a collection of discovered mutations
     */
    Collection<ResourceLocation> getDiscoveredMutations();

    /**
     * @return a collection of researched mutations
     */
    Collection<ResourceLocation> getResearchedMutations();

    /**
     * @param species the species to query
     * @return whether the player has discovered this species
     */
    boolean isDiscovered(Species species);

    /**
     * @param mutation the mutation to query
     * @return whether the player has discovered this mutation
     */
    boolean isDiscovered(Mutation mutation);

    /**
     * @param mutation the mutation to query
     * @return whether the player has researched this mutation
     */
    boolean isResearched(Mutation mutation);

    /**
     * @param species the species to mark as discovered
     */
    void discover(Species species);

    /**
     * @param mutation the mutation to mark as discovered
     */
    void discover(Mutation mutation);

    /**
     * @param mutation the mutation to mark as researched
     */
    void research(Mutation mutation);
}
