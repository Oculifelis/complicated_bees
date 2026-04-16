package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.loot.InheritHiveCombFunction;
import com.accbdd.complicated_bees.loot.InheritHiveSpeciesFunction;
import com.accbdd.complicated_bees.recipe.*;
import com.accbdd.complicated_bees.worldgen.ComplicatedBeenestDecorator;
import com.accbdd.complicated_bees.worldgen.ComplicatedHiveFeature;
import com.accbdd.complicated_bees.worldgen.ComplicatedHiveFeatureConfiguration;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class EsotericRegistration {
    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_ITEM_FUNCTION_REGISTER = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, MODID);
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_REGISTER = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, MODID);
    public static final DeferredRegister<Feature<?>> FEATURE_REGISTER = DeferredRegister.create(Registries.FEATURE, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(Registries.PARTICLE_TYPE, MODID);

    public static EnumProperty<AssembledStatus> ASSEMBLED = EnumProperty.create("assembled", AssembledStatus.class);

    public enum AssembledStatus implements StringRepresentable {
        none,
        side,
        top;

        @Override
        public String getSerializedName() {
            return toString();
        }
    }

    public static final Supplier<SimpleParticleType> BEE_PARTICLE = PARTICLE_TYPE.register("bee",
            () -> new SimpleParticleType(true));

    public static final Supplier<LootItemFunctionType<InheritHiveSpeciesFunction>> INHERIT_HIVE = LOOT_ITEM_FUNCTION_REGISTER.register("inherit_hive_species",
            () -> new LootItemFunctionType<>(InheritHiveSpeciesFunction.Serializer.INSTANCE));
    public static final Supplier<LootItemFunctionType<InheritHiveCombFunction>> INHERIT_COMB = LOOT_ITEM_FUNCTION_REGISTER.register("inherit_hive_comb",
            () -> new LootItemFunctionType<>(InheritHiveCombFunction.Serializer.INSTANCE));

    public static final Supplier<TreeDecoratorType<ComplicatedBeenestDecorator>> COMPLICATED_BEENEST_DECORATOR = TREE_DECORATOR_REGISTER.register("bee_nest_decorator",
            () -> new TreeDecoratorType<>(ComplicatedBeenestDecorator.CODEC));

    public static final Supplier<ComplicatedHiveFeature> COMPLICATED_HIVE_FEATURE = FEATURE_REGISTER.register("complicated_bee_nest",
            () -> new ComplicatedHiveFeature(ComplicatedHiveFeatureConfiguration.CODEC));

    public static final DeferredHolder<RecipeType<?>, RecipeType<CentrifugeRecipe>> CENTRIFUGE_RECIPE = RECIPE_TYPE_REGISTER.register("centrifuge",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "centrifuge")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<MutatorRecipe>> MUTATOR_RECIPE = RECIPE_TYPE_REGISTER.register("mutator",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "mutator")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<TempUnitRecipe>> TEMP_UNIT_RECIPE = RECIPE_TYPE_REGISTER.register("temp_unit",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "temp_unit")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<HydroRecipe>> HYDROREGULATOR_RECIPE = RECIPE_TYPE_REGISTER.register("hydroregulator",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "hydroregulator")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<HoneyGeneratorRecipe>> HONEY_GENERATOR_RECIPE = RECIPE_TYPE_REGISTER.register("honey_generator",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "honey_generator")));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CentrifugeRecipe>> CENTRIFUGE_RECIPE_SERIALIZER = RECIPE_SERIALIZER_REGISTER.register("centrifuge",
            () -> CentrifugeRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MutatorRecipe>> MUTATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZER_REGISTER.register("mutator",
            () -> MutatorRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TempUnitRecipe>> TEMP_UNIT_RECIPE_SERIALIZER = RECIPE_SERIALIZER_REGISTER.register("temp_unit",
            () -> TempUnitRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HydroRecipe>> HYDROREGULATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZER_REGISTER.register("hydroregulator",
            () -> HydroRecipe.SERIALIZER);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HoneyGeneratorRecipe>> HONEY_GENERATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZER_REGISTER.register("honey_generator",
            () -> HoneyGeneratorRecipe.SERIALIZER);
}
