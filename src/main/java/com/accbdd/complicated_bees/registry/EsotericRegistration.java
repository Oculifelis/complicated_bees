package com.accbdd.complicated_bees.registry;

import com.accbdd.complicated_bees.loot.InheritHiveCombFunction;
import com.accbdd.complicated_bees.loot.InheritHiveSpeciesFunction;
import com.accbdd.complicated_bees.recipe.CentrifugeRecipe;
import com.accbdd.complicated_bees.recipe.HydroRecipe;
import com.accbdd.complicated_bees.recipe.MutatorRecipe;
import com.accbdd.complicated_bees.recipe.TempUnitRecipe;
import com.accbdd.complicated_bees.worldgen.ComplicatedBeenestDecorator;
import com.accbdd.complicated_bees.worldgen.ComplicatedHiveFeature;
import com.accbdd.complicated_bees.worldgen.ComplicatedHiveFeatureConfiguration;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class EsotericRegistration {
    public static final DeferredRegister<LootItemFunctionType> LOOT_ITEM_FUNCTION_REGISTER = DeferredRegister.create(BuiltInRegistries.LOOT_FUNCTION_TYPE.key(), MODID);
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_REGISTER = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, MODID);
    public static final DeferredRegister<Feature<?>> FEATURE_REGISTER = DeferredRegister.create(ForgeRegistries.FEATURES, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);

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

    public static final Supplier<LootItemFunctionType> INHERIT_HIVE = LOOT_ITEM_FUNCTION_REGISTER.register("inherit_hive_species",
            () -> new LootItemFunctionType(InheritHiveSpeciesFunction.Serializer.INSTANCE));
    public static final Supplier<LootItemFunctionType> INHERIT_COMB = LOOT_ITEM_FUNCTION_REGISTER.register("inherit_hive_comb",
            () -> new LootItemFunctionType(InheritHiveCombFunction.Serializer.INSTANCE));

    public static final Supplier<TreeDecoratorType<ComplicatedBeenestDecorator>> COMPLICATED_BEENEST_DECORATOR = TREE_DECORATOR_REGISTER.register("bee_nest_decorator",
            () -> new TreeDecoratorType<>(ComplicatedBeenestDecorator.CODEC));

    public static final Supplier<ComplicatedHiveFeature> COMPLICATED_HIVE_FEATURE = FEATURE_REGISTER.register("complicated_bee_nest",
            () -> new ComplicatedHiveFeature(ComplicatedHiveFeatureConfiguration.CODEC));

    public static final RegistryObject<RecipeType<CentrifugeRecipe>> CENTRIFUGE_RECIPE = RECIPE_TYPE_REGISTER.register("centrifuge",
            () -> RecipeType.simple(new ResourceLocation(MODID, "centrifuge")));
    public static final RegistryObject<RecipeType<MutatorRecipe>> MUTATOR_RECIPE = RECIPE_TYPE_REGISTER.register("mutator",
            () -> RecipeType.simple(new ResourceLocation(MODID, "mutator")));
    public static final RegistryObject<RecipeType<TempUnitRecipe>> TEMP_UNIT_RECIPE = RECIPE_TYPE_REGISTER.register("temp_unit",
            () -> RecipeType.simple(new ResourceLocation(MODID, "temp_unit")));
    public static final RegistryObject<RecipeType<HydroRecipe>> HYDROREGULATOR_RECIPE = RECIPE_TYPE_REGISTER.register("hydroregulator",
            () -> RecipeType.simple(new ResourceLocation(MODID, "hydroregulator")));

    public static final RegistryObject<RecipeSerializer<CentrifugeRecipe>> CENTRIFUGE_RECIPE_SERIALIZER = RECIPE_SERIALIZER_REGISTER.register("centrifuge",
            () -> CentrifugeRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<MutatorRecipe>> MUTATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZER_REGISTER.register("mutator",
            () -> MutatorRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<TempUnitRecipe>> TEMP_UNIT_RECIPE_SERIALIZER = RECIPE_SERIALIZER_REGISTER.register("temp_unit",
            () -> TempUnitRecipe.SERIALIZER);
    public static final RegistryObject<RecipeSerializer<HydroRecipe>> HYDROREGULATOR_RECIPE_SERIALIZER = RECIPE_SERIALIZER_REGISTER.register("hydroregulator",
            () -> HydroRecipe.SERIALIZER);
}
