package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.bees.Comb;
import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTemperature;
import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import com.accbdd.complicated_bees.datagen.builtin.BuiltInSpecies;
import com.accbdd.complicated_bees.datagen.builtin.Combs;
import com.accbdd.complicated_bees.datagen.condition.ItemEnabledCondition;
import com.accbdd.complicated_bees.recipe.*;
import com.accbdd.complicated_bees.recipe.mutation.MutationRecipe;
import com.accbdd.complicated_bees.recipe.mutation.condition.*;
import com.accbdd.complicated_bees.registry.BlocksRegistration;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class RecipeGenerator extends RecipeProvider {
    public RecipeGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        frameRecipe(output, ItemsRegistration.FRAME.get(), Ingredient.of(Tags.Items.STRINGS), Ingredient.of(Tags.Items.RODS_WOODEN));
        frameRecipe(output, ItemsRegistration.WAXED_FRAME.get(), Ingredient.of(Tags.Items.STRINGS), Ingredient.of(ItemsRegistration.WAXED_STICK.get()), ItemsRegistration.WAXED_STICK.get());
        frameRecipe(output, ItemsRegistration.HONEYED_FRAME.get(), Ingredient.of(Tags.Items.STRINGS), Ingredient.of(ItemsRegistration.HONEYED_STICK.get()), ItemsRegistration.HONEYED_STICK.get());
        frameRecipe(output, ItemsRegistration.TWISTING_FRAME.get(), Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL), Ingredient.of(ItemsRegistration.WAXED_STICK.get()), ItemsRegistration.WAXED_STICK.get());
        frameRecipe(output, ItemsRegistration.SOOTHING_FRAME.get(), Ingredient.of(ItemsRegistration.ROYAL_JELLY.get()), Ingredient.of(ItemsRegistration.HONEYED_STICK.get()), ItemsRegistration.HONEYED_STICK.get());
        frameRecipe(output, ItemsRegistration.RESTRICTIVE_FRAME.get(), Ingredient.of(Items.CHAIN), Ingredient.of(ItemsRegistration.WAXED_STICK.get()), ItemsRegistration.WAXED_STICK.get());
        frameRecipe(output, ItemsRegistration.DRY_FRAME.get(), Ingredient.of(ItemTags.SAND), Ingredient.of(ItemsRegistration.WAXED_STICK.get()), ItemsRegistration.WAXED_STICK.get());
        frameRecipe(output, ItemsRegistration.WET_FRAME.get(), Ingredient.of(Items.WATER_BUCKET), Ingredient.of(ItemsRegistration.WAXED_STICK.get()), ItemsRegistration.WAXED_STICK.get());
        frameRecipe(output, ItemsRegistration.HOT_FRAME.get(), Ingredient.of(Items.MAGMA_BLOCK), Ingredient.of(Items.NETHER_BRICK), ItemsRegistration.WAXED_STICK.get());
        frameRecipe(output, ItemsRegistration.COLD_FRAME.get(), Ingredient.of(Items.BLUE_ICE), Ingredient.of(ItemsRegistration.WAXED_STICK.get()), ItemsRegistration.WAXED_STICK.get());
        deadlyFrame(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING)
                .requires(ItemsRegistration.SILK_WISP.get(), 3)
                .unlockedBy(getHasName(ItemsRegistration.SILK_WISP.get()), has(ItemsRegistration.SILK_WISP.get()))
                .save(output, "complicated_bees:string_from_silk");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CANDLE)
                .pattern("#")
                .pattern("W")
                .define('#', Items.STRING)
                .define('W', ItemsRegistration.BEESWAX.get())
                .unlockedBy(getHasName(ItemsRegistration.BEESWAX.get()), has(ItemsRegistration.BEESWAX.get()))
                .save(output, "complicated_bees:candle");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemsRegistration.HONEYED_STICK.get())
                .requires(ItemsRegistration.HONEY_DROPLET.get(), 4)
                .requires(Ingredient.of(Tags.Items.RODS_WOODEN), 1)
                .unlockedBy(getHasName(ItemsRegistration.FRAME.get()), has(ItemsRegistration.FRAME.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemsRegistration.WAXED_STICK.get())
                .requires(ItemsRegistration.BEESWAX.get(), 2)
                .requires(Ingredient.of(Tags.Items.RODS_WOODEN), 1)
                .unlockedBy(getHasName(ItemsRegistration.FRAME.get()), has(ItemsRegistration.FRAME.get()))
                .save(output);
        enabledRecipe(ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemsRegistration.HONEY_BREAD.get())
                .requires(Items.BREAD)
                .requires(ItemsRegistration.HONEY_DROPLET.get(), 4)
                .unlockedBy(getHasName(ItemsRegistration.HONEY_DROPLET.get()), has(ItemsRegistration.HONEY_DROPLET.get())), output);
        enabledRecipe(ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ItemsRegistration.HONEY_PORKCHOP.get())
                .requires(Items.COOKED_PORKCHOP)
                .requires(ItemsRegistration.HONEY_DROPLET.get(), 4)
                .unlockedBy(getHasName(ItemsRegistration.HONEY_DROPLET.get()), has(ItemsRegistration.HONEY_DROPLET.get())), output);
        enabledRecipe(ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ItemsRegistration.AMBROSIA.get())
                .pattern("HHH")
                .pattern("PRP")
                .pattern("HHH")
                .define('H', ItemsRegistration.HONEY_DROPLET.get())
                .define('P', ItemsRegistration.POLLEN.get())
                .define('R', ItemsRegistration.ROYAL_JELLY.get())
                .unlockedBy(getHasName(ItemsRegistration.HONEY_DROPLET.get()), has(ItemsRegistration.HONEY_DROPLET.get())), output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.ANALYZER.get())
                .pattern("IGI")
                .pattern("RWR")
                .pattern("ITI")
                .define('G', Tags.Items.GLASS_PANES)
                .define('I', Items.GOLD_INGOT)
                .define('W', Items.DIAMOND)
                .define('R', Items.REDSTONE)
                .define('T', Items.TRIPWIRE_HOOK)
                .unlockedBy("has_bee", has(ItemsRegistration.PRINCESS.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.METER.get())
                .pattern("IGI")
                .pattern("RWR")
                .pattern("ITI")
                .define('G', Tags.Items.GLASS_PANES)
                .define('I', Items.IRON_INGOT)
                .define('W', Items.REDSTONE_TORCH)
                .define('R', Items.REDSTONE)
                .define('T', Items.TRIPWIRE_HOOK)
                .unlockedBy("has_bee", has(ItemsRegistration.PRINCESS.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.SCOOP.get())
                .pattern("SWS")
                .pattern("SSS")
                .pattern(" S ")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('W', ItemTags.WOOL)
                .unlockedBy("has_wool", has(ItemTags.WOOL)).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.APIARY.get())
                .pattern("PPP")
                .pattern("ICI")
                .pattern("PPP")
                .define('P', ItemTags.PLANKS)
                .define('I', Items.IRON_INGOT)
                .define('C', ItemTagGenerator.COMB)
                .unlockedBy("has_bee", has(ItemsRegistration.PRINCESS.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.CENTRIFUGE.get())
                .pattern("III")
                .pattern("ICI")
                .pattern("SRS")
                .define('C', Items.CHEST)
                .define('I', Items.IRON_INGOT)
                .define('S', Items.SMOOTH_STONE)
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(ItemsRegistration.COMB.get()), has(ItemsRegistration.COMB.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.FURNACE_GENERATOR.get())
                .pattern("CIC")
                .pattern("IBI")
                .pattern("SRS")
                .define('C', Items.COPPER_INGOT)
                .define('I', Items.IRON_INGOT)
                .define('B', Items.BLAST_FURNACE)
                .define('S', Items.SMOOTH_STONE)
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(ItemsRegistration.COMB.get()), has(ItemsRegistration.COMB.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.HONEY_GENERATOR.get())
                .pattern("IGI")
                .pattern("PFP")
                .pattern("WWW")
                .define('F', ItemsRegistration.FURNACE_GENERATOR.get())
                .define('W', ItemsRegistration.SMOOTH_WAX.get())
                .define('P', ItemsRegistration.PROPOLIS.get())
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('I', Items.IRON_INGOT)
                .unlockedBy(getHasName(ItemsRegistration.FURNACE_GENERATOR.get()), has(ItemsRegistration.FURNACE_GENERATOR.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.AUTOLYZER.get())
                .pattern("III")
                .pattern("IAI")
                .pattern("WWW")
                .define('A', ItemsRegistration.ANALYZER.get())
                .define('W', Items.SMOOTH_STONE)
                .define('I', Items.IRON_INGOT)
                .unlockedBy(getHasName(ItemsRegistration.ANALYZER.get()), has(ItemsRegistration.ANALYZER.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.BASIC_UPGRADE.get())
                .pattern("SPS")
                .pattern("PHP")
                .pattern("SPS")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('P', ItemsRegistration.PROPOLIS.get())
                .define('H', ItemTags.PLANKS)
                .unlockedBy(getHasName(ItemsRegistration.PROPOLIS.get()), has(ItemsRegistration.PROPOLIS.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.WAXED_UPGRADE.get())
                .pattern("PWP")
                .pattern("WBW")
                .pattern("PWP")
                .define('P', ItemsRegistration.PROPOLIS.get())
                .define('B', ItemsRegistration.BASIC_UPGRADE.get())
                .define('W', ItemsRegistration.BEESWAX.get())
                .unlockedBy(getHasName(ItemsRegistration.BASIC_UPGRADE.get()), has(ItemsRegistration.BASIC_UPGRADE.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.HONEYED_UPGRADE.get())
                .pattern("PWP")
                .pattern("WBW")
                .pattern("PWP")
                .define('P', ItemsRegistration.PROPOLIS.get())
                .define('B', ItemsRegistration.BASIC_UPGRADE.get())
                .define('W', ItemsRegistration.HONEYED_PLANKS.get())
                .unlockedBy(getHasName(ItemsRegistration.BASIC_UPGRADE.get()), has(ItemsRegistration.BASIC_UPGRADE.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.ROYAL_UPGRADE.get())
                .pattern("PRP")
                .pattern("RBR")
                .pattern("PRP")
                .define('P', ItemsRegistration.PROPOLIS.get())
                .define('B', ItemsRegistration.BASIC_UPGRADE.get())
                .define('R', ItemsRegistration.ROYAL_JELLY.get())
                .unlockedBy(getHasName(ItemsRegistration.ROYAL_JELLY.get()), has(ItemsRegistration.ROYAL_JELLY.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.TWISTED_UPGRADE.get())
                .pattern("PLP")
                .pattern("SBS")
                .pattern("PSP")
                .define('P', ItemsRegistration.PROPOLIS.get())
                .define('B', ItemsRegistration.BASIC_UPGRADE.get())
                .define('L', Items.SHROOMLIGHT)
                .define('S', ItemTags.SOUL_FIRE_BASE_BLOCKS)
                .unlockedBy(getHasName(ItemsRegistration.BASIC_UPGRADE.get()), has(ItemsRegistration.BASIC_UPGRADE.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.SILKY_UPGRADE.get())
                .pattern("PRP")
                .pattern("RBR")
                .pattern("PRP")
                .define('P', ItemsRegistration.PROPOLIS.get())
                .define('B', ItemsRegistration.BASIC_UPGRADE.get())
                .define('R', ItemsRegistration.WOVEN_MESH.get())
                .unlockedBy(getHasName(ItemsRegistration.BASIC_UPGRADE.get()), has(ItemsRegistration.BASIC_UPGRADE.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.ENDENIC_UPGRADE.get())
                .pattern("ECE")
                .pattern("FRF")
                .pattern("EFE")
                .define('E', Items.END_STONE)
                .define('R', ItemsRegistration.ROYAL_UPGRADE.get())
                .define('C', Items.CHORUS_FLOWER)
                .define('F', Items.DRAGON_BREATH)
                .unlockedBy(getHasName(ItemsRegistration.BASIC_UPGRADE.get()), has(ItemsRegistration.BASIC_UPGRADE.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.WITHERED_UPGRADE.get(), 2)
                .pattern("ISI")
                .pattern("PRP")
                .pattern("INI")
                .define('I', Items.IRON_INGOT)
                .define('R', ItemsRegistration.ROYAL_UPGRADE.get())
                .define('S', Items.NETHER_STAR)
                .define('P', ItemsRegistration.POLLEN.get())
                .define('N', Items.NETHERITE_SCRAP)
                .unlockedBy(getHasName(ItemsRegistration.BASIC_UPGRADE.get()), has(ItemsRegistration.BASIC_UPGRADE.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.WOVEN_MESH.get())
                .pattern("WWW")
                .pattern("WWW")
                .pattern("WWW")
                .define('W', ItemsRegistration.SILK_WISP.get())
                .unlockedBy(getHasName(ItemsRegistration.SILK_WISP.get()), has(ItemsRegistration.SILK_WISP.get())).save(output);
        enabledRecipe(ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemsRegistration.BEE_STAFF.get())
                .pattern(" LJ")
                .pattern("PZL")
                .pattern("ZP ")
                .define('Z', Items.BLAZE_ROD)
                .define('P', ItemsRegistration.PROPOLIS.get())
                .define('J', ItemsRegistration.ROYAL_JELLY.get())
                .define('L', ItemsRegistration.POLLEN.get())
                .unlockedBy(getHasName(ItemsRegistration.ROYAL_JELLY.get()), has(ItemsRegistration.ROYAL_JELLY.get())), output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemsRegistration.APIARIST_HELMET.get())
                .pattern("MMM")
                .pattern("M M")
                .define('M', ItemsRegistration.WOVEN_MESH.get())
                .unlockedBy(getHasName(ItemsRegistration.SILK_WISP.get()), has(ItemsRegistration.SILK_WISP.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemsRegistration.APIARIST_CHESTPLATE.get())
                .pattern("M M")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ItemsRegistration.WOVEN_MESH.get())
                .unlockedBy(getHasName(ItemsRegistration.SILK_WISP.get()), has(ItemsRegistration.SILK_WISP.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemsRegistration.APIARIST_LEGGINGS.get())
                .pattern("MMM")
                .pattern("M M")
                .pattern("M M")
                .define('M', ItemsRegistration.WOVEN_MESH.get())
                .unlockedBy(getHasName(ItemsRegistration.SILK_WISP.get()), has(ItemsRegistration.SILK_WISP.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_PANEL.get(), 4)
                .pattern("RW#")
                .pattern("W#W")
                .pattern("#WP")
                .define('#', ItemsRegistration.HONEYED_PLANKS.get())
                .define('P', ItemsRegistration.POLLEN.get())
                .define('R', ItemsRegistration.ROYAL_JELLY.get())
                .define('W', ItemsRegistration.BEESWAX.get())
                .unlockedBy(getHasName(ItemsRegistration.BEESWAX.get()), has(ItemsRegistration.BEESWAX.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_BASE.get())
                .pattern("###")
                .pattern("#A#")
                .pattern("###")
                .define('#', ItemsRegistration.MELLARIUM_PANEL.get())
                .define('A', ItemTags.LOGS)
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_TEMP_UNIT.get())
                .pattern("I#I")
                .pattern("#A#")
                .pattern("I#I")
                .define('#', Items.IRON_BARS)
                .define('I', Items.IRON_INGOT)
                .define('A', ItemsRegistration.MELLARIUM_BASE.get())
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_HYDROREGULATOR.get())
                .pattern(" N ")
                .pattern("#A#")
                .define('#', Items.PRISMARINE_BRICKS)
                .define('N', Items.NAUTILUS_SHELL)
                .define('A', ItemsRegistration.MELLARIUM_BASE.get())
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_MUTATOR.get())
                .pattern("#E#")
                .pattern("#A#")
                .define('#', Items.GOLD_INGOT)
                .define('E', Items.ENDER_EYE)
                .define('A', ItemsRegistration.MELLARIUM_BASE.get())
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_RAIN_SHIELD.get())
                .pattern(" H ")
                .pattern("NAN")
                .pattern("###")
                .define('#', Items.PRISMARINE)
                .define('N', Items.NAUTILUS_SHELL)
                .define('H', Items.HEART_OF_THE_SEA)
                .define('A', ItemsRegistration.MELLARIUM_BASE.get())
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_FRAME_HOUSING_1.get())
                .pattern("###")
                .pattern("CAC")
                .pattern("###")
                .define('#', ItemsRegistration.MELLARIUM_PANEL.get())
                .define('C', Items.COPPER_INGOT)
                .define('A', ItemsRegistration.MELLARIUM_BASE.get())
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_FRAME_HOUSING_2.get())
                .pattern("###")
                .pattern("GAG")
                .pattern("###")
                .define('#', ItemsRegistration.MELLARIUM_PANEL.get())
                .define('G', Items.GOLD_INGOT)
                .define('A', ItemsRegistration.MELLARIUM_FRAME_HOUSING_1.get())
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_FRAME_HOUSING_3.get())
                .pattern("#N#")
                .pattern("#A#")
                .pattern("###")
                .define('#', ItemsRegistration.MELLARIUM_PANEL.get())
                .define('N', Items.NETHERITE_INGOT)
                .define('A', ItemsRegistration.MELLARIUM_FRAME_HOUSING_2.get())
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_ENERGY_CELL.get())
                .pattern("#C#")
                .pattern("RAR")
                .pattern("#R#")
                .define('#', ItemsRegistration.MELLARIUM_PANEL.get())
                .define('C', Items.COPPER_INGOT)
                .define('A', ItemsRegistration.MELLARIUM_BASE.get())
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_SKYBOX.get())
                .pattern("#L#")
                .pattern("GAG")
                .pattern("#D#")
                .define('#', ItemsRegistration.MELLARIUM_PANEL.get())
                .define('L', Items.REDSTONE_LAMP)
                .define('A', ItemsRegistration.MELLARIUM_BASE.get())
                .define('G', Items.GLOWSTONE)
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_TEMPORAL_SIMULATOR.get())
                .pattern("#C#")
                .pattern("GAG")
                .pattern("#T#")
                .define('#', ItemsRegistration.MELLARIUM_PANEL.get())
                .define('C', Items.CLOCK)
                .define('A', ItemsRegistration.MELLARIUM_BASE.get())
                .define('G', Items.GOLD_BLOCK)
                .define('T', Items.TOTEM_OF_UNDYING)
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MELLARIUM_OUTPUT_HATCH.get())
                .pattern("#C#")
                .pattern("BAB")
                .pattern("#H#")
                .define('#', ItemsRegistration.MELLARIUM_PANEL.get())
                .define('C', Tags.Items.CHESTS)
                .define('A', ItemsRegistration.MELLARIUM_BASE.get())
                .define('B', Items.POWERED_RAIL)
                .define('H', Items.HOPPER)
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.IRON_INGOT),
                Ingredient.of(ItemsRegistration.MELLARIUM_PANEL.get()),
                Ingredient.of(Items.REDSTONE_TORCH),
                RecipeCategory.MISC,
                ItemsRegistration.GYROFUGE_PANEL.get())
                .unlocks(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output, "gyrofuge_panel");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_BASE.get())
                .pattern("###")
                .pattern("#A#")
                .pattern("###")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', Items.IRON_BLOCK)
                .unlockedBy(getHasName(ItemsRegistration.MELLARIUM_PANEL.get()), has(ItemsRegistration.MELLARIUM_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_ENERGY_CELL.get())
                .pattern("#C#")
                .pattern("RAR")
                .pattern("#R#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('C', Items.COPPER_INGOT)
                .define('A', ItemsRegistration.GYROFUGE_BASE.get())
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_BASIC_PROCESSING_UNIT.get())
                .pattern("#C#")
                .pattern("GAG")
                .pattern("#D#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_BASE.get())
                .define('C', ItemsRegistration.CENTRIFUGE.get())
                .define('G', Items.GOLD_INGOT)
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_PROCESSING_UNIT.get())
                .pattern("#C#")
                .pattern("GAG")
                .pattern("#D#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_BASIC_PROCESSING_UNIT.get())
                .define('C', ItemsRegistration.CENTRIFUGE.get())
                .define('G', Items.GOLD_BLOCK)
                .define('D', Items.DIAMOND_BLOCK)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_ADVANCED_PROCESSING_UNIT.get())
                .pattern("#W#")
                .pattern("CAC")
                .pattern("#N#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_PROCESSING_UNIT.get())
                .define('C', ItemsRegistration.CENTRIFUGE.get())
                .define('W', Items.NETHER_STAR)
                .define('N', Items.NETHERITE_BLOCK)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_BASIC_SPEED_UNIT.get())
                .pattern("#S#")
                .pattern("RAR")
                .pattern("#D#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_BASE.get())
                .define('S', Items.SUGAR)
                .define('R', Items.REDSTONE)
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_SPEED_UNIT.get())
                .pattern("#C#")
                .pattern("GAG")
                .pattern("#D#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_BASIC_SPEED_UNIT.get())
                .define('C', Items.GOLDEN_CARROT)
                .define('G', Items.REDSTONE_BLOCK)
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_ADVANCED_SPEED_UNIT.get())
                .pattern("#G#")
                .pattern("CAC")
                .pattern("#N#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_SPEED_UNIT.get())
                .define('G', Items.GOLDEN_APPLE)
                .define('C', Items.REDSTONE_LAMP)
                .define('N', Items.NETHERITE_INGOT)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_BASIC_EFFICIENCY_UNIT.get())
                .pattern("#S#")
                .pattern("RAR")
                .pattern("#D#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_BASE.get())
                .define('S', Items.BOOKSHELF)
                .define('R', Items.COPPER_INGOT)
                .define('D', Items.GOLD_INGOT)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_EFFICIENCY_UNIT.get())
                .pattern("#C#")
                .pattern("GAG")
                .pattern("#D#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_BASIC_EFFICIENCY_UNIT.get())
                .define('C', Items.CRYING_OBSIDIAN)
                .define('G', Items.COPPER_BLOCK)
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_ADVANCED_EFFICIENCY_UNIT.get())
                .pattern("#G#")
                .pattern("CAC")
                .pattern("#N#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_EFFICIENCY_UNIT.get())
                .define('G', ItemsRegistration.EXP_DROP.get())
                .define('C', Items.CHORUS_FLOWER)
                .define('N', Items.NETHERITE_INGOT)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_BASIC_EXTRACTION_UNIT.get())
                .pattern("#S#")
                .pattern("RAR")
                .pattern("#D#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_BASE.get())
                .define('S', Items.AMETHYST_SHARD)
                .define('R', Items.PISTON)
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_EXTRACTION_UNIT.get())
                .pattern("#C#")
                .pattern("GAG")
                .pattern("#D#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_BASIC_EXTRACTION_UNIT.get())
                .define('C', Items.ANVIL)
                .define('G', Items.PISTON)
                .define('D', Items.NETHERITE_INGOT)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_ADVANCED_EXTRACTION_UNIT.get())
                .pattern("#G#")
                .pattern("CAC")
                .pattern("#N#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('A', ItemsRegistration.GYROFUGE_EXTRACTION_UNIT.get())
                .define('G', ItemsRegistration.AMBROSIA.get())
                .define('C', Items.DRAGON_BREATH)
                .define('N', Items.END_CRYSTAL)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_OUTPUT_HATCH.get())
                .pattern("#C#")
                .pattern("BAB")
                .pattern("#H#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('C', Tags.Items.CHESTS)
                .define('A', ItemsRegistration.GYROFUGE_BASE.get())
                .define('B', Items.POWERED_RAIL)
                .define('H', Items.HOPPER)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.GYROFUGE_INPUT_HATCH.get())
                .pattern("#H#")
                .pattern("BAB")
                .pattern("#C#")
                .define('#', ItemsRegistration.GYROFUGE_PANEL.get())
                .define('C', Tags.Items.CHESTS)
                .define('A', ItemsRegistration.GYROFUGE_BASE.get())
                .define('B', Items.POWERED_RAIL)
                .define('H', Items.HOPPER)
                .unlockedBy(getHasName(ItemsRegistration.GYROFUGE_PANEL.get()), has(ItemsRegistration.GYROFUGE_PANEL.get())).save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemsRegistration.APIARIST_BOOTS.get())
                .pattern("M M")
                .pattern("M M")
                .define('M', ItemsRegistration.WOVEN_MESH.get())
                .unlockedBy(getHasName(ItemsRegistration.SILK_WISP.get()), has(ItemsRegistration.SILK_WISP.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.MICROSCOPE.get(), 1)
                .pattern(" SI")
                .pattern(" GI")
                .pattern("BBB")
                .define('S', Items.SPYGLASS)
                .define('I', Items.IRON_INGOT)
                .define('B', Items.IRON_BLOCK)
                .define('G', Items.GLASS_PANE)
                .unlockedBy(getHasName(ItemsRegistration.CENTRIFUGE.get()), has(ItemsRegistration.CENTRIFUGE.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.APID_LIBRARY.get(), 1)
                .pattern("IGI")
                .pattern("PWP")
                .pattern("III")
                .define('W', ItemsRegistration.WAX_BLOCK.get())
                .define('I', Items.IRON_INGOT)
                .define('P', ItemsRegistration.PROPOLIS.get())
                .define('G', Items.GLASS_PANE)
                .unlockedBy(getHasName(ItemsRegistration.CENTRIFUGE.get()), has(ItemsRegistration.CENTRIFUGE.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.BEE_SORTER.get(), 1)
                .pattern("WIW")
                .pattern("PCP")
                .pattern("WDW")
                .define('W', ItemsRegistration.HONEYED_PLANKS.get())
                .define('I', Items.HOPPER)
                .define('C', Items.COMPARATOR)
                .define('P', ItemsRegistration.PROPOLIS.get())
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ItemsRegistration.APIARY.get()), has(ItemsRegistration.APIARY.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ItemsRegistration.HONEYED_PLANKS.get(), 8)
                .pattern("SSS")
                .pattern("SBS")
                .pattern("SSS")
                .define('S', ItemTags.PLANKS)
                .define('B', ItemsRegistration.HONEY_DROPLET.get())
                .unlockedBy(getHasName(ItemsRegistration.HONEY_DROPLET.get()), has(ItemsRegistration.HONEY_DROPLET.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ItemsRegistration.WAX_BLOCK.get())
                .pattern("SS")
                .pattern("SS")
                .define('S', ItemsRegistration.BEESWAX.get())
                .unlockedBy(getHasName(ItemsRegistration.BEESWAX.get()), has(ItemsRegistration.BEESWAX.get())).save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ItemsRegistration.WAX_BRICKS.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', ItemsRegistration.WAX_BLOCK.get())
                .unlockedBy(getHasName(ItemsRegistration.WAX_BLOCK.get()), has(ItemsRegistration.WAX_BLOCK.get())).save(output);
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ItemsRegistration.WAX_BLOCK.get()),
                        RecipeCategory.BUILDING_BLOCKS,
                        ItemsRegistration.SMOOTH_WAX.get(),
                        0.1f,
                        200)
                .unlockedBy(getHasName(ItemsRegistration.WAX_BLOCK.get()), has(ItemsRegistration.WAX_BLOCK.get())).save(output);
        generateRecipes(output, DataGenerators.HONEYED_PLANK_FAMILY, FeatureFlagSet.of());
        generateRecipes(output, DataGenerators.WAX_BLOCK_FAMILY, FeatureFlagSet.of());
        generateRecipes(output, DataGenerators.WAX_BRICK_FAMILY, FeatureFlagSet.of());
        generateRecipes(output, DataGenerators.SMOOTH_WAX_FAMILY, FeatureFlagSet.of());
        stonecutterFor(output, DataGenerators.WAX_BLOCK_FAMILY);
        stonecutterFor(output, DataGenerators.WAX_BRICK_FAMILY);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, BlocksRegistration.WAX_BRICKS.get(), BlocksRegistration.WAX_BLOCK.get());
        stonecutterFor(output, DataGenerators.WAX_BRICK_FAMILY, BlocksRegistration.WAX_BLOCK.get());
        stonecutterFor(output, DataGenerators.SMOOTH_WAX_FAMILY);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, BlocksRegistration.CHISELED_WAX.get(), BlocksRegistration.SMOOTH_WAX.get());

        mutatorRecipe(output, "ender_eye", Ingredient.of(Items.ENDER_EYE), 4f);
        mutatorRecipe(output, "ender_pearl", Ingredient.of(Items.ENDER_PEARL), 2f);
        mutatorRecipe(output, "soul_sand", Ingredient.of(Items.SOUL_SAND), 1.5f);
        mutatorRecipe(output, "nether_star", Ingredient.of(Items.NETHER_STAR), 50f);
        mutatorRecipe(output, "warped_wart_block", Ingredient.of(Items.WARPED_WART_BLOCK), 1.5f);
        mutatorRecipe(output, "royal_jelly", Ingredient.of(ItemsRegistration.ROYAL_JELLY.get()), 3f);

        tempUnitRecipe(output, "snowball", Ingredient.of(Items.SNOWBALL), EnumTolerance.DOWN_1, 0.3f);
        tempUnitRecipe(output, "snow_block", Ingredient.of(Items.SNOW_BLOCK), EnumTolerance.DOWN_1, 0.15f);
        tempUnitRecipe(output, "powder_snow_bucket", Ingredient.of(Items.POWDER_SNOW_BUCKET), EnumTolerance.DOWN_1, 0.05f);
        tempUnitRecipe(output, "water_bucket", Ingredient.of(Items.WATER_BUCKET), EnumTolerance.DOWN_1, 0.05f);
        tempUnitRecipe(output, "ice", Ingredient.of(Items.ICE), EnumTolerance.DOWN_1, 0.05f);
        tempUnitRecipe(output, "packed_ice", Ingredient.of(Items.PACKED_ICE), EnumTolerance.DOWN_2, 0.05f);
        tempUnitRecipe(output, "blue_ice", Ingredient.of(Items.BLUE_ICE), EnumTolerance.DOWN_3, 0.05f);
        tempUnitRecipe(output, "magma_cream", Ingredient.of(Items.MAGMA_CREAM), EnumTolerance.UP_1, 0.25f);
        tempUnitRecipe(output, "magma_block", Ingredient.of(Items.MAGMA_BLOCK), EnumTolerance.UP_1, 0.1f);
        tempUnitRecipe(output, "fire_charge", Ingredient.of(Items.FIRE_CHARGE), EnumTolerance.UP_2, 0.15f);
        tempUnitRecipe(output, "blaze_powder", Ingredient.of(Items.BLAZE_POWDER), EnumTolerance.UP_2, 0.1f);
        tempUnitRecipe(output, "blaze_rod", Ingredient.of(Items.BLAZE_ROD), EnumTolerance.UP_2, 0.05f);
        tempUnitRecipe(output, "dragon_breath", Ingredient.of(Items.DRAGON_BREATH), EnumTolerance.UP_3, 0.01f);
        tempUnitRecipe(output, "lava_bucket", Ingredient.of(Items.LAVA_BUCKET), EnumTolerance.UP_3, 0.01f);

        hydroregulatorRecipe(output, "sand", Ingredient.of(ItemTags.SAND), new Product(Items.DIRT.getDefaultInstance(), 0.75f), EnumTolerance.DOWN_1, 0.15f);
        hydroregulatorRecipe(output, "water_bucket", Ingredient.of(Items.WATER_BUCKET), new Product(Items.BUCKET.getDefaultInstance(), 1), EnumTolerance.UP_1, 0.05f);
        hydroregulatorRecipe(output, "sponge", Ingredient.of(Items.SPONGE), new Product(Items.WET_SPONGE.getDefaultInstance(), 1), EnumTolerance.DOWN_1, 0.05f);
        hydroregulatorRecipe(output, "wet_sponge", Ingredient.of(Items.WET_SPONGE), new Product(Items.SPONGE.getDefaultInstance(), 1), EnumTolerance.UP_1, 0.05f);

        honeyGeneratorRecipe(output, "honey_droplet", Ingredient.of(ItemsRegistration.HONEY_DROPLET.get()), 400);
        honeyGeneratorRecipe(output, "beeswax", Ingredient.of(ItemsRegistration.BEESWAX.get()), 150);
        honeyGeneratorRecipe(output, "propolis", Ingredient.of(ItemsRegistration.PROPOLIS.get()), 200);
        honeyGeneratorRecipe(output, "pollen", Ingredient.of(ItemsRegistration.POLLEN.get()), 800);
        honeyGeneratorRecipe(output, "royal_jelly", Ingredient.of(ItemsRegistration.ROYAL_JELLY.get()), 1600);
        honeyGeneratorRecipe(output, "drone", Ingredient.of(ItemsRegistration.DRONE.get()), 800);
        honeyGeneratorRecipe(output, "princess", Ingredient.of(ItemsRegistration.PRINCESS.get()), 4800);
        honeyGeneratorRecipe(output, "queen", Ingredient.of(ItemsRegistration.QUEEN.get()), 9600);

        centrifugeRecipe(output,
                "amethyst_comb",
                combIngredient(Combs.AMETHYST),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.AMETHYST_SHARD, 0.25f));
        centrifugeRecipe(output,
                "bottle_to_droplet",
                Ingredient.of(Items.HONEY_BOTTLE),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 3, 1f));
        centrifugeRecipe(output,
                "coal_comb",
                combIngredient(Combs.COAL),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.COAL, 0.25f));
        centrifugeRecipe(output,
                "copper_comb",
                combIngredient(Combs.COPPER),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.RAW_COPPER, 0.25f));
        centrifugeRecipe(output,
                "diamond_comb",
                combIngredient(Combs.DIAMOND),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.DIAMOND, 0.25f));
        centrifugeRecipe(output,
                "dripping_comb",
                combIngredient(Combs.DRIPPING),
                new Product(ItemsRegistration.BEESWAX.get(), 0.3f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.85f));
        centrifugeRecipe(output,
                "dusty_comb",
                combIngredient(Combs.DUSTY),
                new Product(ItemsRegistration.BEESWAX.get(), 0.3f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.5f));
        centrifugeRecipe(output,
                "emerald_comb",
                combIngredient(Combs.EMERALD),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.EMERALD, 0.25f));
        centrifugeRecipe(output,
                "glowstone_comb",
                combIngredient(Combs.GLOWSTONE),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.GLOWSTONE_DUST, 0.25f));
        centrifugeRecipe(output,
                "gold_comb",
                combIngredient(Combs.GOLD),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.RAW_GOLD, 0.25f));
        centrifugeRecipe(output,
                "honey_comb",
                combIngredient(Combs.HONEY),
                new Product(ItemsRegistration.BEESWAX.get(), 0.4f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.7f));
        centrifugeRecipe(output,
                "iron_comb",
                combIngredient(Combs.IRON),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.RAW_IRON, 0.25f));
        centrifugeRecipe(output,
                "lapis_comb",
                combIngredient(Combs.LAPIS),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.LAPIS_LAZULI, 0.25f));
        centrifugeRecipe(output,
                "mysterious_comb",
                combIngredient(Combs.MYSTERIOUS),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.5f),
                new Product(ItemsRegistration.PEARL_SHARD.get(), 0.1f));
        centrifugeRecipe(output,
                "netherite_comb",
                combIngredient(Combs.NETHERITE),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.NETHERITE_SCRAP, 0.25f));
        centrifugeRecipe(output,
                "propolis",
                Ingredient.of(ItemsRegistration.PROPOLIS),
                new Product(ItemsRegistration.SILK_WISP.get(), 0.6f),
                new Product(Items.SLIME_BALL, 0.1f));
        centrifugeRecipe(output,
                "quartz_comb",
                combIngredient(Combs.QUARTZ),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.QUARTZ, 0.25f));
        centrifugeRecipe(output,
                "redstone_comb",
                combIngredient(Combs.REDSTONE),
                new Product(ItemsRegistration.BEESWAX.get(), 0.5f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.3f),
                new Product(Items.REDSTONE, 0.25f));
        centrifugeRecipe(output,
                "rocky_comb",
                combIngredient(Combs.ROCKY),
                new Product(ItemsRegistration.BEESWAX.get(), 0.8f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.4f));
        centrifugeRecipe(output,
                "rotten_comb",
                combIngredient(Combs.ROTTEN),
                new Product(ItemsRegistration.BEESWAX.get(), 0.6f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.2f),
                new Product(Items.BONE_MEAL, 0.3f),
                new Product(Items.ROTTEN_FLESH, 0.1f));
        centrifugeRecipe(output,
                "royal_comb",
                combIngredient(Combs.ROYAL),
                new Product(ItemsRegistration.BEESWAX.get(), 0.2f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.9f),
                new Product(ItemsRegistration.ROYAL_JELLY.get(), 0.2f));
        centrifugeRecipe(output,
                "silky_comb",
                combIngredient(Combs.SILKY),
                new Product(ItemsRegistration.BEESWAX.get(), 0.1f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.5f),
                new Product(ItemsRegistration.PROPOLIS.get(), 0.7f));
        centrifugeRecipe(output,
                "simmering_comb",
                combIngredient(Combs.SIMMERING),
                new Product(ItemsRegistration.BEESWAX.get(), 0.2f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.75f),
                new Product(ItemsRegistration.PROPOLIS.get(), 0.1f));
        centrifugeRecipe(output,
                "spectral_comb",
                combIngredient(Combs.SPECTRAL),
                new Product(ItemsRegistration.BEESWAX.get(), 0.3f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.65f));
        centrifugeRecipe(output,
                "stringy_comb",
                combIngredient(Combs.STRINGY),
                new Product(ItemsRegistration.BEESWAX.get(), 0.2f),
                new Product(ItemsRegistration.HONEY_DROPLET.get(), 0.5f),
                new Product(ItemsRegistration.PROPOLIS.get(), 0.6f));


        List<Map.Entry<ResourceKey<Species>, Species>> commonMutators = List.of(BuiltInSpecies.FOREST, BuiltInSpecies.PLAINS, BuiltInSpecies.JUNGLE, BuiltInSpecies.DESERT, BuiltInSpecies.ROCKY);
        List<Map.Entry<ResourceKey<Species>, Species>> cultivatedMutators = List.of(BuiltInSpecies.FOREST, BuiltInSpecies.PLAINS);

        for (int i = 0; i < commonMutators.size()-1; i++) {
            for (int j = i+1; j < commonMutators.size(); j++) {
                var first = commonMutators.get(i);
                var second = commonMutators.get(j);
                mutationRecipe(output, "apis/" + first.getKey().location().getPath() + "_" + second.getKey().location().getPath() + "_common", first.getKey(), second.getKey(), BuiltInSpecies.COMMON.getKey(), 0.15f);
            }
        }

        for (Map.Entry<ResourceKey<Species>, Species> entry : cultivatedMutators) {
            mutationRecipe(output, "apis/" + entry.getKey().location().getPath() + "_cultivated", entry.getKey(), BuiltInSpecies.COMMON.getKey(), BuiltInSpecies.CULTIVATED.getKey(), 0.12f);
        }

        mutationRecipe(output, "noble/noble", BuiltInSpecies.COMMON.getKey(), BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.NOBLE.getKey(), 0.10f);
        mutationRecipe(output, "noble/majestic", BuiltInSpecies.NOBLE.getKey(), BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.MAJESTIC.getKey(), 0.10f);
        mutationRecipe(output, "noble/imperial", BuiltInSpecies.MAJESTIC.getKey(), BuiltInSpecies.NOBLE.getKey(), BuiltInSpecies.IMPERIAL.getKey(), 0.08f);

        mutationRecipe(output, "desert/outcast", BuiltInSpecies.DESERT.getKey(), BuiltInSpecies.NOBLE.getKey(), BuiltInSpecies.OUTCAST.getKey(), 0.10f);
        mutationRecipe(output, "desert/bandit", BuiltInSpecies.OUTCAST.getKey(), BuiltInSpecies.DESERT.getKey(), BuiltInSpecies.BANDIT.getKey(), 0.08f);

        mutationRecipe(output, "jungle/tangle", BuiltInSpecies.JUNGLE.getKey(), BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.TANGLE.getKey(), 0.10f);
        mutationRecipe(output, "jungle/lush", BuiltInSpecies.TANGLE.getKey(), BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.LUSH.getKey(), 0.08f);

        mutationRecipe(output, "ender/surreal", BuiltInSpecies.ENDER.getKey(), BuiltInSpecies.WARPED.getKey(), BuiltInSpecies.SURREAL.getKey(), 0.10f, new DimensionCondition(ResourceLocation.tryParse("minecraft:the_end")));
        mutationRecipe(output, "ender/enigmatic", BuiltInSpecies.SURREAL.getKey(), BuiltInSpecies.INTREPID.getKey(), BuiltInSpecies.ENIGMATIC.getKey(), 0.10f, new DimensionCondition(ResourceLocation.tryParse("minecraft:the_end")));

        mutationRecipe(output, "heroic/explorer", BuiltInSpecies.JUNGLE.getKey(), BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.EXPLORER.getKey(), 0.12f);
        mutationRecipe(output, "heroic/intrepid", BuiltInSpecies.EXPLORER.getKey(), BuiltInSpecies.DILIGENT.getKey(), BuiltInSpecies.INTREPID.getKey(), 0.10f);
        mutationRecipe(output, "heroic/champion", BuiltInSpecies.INTREPID.getKey(), BuiltInSpecies.MAJESTIC.getKey(), BuiltInSpecies.CHAMPION.getKey(), 0.08f);

        mutationRecipe(output, "industrious/diligent", BuiltInSpecies.COMMON.getKey(), BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.DILIGENT.getKey(), 0.12f);
        mutationRecipe(output, "industrious/tireless", BuiltInSpecies.DILIGENT.getKey(), BuiltInSpecies.COMMON.getKey(), BuiltInSpecies.TIRELESS.getKey(), 0.10f);
        mutationRecipe(output, "industrious/industrious", BuiltInSpecies.TIRELESS.getKey(), BuiltInSpecies.DILIGENT.getKey(), BuiltInSpecies.INDUSTRIOUS.getKey(), 0.08f);

        mutationRecipe(output, "infernal/cursed", BuiltInSpecies.CRIMSON.getKey(), BuiltInSpecies.WARPED.getKey(), BuiltInSpecies.CURSED.getKey(), 0.12f, new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));
        mutationRecipe(output, "infernal/fiendish", BuiltInSpecies.CURSED.getKey(), BuiltInSpecies.CRIMSON.getKey(), BuiltInSpecies.FIENDISH.getKey(), 0.10f, new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));
        mutationRecipe(output, "infernal/devilish", BuiltInSpecies.FIENDISH.getKey(), BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.DEVILISH.getKey(), 0.08f, new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));
        mutationRecipe(output, "infernal/infernal", BuiltInSpecies.DEVILISH.getKey(), BuiltInSpecies.BANDIT.getKey(), BuiltInSpecies.INFERNAL.getKey(), 0.08f, new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));
        mutationRecipe(output, "infernal/haunted", BuiltInSpecies.CURSED.getKey(), BuiltInSpecies.WARPED.getKey(), BuiltInSpecies.HAUNTED.getKey(), 0.12f, new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH), new BlockUnderCondition(Blocks.SOUL_SAND));
        mutationRecipe(output, "infernal/ghostly", BuiltInSpecies.HAUNTED.getKey(), BuiltInSpecies.TIRELESS.getKey(), BuiltInSpecies.GHOSTLY.getKey(), 0.10f);
        mutationRecipe(output, "infernal/spectral", BuiltInSpecies.GHOSTLY.getKey(), BuiltInSpecies.DEVILISH.getKey(), BuiltInSpecies.SPECTRAL.getKey(), 0.08f);

        mutationRecipe(output, "metallic/cuprous", BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.DILIGENT.getKey(), BuiltInSpecies.CUPROUS.getKey(), 0.10f, new BlockUnderCondition(Blocks.WAXED_COPPER_BLOCK));
        mutationRecipe(output, "metallic/precious", BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.MAJESTIC.getKey(), BuiltInSpecies.PRECIOUS.getKey(), 0.10f, new BlockUnderCondition(Blocks.GOLD_BLOCK));
        mutationRecipe(output, "metallic/ferrous", BuiltInSpecies.CUPROUS.getKey(), BuiltInSpecies.PRECIOUS.getKey(), BuiltInSpecies.FERROUS.getKey(), 0.10f, new BlockUnderCondition(Blocks.IRON_BLOCK));
        mutationRecipe(output, "metallic/adamantine", BuiltInSpecies.FERROUS.getKey(), BuiltInSpecies.LUMINOUS.getKey(), BuiltInSpecies.ADAMANTINE.getKey(), 0.06f, new BlockUnderCondition(Blocks.NETHERITE_BLOCK));

        mutationRecipe(output, "mineral/bituminous", BuiltInSpecies.ROCKY.getKey(), BuiltInSpecies.DESERT.getKey(), BuiltInSpecies.BITUMINOUS.getKey(), 0.10f, new BlockUnderCondition(Blocks.COAL_BLOCK));
        mutationRecipe(output, "mineral/conductive", BuiltInSpecies.BITUMINOUS.getKey(), BuiltInSpecies.TIRELESS.getKey(), BuiltInSpecies.CONDUCTIVE.getKey(), 0.10f, new BlockUnderCondition(Blocks.REDSTONE_BLOCK));
        mutationRecipe(output, "mineral/lapic", BuiltInSpecies.BITUMINOUS.getKey(), BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.LAPIC.getKey(), 0.10f, new BlockUnderCondition(Blocks.LAPIS_BLOCK));
        mutationRecipe(output, "mineral/amethyst", BuiltInSpecies.CONDUCTIVE.getKey(), BuiltInSpecies.BITUMINOUS.getKey(), BuiltInSpecies.AMETHYST.getKey(), 0.10f, new BlockUnderCondition(Blocks.AMETHYST_BLOCK));
        mutationRecipe(output, "mineral/dimantic", BuiltInSpecies.LAPIC.getKey(), BuiltInSpecies.AMETHYST.getKey(), BuiltInSpecies.DIMANTIC.getKey(), 0.06f, new BlockUnderCondition(Blocks.DIAMOND_BLOCK));
        mutationRecipe(output, "mineral/emeradic", BuiltInSpecies.AMETHYST.getKey(), BuiltInSpecies.CONDUCTIVE.getKey(), BuiltInSpecies.EMERADIC.getKey(), 0.08f, new BlockUnderCondition(Blocks.EMERALD_BLOCK));
        mutationRecipe(output, "mineral/quartz", BuiltInSpecies.ROCKY.getKey(), BuiltInSpecies.CRIMSON.getKey(), BuiltInSpecies.QUARTZ.getKey(), 0.10f, new BlockUnderCondition(Blocks.QUARTZ_BLOCK), new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));
        mutationRecipe(output, "mineral/luminous", BuiltInSpecies.QUARTZ.getKey(), BuiltInSpecies.HAUNTED.getKey(), BuiltInSpecies.LUMINOUS.getKey(), 0.10f, new BlockUnderCondition(Blocks.GLOWSTONE), new TemperatureCondition(EnumTemperature.HELLISH, EnumTemperature.HELLISH));

        mutationRecipe(output, "necrotic/decaying", BuiltInSpecies.CURSED.getKey(), BuiltInSpecies.OUTCAST.getKey(), BuiltInSpecies.DECAYING.getKey(), 0.10f);
        mutationRecipe(output, "necrotic/rotten", BuiltInSpecies.DECAYING.getKey(), BuiltInSpecies.TANGLE.getKey(), BuiltInSpecies.ROTTEN.getKey(), 0.10f);
        mutationRecipe(output, "necrotic/necromantic", BuiltInSpecies.ROTTEN.getKey(), BuiltInSpecies.LUSH.getKey(), BuiltInSpecies.NECROMANTIC.getKey(), 0.08f, new NighttimeCondition(), new TemperatureCondition(EnumTemperature.FROZEN, EnumTemperature.COLD));

        mutationRecipe(output, "rocky/robust", BuiltInSpecies.ROCKY.getKey(), BuiltInSpecies.DILIGENT.getKey(), BuiltInSpecies.ROBUST.getKey(), 0.12f);
        mutationRecipe(output, "rocky/resilient", BuiltInSpecies.ROBUST.getKey(), BuiltInSpecies.ROCKY.getKey(), BuiltInSpecies.RESILIENT.getKey(), 0.08f);

        mutationRecipe(output, "creative/jazzy", BuiltInSpecies.CULTIVATED.getKey(), BuiltInSpecies.JUNGLE.getKey(), BuiltInSpecies.JAZZY.getKey(), 0.10f);
        mutationRecipe(output, "creative/essayist", BuiltInSpecies.JAZZY.getKey(), BuiltInSpecies.DESERT.getKey(), BuiltInSpecies.ESSAYIST.getKey(), 0.10f);
        mutationRecipe(output, "creative/tricky", BuiltInSpecies.FIENDISH.getKey(), BuiltInSpecies.ROTTEN.getKey(), BuiltInSpecies.TRICKY.getKey(), 0.10f);

        mutationRecipe(output, "terraforming/primordial", BuiltInSpecies.ENIGMATIC.getKey(), BuiltInSpecies.NECROMANTIC.getKey(), BuiltInSpecies.PRIMORDIAL.getKey(), 0.08f, new BiomeCondition(BiomeTags.IS_END));
        mutationRecipe(output, "terraforming/campestral", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.PLAINS.getKey(), BuiltInSpecies.CAMPESTRAL.getKey(), 0.12f, new BiomeCondition(Tags.Biomes.IS_PLAINS));
        mutationRecipe(output, "terraforming/sylvan", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.FOREST.getKey(), BuiltInSpecies.SYLVAN.getKey(), 0.12f, new BiomeCondition(BiomeTags.IS_FOREST));
        mutationRecipe(output, "terraforming/boreal", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.COMMON.getKey(), BuiltInSpecies.BOREAL.getKey(), 0.12f, new BiomeCondition(BiomeTags.IS_TAIGA));
        mutationRecipe(output, "terraforming/tropic", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.JUNGLE.getKey(), BuiltInSpecies.TROPIC.getKey(), 0.12f, new BiomeCondition(BiomeTags.IS_JUNGLE));
        mutationRecipe(output, "terraforming/paludal", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.DECAYING.getKey(), BuiltInSpecies.PALUDAL.getKey(), 0.12f, new BiomeCondition(Tags.Biomes.IS_SWAMP));
        mutationRecipe(output, "terraforming/gelid", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.NECROMANTIC.getKey(), BuiltInSpecies.GELID.getKey(), 0.12f, new BiomeCondition(Tags.Biomes.IS_SNOWY));
        mutationRecipe(output, "terraforming/mycelic", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.WARPED.getKey(), BuiltInSpecies.MYCELIC.getKey(), 0.12f, new BiomeCondition(Tags.Biomes.IS_MUSHROOM));
        mutationRecipe(output, "terraforming/xeric", BuiltInSpecies.PRIMORDIAL.getKey(), BuiltInSpecies.DESERT.getKey(), BuiltInSpecies.XERIC.getKey(), 0.12f, new BiomeCondition(Tags.Biomes.IS_DESERT));
    }

    protected static Ingredient combIngredient(Map.Entry<ResourceKey<Comb>, Comb> comb) {
        return DataComponentIngredient.of(false, EsotericRegistration.COMB_TYPE, comb.getKey().location(), ItemsRegistration.COMB.get());
    }

    protected static void frameRecipe(RecipeOutput output, ItemLike result, Ingredient center, Ingredient outside) {
        frameRecipe(output, result, center, outside, ItemsRegistration.APIARY.get());
    }

    protected static void mutationRecipe(RecipeOutput output, String path, ResourceKey<Species> first, ResourceKey<Species> second, ResourceKey<Species> result, float chance, IMutationCondition... conditions) {
        output.accept(ResourceLocation.fromNamespaceAndPath(MODID, "mutation/" + path), 
                new MutationRecipe(first.location(), second.location(), result.location(), chance, Arrays.stream(conditions).toList()),
                null
        );
    }

    protected static void mutatorRecipe(RecipeOutput output, String name, Ingredient input, float modifier) {
        output.accept(ResourceLocation.fromNamespaceAndPath(MODID, "mutator/" + name),
                new MutatorRecipe(input, modifier),
                null
        );
    }

    protected static void tempUnitRecipe(RecipeOutput output, String name, Ingredient input, EnumTolerance tempChange, float useChance) {
        output.accept(ResourceLocation.fromNamespaceAndPath(MODID, "temp_unit/" + name),
                new TempUnitRecipe(input, tempChange, useChance),
                null
        );
    }

    protected static void hydroregulatorRecipe(RecipeOutput output, String name, Ingredient input, Product recipeOutput, EnumTolerance humidityChange, float useChance) {
        output.accept(ResourceLocation.fromNamespaceAndPath(MODID, "hydroregulator/" + name),
                new HydroRecipe(input, recipeOutput, humidityChange, useChance),
                null
        );
    }

    protected static void honeyGeneratorRecipe(RecipeOutput output, String name, Ingredient input, int burnTime) {
        output.accept(ResourceLocation.fromNamespaceAndPath(MODID, "honey_generator/" + name),
                new HoneyGeneratorRecipe(input, burnTime),
                null
        );
    }

    protected static void centrifugeRecipe(RecipeOutput output, String name, Ingredient input, Product... outputs) {
        output.accept(ResourceLocation.fromNamespaceAndPath(MODID, "centrifuge/" + name),
                new CentrifugeRecipe(input, Arrays.stream(outputs).toList()),
                null);
    }

    protected static void frameRecipe(RecipeOutput output, ItemLike result, Ingredient center, Ingredient outside, ItemLike unlockedBy) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                .pattern("OOO")
                .pattern("OXO")
                .pattern("OOO")
                .define('O', outside)
                .define('X', center)
                .unlockedBy(getHasName(unlockedBy), has(unlockedBy))
                .save(output.withConditions(new ItemEnabledCondition(BuiltInRegistries.ITEM.getKey(result.asItem()))));
    }

    protected static void deadlyFrame(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistration.DEADLY_FRAME.get())
                .pattern("OCO")
                .pattern("OXO")
                .pattern("OOO")
                .define('O', Items.OBSIDIAN)
                .define('X', Items.SKELETON_SKULL)
                .define('C', Items.CRYING_OBSIDIAN)
                .unlockedBy("has_apiary", has(ItemsRegistration.APIARY.get()))
                .save(output.withConditions(new ItemEnabledCondition(BuiltInRegistries.ITEM.getKey(ItemsRegistration.DEADLY_FRAME.get().asItem()))));
    }

    protected static void enabledRecipe(RecipeBuilder builder, RecipeOutput output) {
        builder.save(output.withConditions(new ItemEnabledCondition(BuiltInRegistries.ITEM.getKey(builder.getResult()))));
    }

    protected static void stonecutterFor(RecipeOutput output, BlockFamily family) {
        stonecutterFor(output, family, family.getBaseBlock());
    }

    protected static void stonecutterFor(RecipeOutput output, BlockFamily family, Block base) {
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, family.get(BlockFamily.Variant.SLAB), base, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, family.get(BlockFamily.Variant.STAIRS), base);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, family.get(BlockFamily.Variant.WALL), base);
    }
}
