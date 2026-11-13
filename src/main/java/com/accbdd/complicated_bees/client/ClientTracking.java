package com.accbdd.complicated_bees.client;
import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.network.packet.TrackerUpdateClientbound;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import com.accbdd.complicated_bees.recipe.mutation.MutationRecipe;
import net.minecraft.client.Minecraft;
public final class ClientTracking {
    public static void updateFromPacket(TrackerUpdateClientbound packet) {
        switch (packet.updateType()) {
            case SPECIES -> {
                BreedingTracker.CLIENT_INSTANCE.getDiscoveredSpecies().add(packet.loc());
                com.accbdd.complicated_bees.port.TargetClient.toast(new DiscoverToast(SpeciesRegistration.getFromIdentifier(packet.loc())));
                BreedingTracker.fireClientListeners();
            }
            case MUTATION -> BreedingTracker.CLIENT_INSTANCE.getDiscoveredMutations().add(packet.loc());
            case RESEARCH -> {
                BreedingTracker.CLIENT_INSTANCE.getResearchedMutations().add(packet.loc());
                com.accbdd.complicated_bees.port.PortRecipes.manager(Minecraft.getInstance().level).byKey(packet.loc()).ifPresent(holder -> {
                    if (holder.value() instanceof MutationRecipe mutation) {
                        com.accbdd.complicated_bees.port.TargetClient.toast(new ResearchToast(mutation));
                    }
                });
            }
        }
    }

}
