package com.accbdd.complicated_bees.block.entity;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.registry.BlockEntitiesRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BeeNestBlockEntity extends BlockEntity {
    private Species species;

    public BeeNestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegistration.BEE_NEST_ENTITY.get(), pPos, pBlockState);
    }

    public static int getNestColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int index) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof BeeNestBlockEntity) {
            return ((BeeNestBlockEntity) be).getSpecies().getNestColor();
        }
        return 0xFFFFFF;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        RegistryAccess registryAccess = GeneticHelper.getRegistryAccess();
        if (registryAccess.registryOrThrow(SpeciesRegistration.SPECIES_REGISTRY_KEY).getKey(getSpecies()) == null) {
            tag.put("species", StringTag.valueOf("complicated_bees:invalid"));
        } else {
            tag.put("species", StringTag.valueOf(registryAccess.registryOrThrow(SpeciesRegistration.SPECIES_REGISTRY_KEY).getKey(getSpecies()).toString()));
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.species = SpeciesRegistration.getFromResourceLocation(ResourceLocation.tryParse(tag.getString("species")));
    }

    public Species getSpecies() {
        if (species == null) {
            species = Species.INVALID;
        }
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
        loadAdditional(pkt.getTag(), lookupProvider);
    }
}
