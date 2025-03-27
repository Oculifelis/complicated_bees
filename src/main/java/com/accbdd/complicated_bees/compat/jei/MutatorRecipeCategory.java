package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.recipe.MutatorRecipe;
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

public class MutatorRecipeCategory implements IRecipeCategory<MutatorRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(MODID, "jei/mutator");
    public static final RecipeType<MutatorRecipe> TYPE = new RecipeType<>(ID, MutatorRecipe.class);

    private static final Component TITLE = Component.translatable("jei.complicated_bees.mutator");

    public final IDrawable icon;
    public final IDrawable BACKGROUND = ComplicatedBeesJEI.createDrawable(new ResourceLocation(MODID, "textures/gui/jei/single_slot.png"), 0, 0, 143, 40, 143, 40);

    public MutatorRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemsRegistration.MELLARIUM_MUTATOR.get()));
    }

    @Override
    public RecipeType<MutatorRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, MutatorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 12)
                .setSlotName("input")
                .addIngredient(VanillaTypes.ITEM_STACK, recipe.getInput().getDefaultInstance());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, MutatorRecipe recipe, IFocusGroup focuses) {
        IRecipeCategory.super.createRecipeExtras(builder, recipe, focuses);
        var widget = builder.addText(Component.translatable("jei.complicated_bees.modifier", recipe.getMutationModifier() + "x"), 81, 14);
        widget.setPosition(59, 5, 81, 31, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        widget.setTextAlignment(HorizontalAlignment.CENTER);
        widget.setTextAlignment(VerticalAlignment.CENTER);
    }
}
