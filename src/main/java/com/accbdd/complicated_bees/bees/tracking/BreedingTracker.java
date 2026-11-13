package com.accbdd.complicated_bees.bees.tracking;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.component.Bee;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.network.packet.TrackerSyncClientbound;
import com.accbdd.complicated_bees.network.packet.TrackerUpdateClientbound;
import com.accbdd.complicated_bees.recipe.mutation.MutationRecipe;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class BreedingTracker extends SavedData implements IBreedingTracker {
    public static BreedingTracker CLIENT_INSTANCE;
    /** Run on the client whenever the discovered species change (sync or a new discovery). */
    public static final java.util.List<Runnable> CLIENT_LISTENERS = new java.util.concurrent.CopyOnWriteArrayList<>();

    public static void fireClientListeners() {
        for (Runnable r : CLIENT_LISTENERS) r.run();
    }

    public static String UUID_KEY = "uuid";
    public static String SPECIES_KEY = "species";
    public static String MUTATIONS_KEY = "mutations";
    public static String RESEARCH_KEY = "researched";

    private final UUID playerId;
    protected final Set<Identifier> discoveredSpecies;
    protected final Set<Identifier> discoveredMutations;
    protected final Set<Identifier> researchedMutations;

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
    public Set<Identifier> getDiscoveredSpecies() {
        return discoveredSpecies;
    }

    @Override
    public Collection<Identifier> getDiscoveredMutations() {
        return discoveredMutations;
    }

    @Override
    public Set<Identifier> getResearchedMutations() {
        return researchedMutations;
    }

    @Override
    public boolean isDiscovered(Species species) {
        return discoveredSpecies.contains(SpeciesRegistration.getIdentifier(species));
    }

    @Override
    public boolean isDiscovered(RecipeHolder<MutationRecipe> mutation) {
        return discoveredMutations.contains(mutation.id().identifier());
    }

    @Override
    public boolean isResearched(RecipeHolder<MutationRecipe> mutation) {
        return researchedMutations.contains(mutation.id().identifier());
    }

    public void discoverIndividual(ItemStack stack) {
        if (stack.is(ItemTagGenerator.BEE)) {
            discover(GeneticHelper.getSpecies(stack, true));
            if (stack.getOrDefault(EsotericRegistration.BEE, Bee.DEFAULT).analyzed()) {
                discover(GeneticHelper.getSpecies(stack, false));
            }
        }
    }

    @Override
    public void discover(Species species) {
        if (!isDiscovered(species)) {
            Identifier loc = SpeciesRegistration.getIdentifier(species);
            discoveredSpecies.add(loc);
            setDirty();
            sendUpdateToPlayer(TrackerUpdateClientbound.UpdateType.SPECIES, loc);
        }
    }

    @Override
    public void discover(RecipeHolder<MutationRecipe> mutation) {
        if (!isDiscovered(mutation)) {
            Identifier loc = mutation.id().identifier();
            discoveredMutations.add(loc);
            setDirty();
            sendUpdateToPlayer(TrackerUpdateClientbound.UpdateType.MUTATION, loc);
        }
    }

    @Override
    public void research(RecipeHolder<MutationRecipe> mutation) {
        if (!isResearched(mutation)) {
            Identifier loc = mutation.id().identifier();
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
        PacketDistributor.sendToPlayer(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(getUUID()), new TrackerSyncClientbound(this));
    }

    public void sendUpdateToPlayer(TrackerUpdateClientbound.UpdateType type, Identifier loc) {
        if (ServerLifecycleHooks.getCurrentServer() != null && ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(getUUID()) != null)
            PacketDistributor.sendToPlayer(ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(getUUID()), new TrackerUpdateClientbound(type, loc));
    }

    public CompoundTag save(CompoundTag pCompoundTag, HolderLookup.Provider registries) {
        pCompoundTag.store(UUID_KEY, net.minecraft.core.UUIDUtil.CODEC, playerId);
        writeListToNBT(pCompoundTag, discoveredSpecies, SPECIES_KEY);
        writeListToNBT(pCompoundTag, discoveredMutations, MUTATIONS_KEY);
        writeListToNBT(pCompoundTag, researchedMutations, RESEARCH_KEY);
        return pCompoundTag;
    }

    public static BreedingTracker load(CompoundTag tag, HolderLookup.Provider registries) {
        if (!tag.contains(UUID_KEY))
            throw new NullPointerException("tried to load breeding tracker with no uuid!");
        BreedingTracker tracker = new BreedingTracker(tag.read(UUID_KEY, net.minecraft.core.UUIDUtil.CODEC).orElse(null));
        readListFromNBT(tag, str -> tracker.discoveredSpecies.add(Identifier.tryParse(str)), SPECIES_KEY);
        readListFromNBT(tag, str -> tracker.discoveredMutations.add(Identifier.tryParse(str)), MUTATIONS_KEY);
        readListFromNBT(tag, str -> tracker.researchedMutations.add(Identifier.tryParse(str)), RESEARCH_KEY);
        return tracker;
    }

    /**
     * writes resource locations to a list of strings in the given compound tag
     * @param tag tag to write to
     * @param values values to write
     * @param key key of the values
     */
    private static void writeListToNBT(CompoundTag tag, Iterable<Identifier> values, String key) {
        ListTag listTag = new ListTag();
        for (Identifier value : values) {
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
            ListTag listTag = tag.getListOrEmpty(key);
            for (Tag stringTag : listTag) {
                consumer.accept(stringTag.asString().orElse(""));
            }
        }
    }

    public static BreedingTracker getTracker(Player player) {
        return getTracker(player.getUUID());
    }

    public static BreedingTracker getTracker(UUID uuid) {
        if (ServerLifecycleHooks.getCurrentServer() == null)
            return CLIENT_INSTANCE;
        SavedDataStorage storage = ServerLifecycleHooks.getCurrentServer().overworld().getDataStorage();
        return storage.computeIfAbsent(new net.minecraft.world.level.saveddata.SavedDataType<>(Identifier.fromNamespaceAndPath("complicated_bees",uuid.toString()), () -> new BreedingTracker(uuid), CompoundTag.CODEC.xmap(tag -> load(tag,null), tracker -> tracker.save(new CompoundTag(),null))));
    }
}
