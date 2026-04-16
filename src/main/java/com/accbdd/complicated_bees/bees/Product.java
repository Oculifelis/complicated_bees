package com.accbdd.complicated_bees.bees;

import com.accbdd.complicated_bees.ComplicatedBees;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Product {

    public static final Codec<Product> CODEC = Codec.of(Product::encode, Product::decode);

    public static final List<Product> EMPTY = List.of(new Product(Items.AIR.getDefaultInstance(), 0));
    public static final Random rand = new Random();

    private final Item item;
    private final int count;
    private final CompoundTag nbt;
    private final TagKey<Item> tag;
    private final float chance;

    private ItemStack cachedStack;

    public Product(Item item, int count, @Nullable CompoundTag nbt, @Nullable TagKey<Item> tag, float chance) {
        this.item = item;
        this.count = count;
        this.nbt = nbt;
        this.tag = tag;
        this.chance = chance;
    }

    public Product(ItemStack stack, float chance) {
        this(stack.getItem(), stack.getCount(), stack.getTag(), null, chance);
    }

    public boolean isTagProduct() {
        return this.tag != null;
    }

    public float getChance() {
        return chance;
    }

    public ItemStack getStack() {
        if (cachedStack != null) return cachedStack.copy();

        Item resolvedItem;
        if (isTagProduct()) {
            Optional<Item> firstItem = BuiltInRegistries.ITEM.getTag(tag).flatMap(set -> set.stream()
                    .map(Holder::value).min(Comparator.comparing(i -> BuiltInRegistries.ITEM.getKey(i).toString())));

            if (firstItem.isEmpty()) {
                ComplicatedBees.LOGGER.error("tag " + tag.location() + " is empty or invalid");
                cachedStack = ItemStack.EMPTY;
                return cachedStack.copy();
            }
            resolvedItem = firstItem.get();
        } else {
            resolvedItem = item;
        }
        ItemStack stack = new ItemStack(resolvedItem, count);
        if (nbt != null)
            stack.setTag(nbt.copy());
        cachedStack = stack;
        return cachedStack.copy();
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
        buf.writeBoolean(isTagProduct());
        buf.writeInt(count);
        buf.writeFloat(chance);
        buf.writeNbt(nbt);

        if (isTagProduct()) {
            buf.writeResourceLocation(tag.location());
        } else {
            buf.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, item);
        }
    }

    public static Product fromNetwork(FriendlyByteBuf buf) {
        boolean isTag = buf.readBoolean();
        int count = buf.readInt();
        float chance = buf.readFloat();
        CompoundTag nbt = buf.readNbt();

        if (isTag) {
            ResourceLocation tagId = buf.readResourceLocation();
            TagKey<Item> tag = TagKey.create(BuiltInRegistries.ITEM.key(), tagId);
            return new Product(Items.AIR, count, nbt, tag, chance);
        } else {
            Item item = buf.readRegistryIdUnsafe(ForgeRegistries.ITEMS);
            ItemStack stack = new ItemStack(item, count);
            if (nbt != null) {
                stack.setTag(nbt);
            }
            return new Product(stack, chance);
        }
    }

    private <T> DataResult<T> encode(DynamicOps<T> ops, T prefix) {
        RecordBuilder<T> builder = ops.mapBuilder();

        if (isTagProduct()) {
            builder.add("tag", ResourceLocation.CODEC.encodeStart(ops, tag.location()).result().orElse(ops.empty()));
        } else {
            builder.add("item", BuiltInRegistries.ITEM.byNameCodec().encodeStart(ops, item));
        }

        if (nbt != null && !nbt.isEmpty())
            builder.add("nbt", CompoundTag.CODEC.encodeStart(ops, nbt).result().orElseThrow());
        if (count > 1)
            builder.add("count", ops.createInt(count));
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
        ).parse(ops, tag.get()).flatMap(tagKey -> {
            CompoundTag copyNbt = nbt.isEmpty() ? null : nbt.copy();
            return DataResult.success(Pair.of(new Product(Items.AIR, count, copyNbt, tagKey, chance), input));
        });
    }
}
