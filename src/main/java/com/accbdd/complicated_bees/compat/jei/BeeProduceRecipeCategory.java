package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.bees.Product;
import com.accbdd.complicated_bees.bees.Species;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeProduceRecipeCategory implements IRecipeCategory<Species> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(MODID, "jei/bee_product");
    public static final RecipeType<Species> TYPE = new RecipeType<>(ID, Species.class);

    private static final Component TITLE = Component.translatable("gui.complicated_bees.jei.bee_products");

    public final IDrawable ICON = ComplicatedBeesJEI.createDrawable(Identifier.fromNamespaceAndPath(MODID, "textures/item/bee.png"), 0, 0, 16, 16, 16, 16);
    public final IDrawable BACKGROUND = ComplicatedBeesJEI.createDrawable(Identifier.fromNamespaceAndPath(MODID, "textures/gui/jei/bee_products.png"), 0, 0, 160, 64, 160, 64);

    @Override
    public RecipeType<Species> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }


    public void draw(Species recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView slots, net.minecraft.client.gui.GuiGraphicsExtractor graphics, double mouseX, double mouseY){getBackground().draw(graphics);}
    public int getWidth(){return getBackground().getWidth();}
    public int getHeight(){return getBackground().getHeight();}
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
}
