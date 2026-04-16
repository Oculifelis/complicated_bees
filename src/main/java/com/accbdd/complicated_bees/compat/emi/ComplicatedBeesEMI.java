package com.accbdd.complicated_bees.compat.emi;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.compat.emi.ingredient.EmiFlower;
import com.accbdd.complicated_bees.compat.emi.recipe.*;
import com.accbdd.complicated_bees.registry.*;
import com.accbdd.complicated_bees.screen.BeeSorterScreen;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

@EmiEntrypoint
public class ComplicatedBeesEMI implements EmiPlugin {
    public static final EmiStack CENTRIFUGE = EmiStack.of(ItemsRegistration.CENTRIFUGE.get());
    public static final EmiStack GYROFUGE = EmiStack.of(ItemsRegistration.GYROFUGE_BASE.get());
    public static final EmiStack APIARY = EmiStack.of(ItemsRegistration.APIARY.get());
    public static final EmiStack MELLARIUM = EmiStack.of(ItemsRegistration.MELLARIUM_BASE.get());
    public static final EmiStack MUTATOR = EmiStack.of(ItemsRegistration.MELLARIUM_MUTATOR.get());
    public static final EmiStack TEMP_UNIT = EmiStack.of(ItemsRegistration.MELLARIUM_TEMP_UNIT.get());
    public static final EmiStack HYDROREGULATOR = EmiStack.of(ItemsRegistration.MELLARIUM_HYDROREGULATOR.get());
    public static final EmiStack HONEY_GENERATOR = EmiStack.of(ItemsRegistration.HONEY_GENERATOR.get());
    public static final EmiRecipeCategory CENTRIFUGE_CATEGORY = new ComplicatedBeesRecipeCategory("centrifuge", CENTRIFUGE, Component.translatable("gui.complicated_bees.jei.centrifuge"));
    public static final EmiRecipeCategory BEE_PRODUCE_CATEGORY = new ComplicatedBeesRecipeCategory("bee_produce", APIARY, Component.translatable("gui.complicated_bees.jei.bee_products"));
    public static final EmiRecipeCategory MUTATION_CATEGORY = new ComplicatedBeesRecipeCategory("mutation", APIARY, Component.translatable("gui.complicated_bees.jei.mutations"));
    public static final EmiRecipeCategory FLOWER_TYPE_CATEGORY = new ComplicatedBeesRecipeCategory("flower_type", EmiStack.of(Items.POPPY), Component.translatable("gui.complicated_bees.jei.flower_type"));
    public static final EmiRecipeCategory MUTATOR_CATEGORY = new ComplicatedBeesRecipeCategory("mutator", MUTATOR, Component.translatable("jei.complicated_bees.mutator"));
    public static final EmiRecipeCategory TEMP_UNIT_CATEGORY = new ComplicatedBeesRecipeCategory("temp_unit", TEMP_UNIT, Component.translatable("jei.complicated_bees.temp_unit"));
    public static final EmiRecipeCategory HYDROREGULATOR_CATEGORY = new ComplicatedBeesRecipeCategory("hydroregulator", HYDROREGULATOR, Component.translatable("jei.complicated_bees.hydroregulator"));
    public static final EmiRecipeCategory HONEY_GENERATOR_CATEGORY = new ComplicatedBeesRecipeCategory("honey_generator", HONEY_GENERATOR, Component.translatable("jei.complicated_bees.honey_generator"));
    public static final Comparison COMPARE_BEE
            = Comparison.compareData(stack -> GeneticHelper.getSpecies(stack.getItemStack(), true));

