package com.accbdd.complicated_bees.compat.emi.recipe;

import com.accbdd.complicated_bees.compat.emi.ComplicatedBeesEMI;
import com.accbdd.complicated_bees.recipe.MutatorRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MutatorEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final EmiIngredient input;
    private final float mutationModifier;

    public MutatorEmiRecipe(MutatorRecipe recipe) {
        id = recipe.getId();
        input = EmiStack.of(recipe.getInput());
        mutationModifier = recipe.getMutationModifier();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ComplicatedBeesEMI.MUTATOR_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(EmiStack.EMPTY);
    }

    @Override
    public int getDisplayWidth() {
        return 143;
    }

    @Override
    public int getDisplayHeight() {
        return 40;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(new ResourceLocation(MODID, "textures/gui/jei/single_slot.png"), 0, 0, 143, 40, 0, 0);
        widgets.addSlot(input, 12, 12);

        var widget = widgets.addText(Component.translatable("jei.complicated_bees.modifier", mutationModifier + "x"), 99, 20, 0xFFFFFF, true);
        widget.horizontalAlign(TextWidget.Alignment.CENTER);
        widget.verticalAlign(TextWidget.Alignment.CENTER);
    }
}
