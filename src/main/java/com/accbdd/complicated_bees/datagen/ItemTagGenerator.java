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
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class ItemTagGenerator extends ItemTagsProvider {
    public static final TagKey<Item> SCOOP_TOOL = ItemTags.create(new ResourceLocation(MODID, "scoop_tool"));
    public static final TagKey<Item> BEE = ItemTags.create(new ResourceLocation(MODID, "bee"));
    public static final TagKey<Item> ROYAL = ItemTags.create(new ResourceLocation(MODID, "royal"));
    public static final TagKey<Item> FRAME = ItemTags.create(new ResourceLocation(MODID, "frame"));
    public static final TagKey<Item> ANALYZER_FUEL = ItemTags.create(new ResourceLocation(MODID, "analyzer_fuel"));
    public static final TagKey<Item> RESEARCH_MATERIAL = ItemTags.create(new ResourceLocation(MODID, "research_material"));
    public static final TagKey<Item> AIR_CON_FUEL = ItemTags.create(new ResourceLocation(MODID, "air_con_fuel"));
    public static final TagKey<Item> AIR_CON_COOLING_1 = ItemTags.create(new ResourceLocation(MODID, "air_con_cooling_1"));
    public static final TagKey<Item> AIR_CON_COOLING_2 = ItemTags.create(new ResourceLocation(MODID, "air_con_cooling_2"));
    public static final TagKey<Item> AIR_CON_COOLING_3 = ItemTags.create(new ResourceLocation(MODID, "air_con_cooling_3"));
    public static final TagKey<Item> AIR_CON_HEATING_1 = ItemTags.create(new ResourceLocation(MODID, "air_con_heating_1"));
    public static final TagKey<Item> AIR_CON_HEATING_2 = ItemTags.create(new ResourceLocation(MODID, "air_con_heating_2"));
    public static final TagKey<Item> AIR_CON_HEATING_3 = ItemTags.create(new ResourceLocation(MODID, "air_con_heating_3"));

    public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(SCOOP_TOOL).add(ItemsRegistration.SCOOP.get());
        tag(ROYAL).add(ItemsRegistration.PRINCESS.get(), ItemsRegistration.QUEEN.get());
        tag(BEE).add(ItemsRegistration.PRINCESS.get(), ItemsRegistration.QUEEN.get(), ItemsRegistration.DRONE.get());
        tag(ANALYZER_FUEL).add(ItemsRegistration.HONEY_DROPLET.get(), ItemsRegistration.ROYAL_JELLY.get());
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
        tag(AIR_CON_COOLING_1).add(
                Items.SNOWBALL,
                Items.SNOW_BLOCK,
                Items.POWDER_SNOW_BUCKET,
                Items.WATER_BUCKET,
                Items.ICE
        );
        tag(AIR_CON_COOLING_2).add(
                Items.PACKED_ICE
        );
        tag(AIR_CON_COOLING_3).add(
                Items.BLUE_ICE
        );
        tag(AIR_CON_HEATING_1).add(
                Items.MAGMA_CREAM,
                Items.MAGMA_BLOCK
        );
        tag(AIR_CON_HEATING_2).add(
                Items.FIRE_CHARGE,
                Items.BLAZE_POWDER,
                Items.BLAZE_ROD
        );
        tag(AIR_CON_HEATING_3).add(
                Items.LAVA_BUCKET,
                Items.DRAGON_BREATH
        );
        tag(AIR_CON_FUEL).addTags(AIR_CON_COOLING_1, AIR_CON_COOLING_2, AIR_CON_COOLING_3, AIR_CON_HEATING_1, AIR_CON_HEATING_2, AIR_CON_HEATING_3);

    }
}
