package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.bees.Comb;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.gene.GeneSpecies;
import com.accbdd.complicated_bees.compat.jei.ingredient.BlockRenderer;
import com.accbdd.complicated_bees.compat.jei.ingredient.ComplicatedIngredients;
import com.accbdd.complicated_bees.compat.jei.ingredient.BlockHelper;
import com.accbdd.complicated_bees.compat.jei.ingredient.BlockListFactory;
import com.accbdd.complicated_bees.item.CombItem;
import com.accbdd.complicated_bees.registry.EsotericRegistration;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.registry.MutationRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import com.accbdd.complicated_bees.screen.BeeSorterScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

@JeiPlugin
public class ComplicatedBeesJEI implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.tryBuild(MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new CentrifugeRecipeCategory(helper),
                new BeeProduceRecipeCategory(),
                new MutationRecipeCategory(),
                new TempUnitRecipeCategory(helper),
                new MutatorRecipeCategory(helper),
                new HydroRecipeCategory(helper),
                new HoneyGeneratorRecipeCategory(helper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().getConnection().getRecipeManager();
        registration.addRecipes(CentrifugeRecipeCategory.TYPE, manager.getAllRecipesFor(EsotericRegistration.CENTRIFUGE_RECIPE.get()));
        registration.addRecipes(BeeProduceRecipeCategory.TYPE, Minecraft.getInstance().getConnection().registryAccess().registry(SpeciesRegistration.SPECIES_REGISTRY_KEY).get().stream().toList());
        registration.addRecipes(MutationRecipeCategory.TYPE, Minecraft.getInstance().getConnection().registryAccess().registry(MutationRegistration.MUTATION_REGISTRY_KEY).get().stream().toList());
        registration.addRecipes(TempUnitRecipeCategory.TYPE, manager.getAllRecipesFor(EsotericRegistration.TEMP_UNIT_RECIPE.get()));
        registration.addRecipes(MutatorRecipeCategory.TYPE, manager.getAllRecipesFor(EsotericRegistration.MUTATOR_RECIPE.get()));
        registration.addRecipes(HydroRecipeCategory.TYPE, manager.getAllRecipesFor(EsotericRegistration.HYDROREGULATOR_RECIPE.get()));
        registration.addRecipes(HoneyGeneratorRecipeCategory.TYPE, manager.getAllRecipesFor(EsotericRegistration.HONEY_GENERATOR_RECIPE.get()));
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        IIngredientSubtypeInterpreter<ItemStack> speciesInterpreter = (stack, context) -> {
            Lazy<Species> species = Lazy.of(() -> ((GeneSpecies) GeneticHelper.getChromosome(stack, true).getGene(GeneSpecies.ID)).get());
            ResourceLocation key = GeneticHelper.getRegistryAccess().registry(SpeciesRegistration.SPECIES_REGISTRY_KEY).get().getKey(species.get());
            if (key == null) {
                return "invalid";
            }
            return key.toString();
        };

        IIngredientSubtypeInterpreter<ItemStack> combInterpreter = (stack, context) -> {
            Lazy<Comb> comb = Lazy.of(() -> CombItem.getComb(stack));
            return comb.get() == null ? Comb.NULL.toString() : comb.get().toString();
        };

        IIngredientSubtypeInterpreter<ItemStack> nestInterpreter = (stack, context) -> stack.getOrCreateTag().getCompound("BlockEntityTag").getString("species");

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
        registration.addRecipeCatalyst(ItemsRegistration.MELLARIUM_BASE.get().getDefaultInstance(), BeeProduceRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ItemsRegistration.MELLARIUM_BASE.get().getDefaultInstance(), MutationRecipeCategory.TYPE);
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

    /**
     * Register special ingredients, beyond the basic ItemStack and FluidStack.
     *
     * @param registration
     */
    @Override public void registerIngredients(IModIngredientRegistration registration) {
        BlockHelper helper = new BlockHelper();

        registration.register(ComplicatedIngredients.BLOCK, BlockListFactory.create(helper), helper, new BlockRenderer());
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
