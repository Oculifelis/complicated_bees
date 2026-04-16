package com.accbdd.complicated_bees.compat.emi.recipe;

import com.accbdd.complicated_bees.compat.emi.ComplicatedBeesEMI;
import com.accbdd.complicated_bees.recipe.HoneyGeneratorRecipe;
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

public class HoneyGeneratorEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final EmiIngredient input;
    private final int burnTime;

    public HoneyGeneratorEmiRecipe(ResourceLocation id, HoneyGeneratorRecipe recipe) {
        this.id = id;
        input = EmiIngredient.of(recipe.getInput());
        burnTime = recipe.getBurnTime();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ComplicatedBeesEMI.HONEY_GENERATOR_CATEGORY;
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
        widgets.addTexture(ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/jei/single_slot.png"), 0, 0, 143, 40, 0, 0, 143, 40, 143, 40);
        widgets.addSlot(input, 11, 11);

        var widget = widgets.addText(Component.translatable("jei.complicated_bees.burn_time", burnTime), 99, 20, 0xFFFFFF, true);
        widget.horizontalAlign(TextWidget.Alignment.CENTER);
        widget.verticalAlign(TextWidget.Alignment.CENTER);
    }
}