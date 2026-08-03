package com.accbdd.complicated_bees.bees.tracking;

import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.recipe.mutation.MutationRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collection;
import java.util.UUID;

public interface IBreedingTracker {

    /**
     * @return the UUID of the player this tracker is for
     */
    UUID getUUID();

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
     * @param mutation the mutation recipe to query
     * @return whether the player has discovered this mutation
     */
    boolean isDiscovered(RecipeHolder<MutationRecipe> mutation);

    /**
     * @param mutation the mutation recipe to query
     * @return whether the player has researched this mutation
     */
    boolean isResearched(RecipeHolder<MutationRecipe> mutation);

    /**
     * @param species the species to mark as discovered
     */
    void discover(Species species);

    /**
     * @param mutation the mutation to mark as discovered
     */
    void discover(RecipeHolder<MutationRecipe> mutation);

    /**
     * @param mutation the mutation to mark as researched
     */
    void research(RecipeHolder<MutationRecipe> mutation);
}
