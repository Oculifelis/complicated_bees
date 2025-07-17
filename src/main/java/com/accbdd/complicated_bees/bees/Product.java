package com.accbdd.complicated_bees.bees;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.RecordBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Product {

    public static final Codec<Product> CODEC = Codec.of(Product::encode, Product::decode);

    public static final List<Product> EMPTY = List.of(new Product(Items.AIR.getDefaultInstance(), 0));
    public static final Random rand = new Random();

    private final ItemStack stack;
    private final float chance;

    public Product(ItemStack stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }

    public float getChance() {
        return chance;
    }

    public ItemStack getStack() {
        return stack.copy();
    }

    public ItemStack getStackResult(float... modifiers) {
        float stackChance = this.getChance();
        for (float modifier : modifiers) {
            stackChance *= modifier;
        }
        ItemStack stack;
        if (stackChance > 1) {
            stack = this.getStack();
            stack.setCount((int)stackChance * stack.getCount());
            stack.grow(rand.nextFloat() < (stackChance - (int) stackChance) ? this.getStack().getCount() : 0);
        } else {
            stack = rand.nextFloat() < stackChance ? this.getStack() : ItemStack.EMPTY;
        }

        return stack;
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(chance);
        buf.writeItem(stack);
    }

    public static Product fromNetwork(FriendlyByteBuf buf) {
        float chance = buf.readFloat();
        ItemStack item = buf.readItem();
        return new Product(item, chance);
    }

    private <T> DataResult<T> encode(DynamicOps<T> ops, T prefix) {
        RecordBuilder<T> builder = ops.mapBuilder();

        builder.add("item", BuiltInRegistries.ITEM.byNameCodec().encodeStart(ops, stack.getItem()));

        if (stack.hasTag() && !stack.getTag().isEmpty())
            builder.add("nbt", CompoundTag.CODEC.encodeStart(ops, stack.getTag()).result().orElseThrow());
        if (stack.getCount() > 1)
            builder.add("count", ops.createInt(stack.getCount()));
        if (chance != 1.0f)
            builder.add("chance", ops.createFloat(chance));

        return builder.build(prefix);
    }

    private static <T> DataResult<Pair<Product, T>> decode(DynamicOps<T> ops, T input) {
        Optional<T> item = ops.get(input, "item").result();
        Optional<T> tag = ops.get(input, "tag").result();
        int count = ops.get(input, "count").flatMap(data -> Codec.INT.parse(ops, data)).result().orElse(1);
        float chance = ops.get(input, "chance").flatMap(data -> Codec.FLOAT.parse(ops, data)).result().orElse(1.0f);
        CompoundTag nbt = ops.get(input, "nbt").flatMap(data -> CompoundTag.CODEC.parse(ops, data)).result().orElse(new CompoundTag());

        if (item.isEmpty() && tag.isEmpty()) {
            return DataResult.error(() -> "Expected either 'item' or 'tag' field");
        }
        if (item.isPresent()) {
            return BuiltInRegistries.ITEM.byNameCodec().parse(ops, item.get()).map(i -> {
                ItemStack stack = new ItemStack(i, count);
                if (!nbt.isEmpty())
                    stack.setTag(nbt);
                return Pair.of(new Product(stack, chance), input);
            });
        }

        //wow this is messy
        return ResourceLocation.CODEC.xmap(
                loc -> TagKey.create(BuiltInRegistries.ITEM.key(), loc),
                TagKey::location
        ).parse(ops, tag.get()).map(tagKey -> {
            ItemStack itemStack = BuiltInRegistries.ITEM.getTag(tagKey)
                    .flatMap(t -> t.stream().findFirst())
                    .map(Holder::value).map(i -> new ItemStack(i, count)).orElse(ItemStack.EMPTY);
            if (!nbt.isEmpty())
                itemStack.setTag(nbt);
            return Pair.of(new Product(itemStack, chance), input);
        });
    }
}
