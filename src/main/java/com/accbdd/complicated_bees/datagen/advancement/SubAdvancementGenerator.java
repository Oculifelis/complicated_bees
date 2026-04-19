package com.accbdd.complicated_bees.datagen.advancement;

import com.accbdd.complicated_bees.datagen.BlockTagGenerator;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Optional;
import java.util.function.Consumer;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class SubAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
    private AdvancementHolder root;

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper ex) {
        this.root = advancement()
                .display(ItemsRegistration.SCOOP.get(),
                        Component.translatable("advancements.complicated_bees.root.title"),
                        Component.translatable("advancements.complicated_bees.root.description"),
                        loc("textures/gui/advancement/background.png"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("has_scoop", hasItem(ItemTagGenerator.SCOOP_TOOL))
                .save(saver, loc("root"), ex);

        hiddenHas(ItemsRegistration.BEESWAX.get(), "beeswax", saver, ex);
        hiddenHas(ItemsRegistration.ROYAL_JELLY.get(), "royal_jelly", saver, ex);
        hiddenHas(ItemsRegistration.PROPOLIS.get(), "propolis", saver, ex);
        hiddenHas(ItemsRegistration.POLLEN.get(), "pollen", saver, ex);

        AdvancementHolder firstBee = advancement(ItemsRegistration.QUEEN.get(), "first_bee")
                .parent(this.root)
                .addCriterion("nest_broken", hasItem(ItemTagGenerator.BEE))
                .save(saver, loc("first_bee"), ex);

        AdvancementHolder apiary = simpleHas(ItemsRegistration.APIARY.get(), firstBee, saver, ex);
        AdvancementHolder meter = simpleHas(ItemsRegistration.METER.get(), firstBee, saver, ex);

        AdvancementHolder processing = advancement(ItemsRegistration.CENTRIFUGE.get(), "processing")
                .parent(apiary)
                .addCriterion("furnace_generator", hasItem(ItemsRegistration.FURNACE_GENERATOR.get()))
                .addCriterion("centrifuge", hasItem(ItemsRegistration.CENTRIFUGE.get()))
                .save(saver, loc("processing"), ex);
        AdvancementHolder frame = advancement(ItemsRegistration.FRAME.get(), "frame")
                .parent(apiary)
                .addCriterion("has_frame", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTagGenerator.FRAME).build()))
                .save(saver, loc("frame"), ex);

        AdvancementHolder honey_droplet = simpleHas(ItemsRegistration.HONEY_DROPLET.get(), processing, saver, ex);

        AdvancementHolder advanced_products = advancement(ItemsRegistration.ROYAL_JELLY.get(), "advanced_products")
                .parent(honey_droplet)
                .addCriterion("royal_jelly", hasItem(ItemsRegistration.ROYAL_JELLY.get()))
                .addCriterion("pollen", hasItem(ItemsRegistration.POLLEN.get()))
                .save(saver, loc("advanced_products"), ex);

        AdvancementHolder analyzer = simpleHas(ItemsRegistration.ANALYZER.get(), honey_droplet, saver, ex);

        AdvancementHolder microscope = simpleUse(BlocksRegistration.MICROSCOPE.get(), analyzer, saver, ex);
        AdvancementHolder apid_library = simpleUse(BlocksRegistration.APID_LIBRARY.get(), analyzer, saver, ex);

        // TODO: cannot match against multiple properties that are named identically ("assembled")

//        AdvancementHolder mellarium = advancement(ItemsRegistration.MELLARIUM_BASE.get(), "mellarium")
//                .parent(advanced_products)
//                .addCriterion("use_mellarium", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
//                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().setProperties(
//                                StatePropertiesPredicate.Builder.properties()
//                                        .hasProperty(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.top)
//                                        .hasProperty(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.side)
//                        ).of(BlockTagGenerator.MELLARIUM)),
//                        ItemPredicate.Builder.item()))
//                .display(ItemsRegistration.MELLARIUM_BASE.get(),
//                        Component.translatable("advancements.complicated_bees.mellarium.title"),
//                        Component.translatable("advancements.complicated_bees.mellarium.description"),
//                        null,
//                        AdvancementType.GOAL,
//                        true,
//                        true,
//                        false)
//                .save(saver, loc("mellarium"), ex);

//        AdvancementHolder gyrofuge = advancement(ItemsRegistration.GYROFUGE_BASE.get(), "gyrofuge")
//                .parent(advanced_products)
//                .addCriterion("use_gyrofuge", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
//                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().setProperties(
//                                StatePropertiesPredicate.Builder.properties()
//                                        .hasProperty(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.top)
//                                        .hasProperty(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.side)
//                        ).of(BlockTagGenerator.GYROFUGE)),
//                        ItemPredicate.Builder.item()))
//                .display(ItemsRegistration.GYROFUGE_BASE.get(),
//                        Component.translatable("advancements.complicated_bees.gyrofuge.title"),
//                        Component.translatable("advancements.complicated_bees.gyrofuge.description"),
//                        null,
//                        AdvancementType.GOAL,
//                        true,
//                        true,
//                        false)
//                .save(saver, loc("gyrofuge"), ex);
    }


    private static Advancement.Builder advancement(ItemLike item, String translationId) {
        return Advancement.Builder.recipeAdvancement().display(display(item, translationId)); //start builder without telemetry
    }

    private static Advancement.Builder advancement() {
        return Advancement.Builder.recipeAdvancement(); //start builder without telemetry
    }

    private static DisplayInfo display(ItemLike item, String translationId) {
        return new DisplayInfo(new ItemStack(item),
                Component.translatable("advancements.complicated_bees."+translationId+".title"),
                Component.translatable("advancements.complicated_bees."+translationId+".description"),
                Optional.empty(),
                AdvancementType.TASK,
                true,
                true,
                false);
    }

    private static ResourceLocation loc(String path) {
        return ResourceLocation.tryBuild(MODID, path);
    }

    private static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

    private static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(ItemLike... items) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build());
    }

    private static AdvancementHolder simpleHas(ItemLike item, AdvancementHolder parent, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        String id = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
        return advancement(item, id)
                .parent(parent)
                .addCriterion("has_"+id, hasItem(item))
                .save(saver, loc(id), existingFileHelper);
    }

    private static AdvancementHolder simpleUse(Block block, AdvancementHolder parent, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        String id = BuiltInRegistries.ITEM.getKey(block.asItem()).getPath();
        return advancement(block, id)
                .parent(parent)
                .addCriterion("use_"+id, ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(block)),
                        ItemPredicate.Builder.item()))
                .save(saver, loc(id), existingFileHelper);
    }

    private AdvancementHolder hiddenHas(ItemLike item, String translationId, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        String id = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
        return advancement(item, id)
                .addCriterion("has_"+id, hasItem(item))
                .parent(this.root)
                .save(saver, loc(id), existingFileHelper);
    }
}
