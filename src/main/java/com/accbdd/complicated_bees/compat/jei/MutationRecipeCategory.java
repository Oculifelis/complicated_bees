package com.accbdd.complicated_bees.compat.jei;

import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.recipe.mutation.MutationRecipe;
import com.accbdd.complicated_bees.recipe.mutation.condition.IMutationCondition;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MutationRecipeCategory implements IRecipeCategory<RecipeHolder<MutationRecipe>> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MODID, "jei/mutation");
    public static final RecipeType<RecipeHolder<MutationRecipe>> TYPE = RecipeType.createRecipeHolderType(ID);

    private static final Component TITLE = Component.translatable("gui.complicated_bees.jei.mutations");

    public final IDrawable ICON = ComplicatedBeesJEI.createDrawable(ResourceLocation.fromNamespaceAndPath(MODID, "textures/item/bee.png"), 0, 0, 16, 16, 16, 16);
    public final IDrawable BACKGROUND = ComplicatedBeesJEI.createDrawable(ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/jei/mutations.png"), 0, 0, 143, 40, 143, 40);

    @Override
    public RecipeType<RecipeHolder<MutationRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public void draw(RecipeHolder<MutationRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        double chance = recipe.value().getChance() * 100;
        if (BreedingTracker.CLIENT_INSTANCE != null) {
            chance += BreedingTracker.CLIENT_INSTANCE.getResearchedMutations().contains(getRegistryName(recipe)) ? ServerConfig.SERVER_CONFIG.researchBonus.get() * 100 : 0;
        }
        chance = Math.min(100, chance);
        String chanceString = recipe.value().getConditions().isEmpty() ? String.format("%.0f%%", chance) : String.format("[%.0f%%]", chance);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, chanceString, 95, 1, 0xFFFFFF);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<MutationRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tips = new ArrayList<>();
        if (mouseX >= 81 && mouseX <= 106 && mouseY >= 1 && mouseY <= 10 && !recipe.value().getConditions().isEmpty()) {
            tips.add(Component.translatable("gui.complicated_bees.mutations.has_conditions"));
            for (IMutationCondition condition : recipe.value().getConditions()) {
                tips.add(condition.getDescription());
            }
        }
        tooltip.addAll(tips);
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<MutationRecipe> mutation, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 12)
                .setSlotName("first_species")
                .addIngredients(VanillaTypes.ITEM_STACK, mutation.value().getFirstSpecies().toMembers());

        builder.addSlot(RecipeIngredientRole.INPUT, 59, 12)
                .setSlotName("second_species")
                .addIngredients(VanillaTypes.ITEM_STACK, mutation.value().getSecondSpecies().toMembers());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 115, 12)
                .setSlotName("output_species")
                .addIngredients(VanillaTypes.ITEM_STACK, mutation.value().getResultSpecies().toMembers());
    }
}
