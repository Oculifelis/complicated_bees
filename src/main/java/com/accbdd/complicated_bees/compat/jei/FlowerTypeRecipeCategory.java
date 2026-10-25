package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.compat.jei.ingredient.BlockWrapper;
import com.accbdd.complicated_bees.compat.jei.ingredient.ComplicatedIngredients;
import com.accbdd.complicated_bees.util.GuiHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class FlowerTypeRecipeCategory implements IRecipeCategory<Flower> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(MODID, "jei/flower_type");
    public static final RecipeType<Flower> TYPE = new RecipeType<>(ID, Flower.class);

    private static final Component TITLE = Component.translatable("gui.complicated_bees.jei.flower_type");

    public final IDrawable ICON = ComplicatedBeesJEI.createDrawable(Identifier.parse("minecraft:textures/block/poppy.png"), 0, 0, 16, 16, 16, 16);
    public final IDrawable BACKGROUND = ComplicatedBeesJEI.createDrawable(Identifier.fromNamespaceAndPath(MODID, "textures/gui/jei/flower.png"), 0, 0, 143, 40, 143, 40);

    @Override
    public RecipeType<Flower> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

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
    public void setRecipe(IRecipeLayoutBuilder builder, Flower flower, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 117, 12)
                .setSlotName("flower_blocks")
                .addIngredients(ComplicatedIngredients.BLOCK, flower.getAllFlowerBlocks().stream().map(BlockWrapper::new).toList());

        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addIngredients(ComplicatedIngredients.FLOWER, List.of(flower));

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                .addIngredients(ComplicatedIngredients.FLOWER, List.of(flower));
    }

    @Override
    public void draw(Flower flower, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        getBackground().draw(guiGraphics);
        IRecipeCategory.super.draw(flower, recipeSlotsView, guiGraphics, mouseX, mouseY);
        GuiHelper.drawCenteredWrappedText(guiGraphics, 45, 16, 0xFFFFFF, Minecraft.getInstance().font.lineHeight + 2, 90, 5, GeneticHelper.getTranslationKey(flower));
    }
}
