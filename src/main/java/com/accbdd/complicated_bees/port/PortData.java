package com.accbdd.complicated_bees.port;
import java.util.function.Consumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.entity.BlockEntityType;
public final class PortData {
    public static CompoundTag blockTag(ItemStack stack){
        var data=stack.get(DataComponents.BLOCK_ENTITY_DATA);
        return data==null?new CompoundTag():data.copyTagWithoutId();
    }
    public static void updateBlock(ItemStack stack,BlockEntityType<?> type,Consumer<CompoundTag> update){
        var tag=blockTag(stack);update.accept(tag);
        stack.set(DataComponents.BLOCK_ENTITY_DATA,TypedEntityData.of(type,tag));
    }
    public static Tag saveStack(ItemStack stack,HolderLookup.Provider registries){return ItemStack.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE),stack).getOrThrow();}
    public static ItemStack loadStack(HolderLookup.Provider registries,CompoundTag tag){return ItemStack.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE),tag).result().orElse(ItemStack.EMPTY);}
}
