package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.recipe.HydroRecipe;
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

public class HydroRecipeCategory implements IRecipeCategory<HydroRecipe> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MODID, "jei/hydroregulator");
    public static final RecipeType<HydroRecipe> TYPE = new RecipeType<>(ID, HydroRecipe.class);

    private static final Component TITLE = Component.translatable("jei.complicated_bees.hydroregulator");

    public final IDrawable icon;
    public final IDrawable BACKGROUND = ComplicatedBeesJEI.createDrawable(ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/jei/two_slot.png"), 0, 0, 143, 40, 143, 40);

    public HydroRecipeCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ItemsRegistration.MELLARIUM_HYDROREGULATOR.get()));
    }

    @Override
    public RecipeType<HydroRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, HydroRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 12)
                .setSlotName("input")
                .addIngredients(recipe.getInput());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 50, 12)
                .setSlotName("output")
                .addIngredient(VanillaTypes.ITEM_STACK, recipe.getOutput().getStack())
                .addRichTooltipCallback(new ChanceTooltipCallback(recipe.getOutput().getChance()));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, HydroRecipe recipe, IFocusGroup focuses) {
        IRecipeCategory.super.createRecipeExtras(builder, recipe, focuses);
        var widget = builder.addText(Component.translatable("jei.complicated_bees.modifier", recipe.getHumidityChange().getTranslationKey()), 71, 14);
        widget.setPosition(69, 5, 71, 14, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        widget.setTextAlignment(HorizontalAlignment.CENTER);
        widget.setTextAlignment(VerticalAlignment.CENTER);
        var widget2 = builder.addText(Component.translatable("jei.complicated_bees.consumption_chance", String.format("%.0f%%", recipe.getUseChance() * 100)), 71, 14);
        widget2.setPosition(69, 22, 71, 14, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        widget2.setTextAlignment(HorizontalAlignment.CENTER);
        widget2.setTextAlignment(VerticalAlignment.CENTER);
    }
}
