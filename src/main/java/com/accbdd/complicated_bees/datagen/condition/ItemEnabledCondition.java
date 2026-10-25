package com.accbdd.complicated_bees.datagen.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.neoforged.neoforge.common.conditions.ICondition;

public class ItemEnabledCondition implements ICondition {
    public static MapCodec<ItemEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(
                            Identifier.CODEC.fieldOf("item").forGetter(ItemEnabledCondition::getItem))
                    .apply(builder, ItemEnabledCondition::new));

    private final Identifier item;

    public ItemEnabledCondition(String location) {
        this(Identifier.tryParse(location));
    }

    public ItemEnabledCondition(String namespace, String path) {
        this(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public ItemEnabledCondition(Identifier item) {
        this.item = item;
    }

    @Override
    public boolean test(IContext context) {
        return BuiltInRegistries.ITEM.getValue(getItem()).isEnabled(FeatureFlagSet.of());
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    public Identifier getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "item_enabled(\"" + item + "\")";
    }
}
