package com.accbdd.complicated_bees.bees.gene;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class GeneSpecies extends Gene<Species> {
    public static final String TAG = "species";
    public static final Identifier ID = Identifier.fromNamespaceAndPath(MODID, TAG);

    public GeneSpecies() {
        super(Species.INVALID, true);
    }

    public GeneSpecies(Species species, boolean dominant) {
        super(species, dominant);
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        RegistryAccess registryAccess = GeneticHelper.getRegistryAccess();
        if (this.get() == null) {
            tag.put(DATA, StringTag.valueOf("complicated_bees:invalid"));
        } else if (this.get().builderOverride != null) {
            tag.put(DATA, StringTag.valueOf(this.get().builderOverride.toString()));
        } else {
            if (registryAccess == null || registryAccess.lookupOrThrow(SpeciesRegistration.SPECIES_REGISTRY_KEY).getKey(this.get()) == null) {
                tag.put(DATA, StringTag.valueOf("complicated_bees:invalid"));
            } else {
                tag.put(DATA, StringTag.valueOf(registryAccess.lookupOrThrow(SpeciesRegistration.SPECIES_REGISTRY_KEY).getKey(this.get()).toString()));
            }
        }

        tag.put(DOMINANT, ByteTag.valueOf(this.isDominant()));
        return tag;
    }

    @Override
    public GeneSpecies deserialize(CompoundTag tag) {
        RegistryAccess registryAccess = GeneticHelper.getRegistryAccess();
        if (registryAccess == null)
            return new GeneSpecies();
        Registry<Species> registry = registryAccess.lookupOrThrow(SpeciesRegistration.SPECIES_REGISTRY_KEY);

        return new GeneSpecies(registry.getValue(Identifier.tryParse(tag.getStringOr(DATA, ""))), tag.getBooleanOr(DOMINANT, false));
    }

    @Override
    public MutableComponent getTranslationKey() {
        return Component.translatable("species.complicated_bees." + SpeciesRegistration.getIdentifier(geneData));
    }

    @Override
    public IGene<Species> copy() {
        return new GeneSpecies(this.get(), this.isDominant());
    }
}
