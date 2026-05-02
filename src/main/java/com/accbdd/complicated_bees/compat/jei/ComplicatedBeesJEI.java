package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.compat.jei.ingredient.*;
import com.accbdd.complicated_bees.registry.*;
import com.accbdd.complicated_bees.screen.BeeSorterScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

@JeiPlugin
public class ComplicatedBeesJEI implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new CentrifugeRecipeCategory(helper),
                new BeeProduceRecipeCategory(),
                new MutationRecipeCategory(),
                new FlowerTypeRecipeCategory(),
                new TempUnitRecipeCategory(helper),
                new MutatorRecipeCategory(helper),
                new HydroRecipeCategory(helper),
                new HoneyGeneratorRecipeCategory(helper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().getConnection().getRecipeManager();
        registration.addRecipes(CentrifugeRecipeCategory.TYPE, manager.getAllRecipesFor(EsotericRegistration.CENTRIFUGE_RECIPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(BeeProduceRecipeCategory.TYPE, GeneticHelper.getRegistryAccess().registryOrThrow(SpeciesRegistration.SPECIES_REGISTRY_KEY).stream().toList());
        registration.addRecipes(MutationRecipeCategory.TYPE, GeneticHelper.getRegistryAccess().registryOrThrow(MutationRegistration.MUTATION_REGISTRY_KEY).stream().toList());
        registration.addRecipes(FlowerTypeRecipeCategory.TYPE, GeneticHelper.getRegistryAccess().registryOrThrow(FlowerRegistration.FLOWER_REGISTRY_KEY).stream().toList());
        registration.addRecipes(TempUnitRecipeCategory.TYPE, manager.getAllRecipesFor(EsotericRegistration.TEMP_UNIT_RECIPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(MutatorRecipeCategory.TYPE, manager.getAllRecipesFor(EsotericRegistration.MUTATOR_RECIPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(HydroRecipeCategory.TYPE, manager.getAllRecipesFor(EsotericRegistration.HYDROREGULATOR_RECIPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(HoneyGeneratorRecipeCategory.TYPE, manager.getAllRecipesFor(EsotericRegistration.HONEY_GENERATOR_RECIPE.get()).stream().map(RecipeHolder::value).toList());
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        ISubtypeInterpreter<ItemStack> speciesInterpreter = new BeeSubtypeInterpreter();

        ISubtypeInterpreter<ItemStack> combInterpreter = new CombSubtypeInterpreter();

        ISubtypeInterpreter<ItemStack> nestInterpreter = new NestSubtypeInterpreter();

        registration.registerSubtypeInterpreter(ItemsRegistration.DRONE.get(), speciesInterpreter);
        registration.registerSubtypeInterpreter(ItemsRegistration.QUEEN.get(), speciesInterpreter);
        registration.registerSubtypeInterpreter(ItemsRegistration.PRINCESS.get(), speciesInterpreter);
        registration.registerSubtypeInterpreter(ItemsRegistration.COMB.get(), combInterpreter);
        registration.registerSubtypeInterpreter(ItemsRegistration.BEE_NEST.get(), nestInterpreter);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ItemsRegistration.APIARY.get().getDefaultInstance(), BeeProduceRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.APIARY.get().getDefaultInstance(), MutationRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.APIARY.get().getDefaultInstance(), FlowerTypeRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.MELLARIUM_BASE.get().getDefaultInstance(), BeeProduceRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.MELLARIUM_BASE.get().getDefaultInstance(), MutationRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.MELLARIUM_BASE.get().getDefaultInstance(), FlowerTypeRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.CENTRIFUGE.get().getDefaultInstance(), CentrifugeRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.GYROFUGE_BASE.get().getDefaultInstance(), CentrifugeRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.MELLARIUM_MUTATOR.get().getDefaultInstance(), MutatorRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.MELLARIUM_TEMP_UNIT.get().getDefaultInstance(), TempUnitRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.MELLARIUM_HYDROREGULATOR.get().getDefaultInstance(), HydroRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.HONEY_GENERATOR.get().getDefaultInstance(), HoneyGeneratorRecipeCategory.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(BeeSorterScreen.class, new BeeSorterDragDropJEI());
    }

    @Override public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(ComplicatedIngredients.BLOCK, BlockIngredientHelper.createList(), new BlockIngredientHelper(), new BlockIngredientRenderer(), Block.CODEC.codec());
        registration.register(ComplicatedIngredients.FLOWER, GeneticHelper.getRegistryAccess().registryOrThrow(FlowerRegistration.FLOWER_REGISTRY_KEY).stream().toList(), new FlowerIngredientHelper(), new FlowerIngredientRenderer(), FlowerRegistration.CODEC);
        IModPlugin.super.registerIngredients(registration);
    }

    public static IDrawable createDrawable(ResourceLocation location, int uOffset, int vOffset, int width, int height, int textureWidth, int textureHeight) {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
                guiGraphics.blit(location, xOffset, yOffset, uOffset, vOffset, getWidth(), getHeight(), textureWidth, textureHeight);
            }
        };
    }
}
