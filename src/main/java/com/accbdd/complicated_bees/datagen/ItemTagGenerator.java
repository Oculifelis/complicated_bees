package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class ItemTagGenerator extends ItemTagsProvider {
    public static final TagKey<Item> SCOOP_TOOL = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "scoop_tool"));
    public static final TagKey<Item> BEE = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "bee"));
    public static final TagKey<Item> ROYAL = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "royal"));
    public static final TagKey<Item> FRAME = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "frame"));
    public static final TagKey<Item> ANALYZER_FUEL = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "analyzer_fuel"));
    public static final TagKey<Item> RESEARCH_MATERIAL = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "research_material"));
    public static final TagKey<Item> COMB = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "comb"));

    public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(SCOOP_TOOL).add(ItemsRegistration.SCOOP.get());
        tag(ROYAL).add(ItemsRegistration.PRINCESS.get(), ItemsRegistration.QUEEN.get());
        tag(BEE).add(ItemsRegistration.PRINCESS.get(), ItemsRegistration.QUEEN.get(), ItemsRegistration.DRONE.get());
        tag(ANALYZER_FUEL).add(ItemsRegistration.HONEY_DROPLET.get(), ItemsRegistration.ROYAL_JELLY.get());
        tag(COMB).add(ItemsRegistration.COMB.get(), Items.HONEYCOMB);
        tag(FRAME).add(
                ItemsRegistration.FRAME.get(),
                ItemsRegistration.DEADLY_FRAME.get(),
                ItemsRegistration.DRY_FRAME.get(),
                ItemsRegistration.WET_FRAME.get(),
                ItemsRegistration.COLD_FRAME.get(),
                ItemsRegistration.HOT_FRAME.get(),
                ItemsRegistration.RESTRICTIVE_FRAME.get(),
                ItemsRegistration.WAXED_FRAME.get(),
                ItemsRegistration.HONEYED_FRAME.get(),
                ItemsRegistration.TWISTING_FRAME.get(),
                ItemsRegistration.SOOTHING_FRAME.get()
        );
        tag(RESEARCH_MATERIAL).add(ItemsRegistration.ROYAL_JELLY.get()).add(TagEntry.tag(BEE.location()));
        tag(ItemTags.PLANKS).add(ItemsRegistration.HONEYED_PLANKS.get());
        tag(ItemTags.WOODEN_STAIRS).add(ItemsRegistration.HONEYED_STAIRS.get());
        tag(ItemTags.WOODEN_SLABS).add(ItemsRegistration.HONEYED_SLAB.get());
        tag(ItemTags.WOODEN_FENCES).add(ItemsRegistration.HONEYED_FENCE.get());
        tag(ItemTags.FENCE_GATES).add(ItemsRegistration.HONEYED_FENCE_GATE.get());
        tag(Tags.Items.FENCE_GATES_WOODEN).add(ItemsRegistration.HONEYED_FENCE_GATE.get());
        tag(ItemTags.WOODEN_BUTTONS).add(ItemsRegistration.HONEYED_BUTTON.get());
        tag(ItemTags.WOODEN_PRESSURE_PLATES).add(ItemsRegistration.HONEYED_PRESSURE_PLATE.get());
        tag(ItemTags.WOODEN_DOORS).add(ItemsRegistration.HONEYED_DOOR.get());
        tag(ItemTags.WOODEN_TRAPDOORS).add(ItemsRegistration.HONEYED_TRAPDOOR.get());
        tag(ItemTags.SIGNS).add(ItemsRegistration.HONEYED_SIGN.get());
        tag(ItemTags.HANGING_SIGNS).add(ItemsRegistration.HONEYED_HANGING_SIGN.get());
        tag(ItemTags.STAIRS).add(
                ItemsRegistration.WAX_BLOCK_STAIRS.get(),
                ItemsRegistration.SMOOTH_WAX_STAIRS.get(),
                ItemsRegistration.WAX_BRICK_STAIRS.get()
        );
        tag(ItemTags.SLABS).add(
                ItemsRegistration.WAX_BLOCK_SLAB.get(),
                ItemsRegistration.SMOOTH_WAX_SLAB.get(),
                ItemsRegistration.WAX_BRICK_SLAB.get()
        );
        tag(ItemTags.WALLS).add(
                ItemsRegistration.WAX_BLOCK_WALL.get(),
                ItemsRegistration.SMOOTH_WAX_WALL.get(),
                ItemsRegistration.WAX_BRICK_WALL.get()
        );
    }
}
