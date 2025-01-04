package com.accbdd.complicated_bees.genetics.tracking;

import com.accbdd.complicated_bees.genetics.GeneticHelper;
import com.accbdd.complicated_bees.genetics.Species;
import com.accbdd.complicated_bees.genetics.mutation.Mutation;
import com.accbdd.complicated_bees.registry.MutationRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class ServerBreedingTracker extends SavedData implements IBreedingTracker {
    public static String UUID_KEY = "uuid";
    public static String SPECIES_KEY = "species";
    public static String MUTATIONS_KEY = "mutations";

    private final UUID playerId;
    protected final Set<ResourceLocation> discoveredSpecies;
    protected final Set<ResourceLocation> discoveredMutations;

    public ServerBreedingTracker(UUID playerId) {
        this.playerId = playerId;
        this.discoveredSpecies = new HashSet<>();
        this.discoveredMutations = new HashSet<>();
    }

    @Override
    public Set<ResourceLocation> getDiscoveredSpecies() {
        return discoveredSpecies;
    }

    @Override
    public Collection<ResourceLocation> getDiscoveredMutations() {
        return discoveredMutations;
    }

    @Override
    public boolean isDiscovered(Species species) {
        return discoveredSpecies.contains(SpeciesRegistration.getResourceLocation(species));
    }

    @Override
    public boolean isDiscovered(Mutation mutation) {
        return discoveredMutations.contains(MutationRegistration.getResourceLocation(mutation));
    }

    @Override
    public void discover(Species species) {
        if (!discoveredSpecies.contains(SpeciesRegistration.getResourceLocation(species))) {
            discoveredSpecies.add(SpeciesRegistration.getResourceLocation(species));
            setDirty();
            //debug messages
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            var playerName = server.getPlayerList().getPlayer(playerId).getName();
            server.getPlayerList().broadcastSystemMessage(
                    MutableComponent.create(playerName.getContents())
                            .append(" has discovered ")
                            .append(GeneticHelper.getTranslationKey(species)),
                    false);
        }
    }

    @Override
    public void discover(Mutation mutation) {
        if (!discoveredMutations.contains(MutationRegistration.getResourceLocation(mutation))) {
            discoveredMutations.add(MutationRegistration.getResourceLocation(mutation));
            setDirty();
            //debug messages
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            var playerName = server.getPlayerList().getPlayer(playerId).getName();
            server.getPlayerList().broadcastSystemMessage(
                    MutableComponent.create(playerName.getContents())
                            .append(" has discovered ")
                            .append(MutationRegistration.getResourceLocation(mutation).toString()),
                    false);
        }
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.putUUID(UUID_KEY, playerId);
        writeListToNBT(pCompoundTag, discoveredSpecies, SPECIES_KEY);
        writeListToNBT(pCompoundTag, discoveredMutations, MUTATIONS_KEY);
        return pCompoundTag;
    }

    public static ServerBreedingTracker load(CompoundTag tag) {
        if (!tag.contains(UUID_KEY))
            throw new NullPointerException("tried to load breeding tracker with no uuid!");
        ServerBreedingTracker tracker = new ServerBreedingTracker(tag.getUUID(UUID_KEY));
        readListFromNBT(tag, str -> tracker.discoveredSpecies.add(ResourceLocation.tryParse(str)), SPECIES_KEY);
        readListFromNBT(tag, str -> tracker.discoveredMutations.add(ResourceLocation.tryParse(str)), MUTATIONS_KEY);
        return tracker;
    }

    /**
     * writes resource locations to a list of strings in the given compound tag
     * @param tag tag to write to
     * @param values values to write
     * @param key key of the values
     */
    private static void writeListToNBT(CompoundTag tag, Iterable<ResourceLocation> values, String key) {
        ListTag listTag = new ListTag();
        for (ResourceLocation value : values) {
            listTag.add(StringTag.valueOf(value.toString()));
        }
        tag.put(key, listTag);
    }

    /**
     * reads strings from a compound tag into a consumer
     * @param tag tag to read from
     * @param consumer consumer of string values
     * @param key key of values to read
     */
    private static void readListFromNBT(CompoundTag tag, Consumer<String> consumer, String key) {
        if (tag.contains(key)) {
            ListTag listTag = tag.getList(key, StringTag.TAG_STRING);
            for (Tag stringTag : listTag) {
                consumer.accept(stringTag.getAsString());
            }
        }
    }

    public static ServerBreedingTracker getTracker(Player player) {
        DimensionDataStorage storage = ServerLifecycleHooks.getCurrentServer().overworld().getDataStorage();
        return storage.computeIfAbsent(ServerBreedingTracker::load, () -> new ServerBreedingTracker(player.getUUID()), "complicated_bees." + player.getStringUUID());
    }
}
