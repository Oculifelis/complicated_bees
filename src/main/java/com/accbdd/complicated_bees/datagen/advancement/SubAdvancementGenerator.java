package com.accbdd.complicated_bees.datagen.advancement;

import com.accbdd.complicated_bees.datagen.ItemTagGenerator;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class SubAdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {
    private static Advancement ROOT = advancement()
            .display(ItemsRegistration.SCOOP.get(),
                    Component.translatable("advancements.complicated_bees.root.title"),
                    Component.translatable("advancements.complicated_bees.root.description"),
                    loc("textures/gui/advancement/background.png"),
                    FrameType.TASK,
                    true,
                    true,
                    false)
            .addCriterion("has_scoop", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTagGenerator.SCOOP_TOOL).build()))
            .build(loc("root"));

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        saver.accept(ROOT);
        advancement(ItemsRegistration.QUEEN.get(), "first_bee").parent(ROOT).addCriterion("nest_broken",
                InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTagGenerator.BEE).build()))
                .save(saver, loc("first_bee"), existingFileHelper);
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
        return new ResourceLocation(MODID, path);
    }
}
