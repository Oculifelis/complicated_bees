package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.recipe.HoneyGeneratorRecipe;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class HoneyGeneratorRecipeCategory implements IRecipeCategory<HoneyGeneratorRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(MODID, "jei/honey_generator");
    public static final RecipeType<HoneyGeneratorRecipe> TYPE = new RecipeType<>(ID, HoneyGeneratorRecipe.class);

    private static final Component TITLE = Component.translatable("jei.complicated_bees.honey_generator");

    public final IDrawable icon;
    public final IDrawable BACKGROUND = ComplicatedBeesJEI.createDrawable(new ResourceLocation(MODID, "textures/gui/jei/single_slot.png"), 0, 0, 143, 40, 143, 40);

    public HoneyGeneratorRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemsRegistration.HONEY_GENERATOR.get()));
    }

    @Override
    public RecipeType<HoneyGeneratorRecipe> getRecipeType() {
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
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HoneyGeneratorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 12)
                .setSlotName("input")
                .addIngredients(recipe.getInput());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, HoneyGeneratorRecipe recipe, IFocusGroup focuses) {
        IRecipeCategory.super.createRecipeExtras(builder, recipe, focuses);
        var widget = builder.addText(Component.translatable("jei.complicated_bees.burn_time", recipe.getBurnTime()), 81, 14);
        widget.setPosition(59, 20, 81, 14, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        widget.setTextAlignment(HorizontalAlignment.CENTER);
        widget.setTextAlignment(VerticalAlignment.CENTER);
    }
}
