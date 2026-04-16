package com.accbdd.complicated_bees.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ComplicatedBeesCodecs {
    //hex string parser (no alpha)
    public static final Codec<Integer> HEX_STRING = Codec.STRING.comapFlatMap(
            str -> {
                try {
                    return DataResult.success(Integer.valueOf(str, 16));
                } catch (NumberFormatException e) {
                    return DataResult.error(() -> str + " is not hexadecimal.");
                }
            },
            Integer::toHexString
    );

    //item from tag
    public static final Codec<Item> ITEM_FROM_TAG = TagKey.codec(BuiltInRegistries.ITEM.key()).flatComapMap(
            tag -> BuiltInRegistries.ITEM.getTag(tag).get().stream().findFirst().orElse(Holder.direct(Items.AIR)).value(),
            item -> DataResult.error(() -> "tried to get tag from item"));

}
