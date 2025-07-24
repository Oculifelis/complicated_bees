package com.accbdd.complicated_bees.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public sealed interface ItemOrTag permits ItemOrTag.ItemWrapper, ItemOrTag.TagWrapper {
    record ItemWrapper(Item item) implements ItemOrTag {}
    record TagWrapper(TagKey<Item> tag) implements ItemOrTag {}
}
