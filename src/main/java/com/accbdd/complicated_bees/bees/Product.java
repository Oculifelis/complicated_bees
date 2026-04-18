package com.accbdd.complicated_bees.bees;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.RecordBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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
    public static final StreamCodec<RegistryFriendlyByteBuf, Product> STREAM_CODEC = StreamCodec.of((buf, val) -> val.toNetwork(buf), Product::fromNetwork);

    public static final List<Product> EMPTY = List.of(new Product(Items.AIR.getDefaultInstance(), 0));
    public static final Random rand = new Random();

    private final Item item;
    private final int count;
    private final DataComponentPatch components;
    private final TagKey<Item> tag;
    private final float chance;

    private ItemStack cachedStack;

    public Product(Item item, int count, @Nullable DataComponentPatch components, @Nullable TagKey<Item> tag, float chance) {
        this.item = item;
        this.count = count;
        this.components = components;
        this.tag = tag;
        this.chance = chance;
    }

    public Product(ItemStack stack, float chance) {
        this(stack.getItem(), stack.getCount(), stack.getComponentsPatch(), null, chance);
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
		cachedStack = new ItemStack(resolvedItem.builtInRegistryHolder(), count, components);
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

    public void toNetwork(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(isTagProduct());
        buf.writeInt(count);
        buf.writeFloat(chance);
        DataComponentPatch.STREAM_CODEC.encode(buf, components);

        if (isTagProduct()) {
            buf.writeResourceLocation(tag.location());
        } else {
            ByteBufCodecs.registry(Registries.ITEM).encode(buf, item);
        }
    }

    public static Product fromNetwork(RegistryFriendlyByteBuf buf) {
        boolean isTag = buf.readBoolean();
        int count = buf.readInt();
        float chance = buf.readFloat();
        DataComponentPatch components = DataComponentPatch.STREAM_CODEC.decode(buf);

        if (isTag) {
            ResourceLocation tagId = buf.readResourceLocation();
            TagKey<Item> tag = TagKey.create(BuiltInRegistries.ITEM.key(), tagId);
            return new Product(Items.AIR, count, components, tag, chance);
        } else {
            Item item = ByteBufCodecs.registry(Registries.ITEM).decode(buf);
            ItemStack stack = new ItemStack(item.builtInRegistryHolder(), count, components);
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

        if (components != null && !components.isEmpty())
            builder.add("components", DataComponentPatch.CODEC.encodeStart(ops, components).result().orElseThrow());
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
        DataComponentPatch components = ops.get(input, "components").flatMap(data -> DataComponentPatch.CODEC.parse(ops, data)).result().orElse(DataComponentPatch.EMPTY);

        if (item.isEmpty() && tag.isEmpty()) {
            return DataResult.error(() -> "Expected either 'item' or 'tag' field");
        }
        if (item.isPresent()) {
            return BuiltInRegistries.ITEM.byNameCodec().parse(ops, item.get()).map(i -> {
                ItemStack stack = new ItemStack(i.builtInRegistryHolder(), count, components);
                return Pair.of(new Product(stack, chance), input);
            });
        }

        //wow this is messy
        return ResourceLocation.CODEC.xmap(
                loc -> TagKey.create(BuiltInRegistries.ITEM.key(), loc),
                TagKey::location
        ).parse(ops, tag.get()).flatMap(tagKey -> DataResult.success(Pair.of(new Product(Items.AIR, count, components, tagKey, chance), input)));
    }
}
