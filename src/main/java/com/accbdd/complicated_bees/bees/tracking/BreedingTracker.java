package com.accbdd.complicated_bees.bees.tracking;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.mutation.Mutation;
import com.accbdd.complicated_bees.client.DiscoverToast;
import com.accbdd.complicated_bees.client.ResearchToast;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.item.BeeItem;
import com.accbdd.complicated_bees.network.PacketHandler;
import com.accbdd.complicated_bees.network.packet.TrackerSyncClientbound;
import com.accbdd.complicated_bees.network.packet.TrackerUpdateClientbound;
import com.accbdd.complicated_bees.registry.MutationRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class BreedingTracker extends SavedData implements IBreedingTracker {
    public static BreedingTracker CLIENT_INSTANCE;

    public static String UUID_KEY = "uuid";
    public static String SPECIES_KEY = "species";
    public static String MUTATIONS_KEY = "mutations";
    public static String RESEARCH_KEY = "researched";

    private final UUID playerId;
    protected final Set<ResourceLocation> discoveredSpecies;
    protected final Set<ResourceLocation> discoveredMutations;
    protected final Set<ResourceLocation> researchedMutations;

    public BreedingTracker(UUID playerId) {
        this.playerId = playerId;
        this.discoveredSpecies = new HashSet<>();
        this.discoveredMutations = new HashSet<>();
        this.researchedMutations = new HashSet<>();
    }

    @Override
    public UUID getUUID() {
        return this.playerId;
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
    public Set<ResourceLocation> getResearchedMutations() {
        return researchedMutations;
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
    public boolean isResearched(Mutation mutation) {
        return researchedMutations.contains(MutationRegistration.getResourceLocation(mutation));
    }

    public void discoverIndividual(ItemStack stack) {
        if (stack.is(ItemTagGenerator.BEE)) {
            discover(GeneticHelper.getSpecies(stack, true));
            if (stack.getOrCreateTag().getBoolean(BeeItem.ANALYZED_TAG)) {
                discover(GeneticHelper.getSpecies(stack, false));
            }
        }
    }

    @Override
    public void discover(Species species) {
        if (!isDiscovered(species)) {
            ResourceLocation loc = SpeciesRegistration.getResourceLocation(species);
            discoveredSpecies.add(loc);
            setDirty();
            sendUpdateToPlayer(TrackerUpdateClientbound.UpdateType.SPECIES, loc);
        }
    }

    @Override
    public void discover(Mutation mutation) {
        if (!isDiscovered(mutation)) {
            ResourceLocation loc = MutationRegistration.getResourceLocation(mutation);
            discoveredMutations.add(loc);
            setDirty();
            sendUpdateToPlayer(TrackerUpdateClientbound.UpdateType.MUTATION, loc);
        }
    }

    @Override
    public void research(Mutation mutation) {
        if (!isResearched(mutation)) {
            ResourceLocation loc = MutationRegistration.getResourceLocation(mutation);
            researchedMutations.add(loc);
            setDirty();
            sendUpdateToPlayer(TrackerUpdateClientbound.UpdateType.RESEARCH, loc);
        }
    }

    public void clearSpecies() {
        discoveredSpecies.clear();
        setDirty();
        syncToPlayer();
    }

    public void clearMutations() {
        discoveredMutations.clear();
        setDirty();
        syncToPlayer();
    }

    public void clearResearch() {
        researchedMutations.clear();
        setDirty();
        syncToPlayer();
    }

    public void syncToPlayer() {
        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(getUUID())),
                new TrackerSyncClientbound(this));
    }

    public void sendUpdateToPlayer(TrackerUpdateClientbound.UpdateType type, ResourceLocation loc) {
        if (ServerLifecycleHooks.getCurrentServer() != null && ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(getUUID()) != null)
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(getUUID())), new TrackerUpdateClientbound(type, loc));
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        pCompoundTag.putUUID(UUID_KEY, playerId);
        writeListToNBT(pCompoundTag, discoveredSpecies, SPECIES_KEY);
        writeListToNBT(pCompoundTag, discoveredMutations, MUTATIONS_KEY);
        writeListToNBT(pCompoundTag, researchedMutations, RESEARCH_KEY);
        return pCompoundTag;
    }

    public static BreedingTracker load(CompoundTag tag) {
        if (!tag.contains(UUID_KEY))
            throw new NullPointerException("tried to load breeding tracker with no uuid!");
        BreedingTracker tracker = new BreedingTracker(tag.getUUID(UUID_KEY));
        readListFromNBT(tag, str -> tracker.discoveredSpecies.add(ResourceLocation.tryParse(str)), SPECIES_KEY);
        readListFromNBT(tag, str -> tracker.discoveredMutations.add(ResourceLocation.tryParse(str)), MUTATIONS_KEY);
        readListFromNBT(tag, str -> tracker.researchedMutations.add(ResourceLocation.tryParse(str)), RESEARCH_KEY);
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

    public static BreedingTracker getTracker(Player player) {
        return getTracker(player.getUUID());
    }

    public static BreedingTracker getTracker(UUID uuid) {
        if (ServerLifecycleHooks.getCurrentServer() == null)
            return CLIENT_INSTANCE;
        DimensionDataStorage storage = ServerLifecycleHooks.getCurrentServer().overworld().getDataStorage();
        return storage.computeIfAbsent(BreedingTracker::load, () -> new BreedingTracker(uuid), "complicated_bees." + uuid.toString());
    }

    @OnlyIn(Dist.CLIENT)
    public static void updateFromPacket(TrackerUpdateClientbound packet) {
        switch (packet.type()) {
            case SPECIES -> {
                CLIENT_INSTANCE.discoveredSpecies.add(packet.loc());
                Minecraft.getInstance().getToasts().addToast(new DiscoverToast(SpeciesRegistration.getFromResourceLocation(packet.loc())));
            }
            case MUTATION -> CLIENT_INSTANCE.discoveredMutations.add(packet.loc());
            case RESEARCH -> {
                CLIENT_INSTANCE.researchedMutations.add(packet.loc());
                Minecraft.getInstance().getToasts().addToast(new ResearchToast(MutationRegistration.getFromResourceLocation(packet.loc())));
            }
        }
    }

}
