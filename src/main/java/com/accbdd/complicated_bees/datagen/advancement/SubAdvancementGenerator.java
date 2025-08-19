package com.accbdd.complicated_bees.datagen.advancement;

import com.accbdd.complicated_bees.datagen.BlockTagGenerator;
import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
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
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class SubAdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {
    private static final Advancement ROOT = advancement()
            .display(ItemsRegistration.SCOOP.get(),
                    Component.translatable("advancements.complicated_bees.root.title"),
                    Component.translatable("advancements.complicated_bees.root.description"),
                    loc("textures/gui/advancement/background.png"),
                    FrameType.TASK,
                    true,
                    true,
                    false)
            .addCriterion("has_scoop", hasItem(ItemTagGenerator.SCOOP_TOOL))
            .build(loc("root"));

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        saver.accept(ROOT);
        Advancement firstBee = advancement(ItemsRegistration.QUEEN.get(), "first_bee")
                .parent(ROOT)
                .addCriterion("nest_broken", hasItem(ItemTagGenerator.BEE))
                .save(saver, loc("first_bee"), existingFileHelper);

        Advancement apiary = simpleHas(ItemsRegistration.APIARY.get(), firstBee, saver, existingFileHelper);
        Advancement meter = simpleHas(ItemsRegistration.METER.get(), firstBee, saver, existingFileHelper);
        Advancement analyzer = simpleHas(ItemsRegistration.ANALYZER.get(), firstBee, saver, existingFileHelper);

        Advancement microscope = simpleUse(BlocksRegistration.MICROSCOPE.get(), analyzer, saver, existingFileHelper);
        Advancement apid_library = simpleUse(BlocksRegistration.APID_LIBRARY.get(), analyzer, saver, existingFileHelper);

        Advancement processing = advancement(ItemsRegistration.CENTRIFUGE.get(), "processing")
                .parent(apiary)
                .addCriterion("furnace_generator", hasItem(ItemsRegistration.FURNACE_GENERATOR.get()))
                .addCriterion("centrifuge", hasItem(ItemsRegistration.CENTRIFUGE.get()))
                .save(saver, loc("processing"), existingFileHelper);

        Advancement mellarium = advancement(ItemsRegistration.MELLARIUM_BASE.get(), "mellarium")
                .parent(apiary)
                .addCriterion("use_mellarium", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().setProperties(
                                StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.top)
                                        .hasProperty(EsotericRegistration.ASSEMBLED, EsotericRegistration.AssembledStatus.side)
                                        .build()
                        ).of(BlockTagGenerator.MELLARIUM).build()),
                        ItemPredicate.Builder.item()))
                .display(ItemsRegistration.MELLARIUM_BASE.get(),
                        Component.translatable("advancements.complicated_bees.mellarium.title"),
                        Component.translatable("advancements.complicated_bees.mellarium.description"),
                        null,
                        FrameType.GOAL,
                        true,
                        true,
                        false)
                .save(saver, loc("mellarium"), existingFileHelper);
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
                null,
                FrameType.TASK,
                true,
                true,
                false);
    }

    private static ResourceLocation loc(String path) {
        return ResourceLocation.tryBuild(MODID, path);
    }

    private static CriterionTriggerInstance hasItem(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }

    private static CriterionTriggerInstance hasItem(ItemLike... items) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build());
    }

    private static Advancement simpleHas(ItemLike item, Advancement parent, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        String id = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
        return advancement(item, id)
                .parent(parent)
                .addCriterion("has_"+id, hasItem(item))
                .save(saver, loc(id), existingFileHelper);
    }

    private static Advancement simpleUse(Block block, Advancement parent, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        String id = BuiltInRegistries.ITEM.getKey(block.asItem()).getPath();
        return advancement(block, id)
                .parent(parent)
                .addCriterion("use_"+id, ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(block).build()),
                        ItemPredicate.Builder.item()))
                .save(saver, loc(id), existingFileHelper);
    }
}
