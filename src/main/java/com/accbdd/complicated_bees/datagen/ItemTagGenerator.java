package com.accbdd.complicated_bees.datagen;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import static com.accbdd.complicated_bees.ComplicatedBees.MODID;
public final class ItemTagGenerator {
    public static final TagKey<Item> SCOOP_TOOL = ItemTags.create(Identifier.fromNamespaceAndPath(MODID, "scoop_tool"));
    public static final TagKey<Item> BEE = ItemTags.create(Identifier.fromNamespaceAndPath(MODID, "bee"));
    public static final TagKey<Item> ROYAL = ItemTags.create(Identifier.fromNamespaceAndPath(MODID, "royal"));
    public static final TagKey<Item> FRAME = ItemTags.create(Identifier.fromNamespaceAndPath(MODID, "frame"));
    public static final TagKey<Item> ANALYZER_FUEL = ItemTags.create(Identifier.fromNamespaceAndPath(MODID, "analyzer_fuel"));
    public static final TagKey<Item> RESEARCH_MATERIAL = ItemTags.create(Identifier.fromNamespaceAndPath(MODID, "research_material"));
    public static final TagKey<Item> COMB = ItemTags.create(Identifier.fromNamespaceAndPath(MODID, "comb"));
}
