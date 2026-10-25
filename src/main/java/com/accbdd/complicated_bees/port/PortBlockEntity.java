package com.accbdd.complicated_bees.port;

import com.mojang.serialization.*;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.*;

/** Retains the upstream save fields while participating in native Value I/O. */
public abstract class PortBlockEntity extends BlockEntity {
    private static final MapCodec<CompoundTag> ROOT_TAG = new MapCodec<>() {
        @Override public <T> DataResult<CompoundTag> decode(DynamicOps<T> ops, MapLike<T> map) {
            return CompoundTag.CODEC.parse(NbtOps.INSTANCE, ops.convertTo(NbtOps.INSTANCE, ops.createMap(map.entries())));
        }
        @Override public <T> RecordBuilder<T> encode(CompoundTag value, DynamicOps<T> ops, RecordBuilder<T> builder) {
            value.entrySet().forEach(entry -> builder.add(entry.getKey(), NbtOps.INSTANCE.convertTo(ops, entry.getValue())));
            return builder;
        }
        @Override public <T> Stream<T> keys(DynamicOps<T> ops) { return Stream.empty(); }
    };
    protected PortBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) { super(type, pos, state); }
    @Override public void preRemoveSideEffects(BlockPos pos,BlockState state) {
        if(level!=null && state.getBlock() instanceof PortEntityBlock block)block.onRemove(state,level,pos,level.getBlockState(pos),false);
        super.preRemoveSideEffects(pos,state);
    }
    @Override protected final void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        var tag = new CompoundTag();
        saveAdditional(tag, level == null ? net.minecraft.core.RegistryAccess.fromRegistryOfRegistries(net.minecraft.core.registries.BuiltInRegistries.REGISTRY) : level.registryAccess());
        output.store(tag);
    }
    @Override protected final void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        loadAdditional(input.read(ROOT_TAG).orElseGet(CompoundTag::new), input.lookup());
    }
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {}
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {}
}
