package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.bees.gene.GeneFlower;
import com.accbdd.complicated_bees.compat.jei.ingredient.ComplicatedIngredients;
import com.accbdd.complicated_bees.registry.FlowerRegistration;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.accbdd.complicated_bees.ComplicatedBees.LOGGER;
import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeProduceRecipeCategory implements IRecipeCategory<Species> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MODID, "jei/bee_product");
    public static final RecipeType<Species> TYPE = new RecipeType<>(ID, Species.class);

    private static final Component TITLE = Component.translatable("gui.complicated_bees.jei.bee_products");

    public final IDrawable ICON = ComplicatedBeesJEI.createDrawable(ResourceLocation.fromNamespaceAndPath(MODID, "textures/item/bee.png"), 0, 0, 16, 16, 16, 16);
    public final IDrawable BACKGROUND = ComplicatedBeesJEI.createDrawable(ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/jei/bee_products.png"), 0, 0, 160, 64, 160, 64);

    @Override
    public RecipeType<Species> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public IDrawable getBackground() {
        return BACKGROUND;
    }

    @Override
    public IDrawable getIcon() {
        return ICON;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Species species, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 14, 24)
                .setSlotName("input_species")
                .addIngredients(VanillaTypes.ITEM_STACK, species.toMembers());

        builder.addSlot(RecipeIngredientRole.INPUT, 14, 45)
                .setSlotName("flower")
                .addIngredients(ComplicatedIngredients.BLOCK, getFlowers(species));

        List<Product> products = species.getProducts();
        List<Product> specProducts = species.getSpecialtyProducts();

        for (int i = 0; i < products.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 66 + (18 * i), 14)
                    .setSlotName("output_" + i)
                    .addIngredient(VanillaTypes.ITEM_STACK, products.get(i).getStack())
                    .addRichTooltipCallback(new ChanceTooltipCallback(products.get(i).getChance()));
        }

        for (int i = 0; i < specProducts.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 66 + (18 * i), 35)
                    .setSlotName("specialty_output_" + i)
                    .addIngredient(VanillaTypes.ITEM_STACK, specProducts.get(i).getStack())
                    .addRichTooltipCallback(new ChanceTooltipCallback(specProducts.get(i).getChance()));
        }
    }

    List<Block> getFlowers(Species species) {
        Level level =Minecraft.getInstance().level;
        if (level != null) {
            Flower flower = level.registryAccess().registry(FlowerRegistration.FLOWER_REGISTRY_KEY).orElseThrow()
                    .get(((GeneFlower) GeneticHelper.getGene(species.toMembers().get(0), GeneFlower.ID, true)).get());
            if (flower != null) {
                ArrayList<Block> flowers = new ArrayList<>();
                flower.getBlocksAsResourceLocs().stream().map(
                   rl -> level.registryAccess().registry(Registries.BLOCK).orElseThrow().get(rl)
                ).filter(Objects::nonNull).forEach(flowers::add);

                flower.getFlowerTags().forEach(
                       key -> {
                           level.registryAccess().registry(Registries.BLOCK).orElseThrow().stream().filter(
                                   block -> block.defaultBlockState().is(key)
                           )
                       .forEach(flowers::add);
                       }
                );

                if (!flowers.isEmpty()) {

                    return flowers;
                }
            }
        }

        LOGGER.error("No valid flower found for: {} \n" +
                "This is an error from the {} JEI plugin and might not affect gameplay.",
                species.toMembers().get(0).getDisplayName().getString(),
                MODID);

        return List.of();
    }
}