    @Override
    public void register(EmiRegistry registry) {
        registry.setDefaultComparison(ItemsRegistration.DRONE.get(), COMPARE_BEE);
        registry.setDefaultComparison(ItemsRegistration.PRINCESS.get(), COMPARE_BEE);
        registry.setDefaultComparison(ItemsRegistration.QUEEN.get(), COMPARE_BEE);
        //registry.setDefaultComparison(ItemsRegistration.COMB.get(), Comparison.compareData(s -> CombItem.getComb(s.getItemStack())));
        registry.setDefaultComparison(ItemsRegistration.BEE_NEST.get(), Comparison.compareData(s -> {
            CompoundTag blockEntityData = s.getItemStack().get(DataComponents.BLOCK_ENTITY_DATA).getUnsafe();
            if (blockEntityData == null)
                return "empty";
            return blockEntityData.getString("species");
        }));

        registry.addCategory(CENTRIFUGE_CATEGORY);
        registry.addWorkstation(CENTRIFUGE_CATEGORY, CENTRIFUGE);
        registry.addWorkstation(CENTRIFUGE_CATEGORY, GYROFUGE);
        registry.addCategory(BEE_PRODUCE_CATEGORY);
        registry.addWorkstation(BEE_PRODUCE_CATEGORY, APIARY);
        registry.addWorkstation(BEE_PRODUCE_CATEGORY, MELLARIUM);
        registry.addCategory(MUTATION_CATEGORY);
        registry.addWorkstation(MUTATION_CATEGORY, APIARY);
        registry.addWorkstation(MUTATION_CATEGORY, MELLARIUM);
        registry.addCategory(FLOWER_TYPE_CATEGORY);
        registry.addWorkstation(FLOWER_TYPE_CATEGORY, APIARY);
        registry.addWorkstation(FLOWER_TYPE_CATEGORY, MELLARIUM);
        registry.addCategory(MUTATOR_CATEGORY);
        registry.addWorkstation(MUTATOR_CATEGORY, MUTATOR);
        registry.addCategory(TEMP_UNIT_CATEGORY);
        registry.addWorkstation(TEMP_UNIT_CATEGORY, TEMP_UNIT);
        registry.addCategory(HYDROREGULATOR_CATEGORY);
        registry.addWorkstation(HYDROREGULATOR_CATEGORY, HYDROREGULATOR);
        registry.addCategory(HONEY_GENERATOR_CATEGORY);
        registry.addWorkstation(HONEY_GENERATOR_CATEGORY, HONEY_GENERATOR);

        registry.addDragDropHandler(BeeSorterScreen.class, new BeeSorterDragDropEMI());

        RecipeManager manager = registry.getRecipeManager();
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();

        manager.getAllRecipesFor(EsotericRegistration.CENTRIFUGE_RECIPE.get())
                .stream()
                .map(holder -> new CentrifugeEmiRecipe(holder.id(), holder.value()))
                .forEach(registry::addRecipe);

        manager.getAllRecipesFor(EsotericRegistration.MUTATOR_RECIPE.get())
                .stream()
                .map(holder -> new MutatorEmiRecipe(holder.id(), holder.value()))
                .forEach(registry::addRecipe);

        manager.getAllRecipesFor(EsotericRegistration.TEMP_UNIT_RECIPE.get())
                .stream()
                .map(holder -> new TempUnitEmiRecipe(holder.id(), holder.value()))
                .forEach(registry::addRecipe);

        manager.getAllRecipesFor(EsotericRegistration.HYDROREGULATOR_RECIPE.get())
                .stream()
                .map(holder -> new HydroEmiRecipe(holder.id(), holder.value()))
                .forEach(registry::addRecipe);

        manager.getAllRecipesFor(EsotericRegistration.HONEY_GENERATOR_RECIPE.get())
                .stream()
                .map(holder -> new HoneyGeneratorEmiRecipe(holder.id(), holder.value()))
                .forEach(registry::addRecipe);

        registryAccess.registryOrThrow(MutationRegistration.MUTATION_REGISTRY_KEY)
                .stream()
                .map(MutationEmiRecipe::new)
                .forEach(registry::addRecipe);

        registryAccess.registryOrThrow(SpeciesRegistration.SPECIES_REGISTRY_KEY)
                .stream()
                .map(BeeProduceEmiRecipe::new)
                .forEach(registry::addRecipe);

        registryAccess.registryOrThrow(FlowerRegistration.FLOWER_REGISTRY_KEY)
                .stream()
                .forEach(flower -> {
                    registry.addRecipe(new FlowerTypeEmiRecipe(flower));
                    registry.addEmiStack(new EmiFlower(flower));
                });
    }

    private static class ComplicatedBeesRecipeCategory extends EmiRecipeCategory {
        private final Component name;

        public ComplicatedBeesRecipeCategory(String path, EmiRenderable icon, Component name) {
            super(ResourceLocation.tryBuild(MODID, path), icon);

            this.name = name;
        }

        @Override
        public Component getName() {
            return name;
        }
    }
}
