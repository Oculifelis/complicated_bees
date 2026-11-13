package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.recipe.mutation.MutationRecipe;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Keeps species marked "secret" out of JEI until the player discovers them: their bees, their
 * produce page and the mutations that make them. Re-checked whenever the client's discoveries change.
 */
final class SecretSpeciesJEI {
    private static IJeiRuntime runtime;
    private static final Set<Species> HIDDEN = new HashSet<>();
    private static boolean listening;

    static void start(IJeiRuntime jei) {
        runtime = jei;
        HIDDEN.clear();
        if (!listening) {
            listening = true;
            BreedingTracker.CLIENT_LISTENERS.add(SecretSpeciesJEI::refresh);
        }
        refresh();
    }

    static void stop() {
        runtime = null;
        HIDDEN.clear();
    }

    static synchronized void refresh() {
        IJeiRuntime jei = runtime;
        if (jei == null || Minecraft.getInstance().level == null) return;
        BreedingTracker tracker = BreedingTracker.CLIENT_INSTANCE;
        List<RecipeHolder<MutationRecipe>> mutations = com.accbdd.complicated_bees.port.PortRecipes
                .manager(Minecraft.getInstance().level).getAllRecipesFor(EsotericRegistration.MUTATION_RECIPE.get()).stream().toList();
        for (Species species : GeneticHelper.getRegistryAccess().lookupOrThrow(SpeciesRegistration.SPECIES_REGISTRY_KEY).stream().toList()) {
            if (!species.isSecret()) continue;
            boolean known = tracker != null && tracker.isDiscovered(species);
            boolean hidden = HIDDEN.contains(species);
            if (known == !hidden) continue;
            List<RecipeHolder<MutationRecipe>> makers = new ArrayList<>();
            for (RecipeHolder<MutationRecipe> m : mutations) if (m.value().getResultSpecies() == species) makers.add(m);
            if (known) {
                jei.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, species.toMembers());
                jei.getRecipeManager().unhideRecipes(BeeProduceRecipeCategory.TYPE, List.of(species));
                jei.getRecipeManager().unhideRecipes(MutationRecipeCategory.TYPE, makers);
                HIDDEN.remove(species);
            } else {
                jei.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, species.toMembers());
                jei.getRecipeManager().hideRecipes(BeeProduceRecipeCategory.TYPE, List.of(species));
                jei.getRecipeManager().hideRecipes(MutationRecipeCategory.TYPE, makers);
                HIDDEN.add(species);
            }
        }
    }

    private SecretSpeciesJEI() {}
}
