package com.accbdd.complicated_bees.compat.emi.recipe;

import com.accbdd.complicated_bees.bees.gene.enums.EnumTolerance;
import com.accbdd.complicated_bees.compat.emi.ComplicatedBeesEMI;
import com.accbdd.complicated_bees.recipe.HydroRecipe;
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

public class HydroEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final EmiIngredient input;
    private final EnumTolerance humidityModifier;
    private final float consumeChance;

    public HydroEmiRecipe(HydroRecipe recipe) {
        id = recipe.getId();
        input = EmiIngredient.of(recipe.getInput());
        humidityModifier = recipe.getHumidityChange();
        consumeChance = recipe.getUseChance();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ComplicatedBeesEMI.HYDROREGULATOR_CATEGORY;
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

        var widget = widgets.addText(Component.translatable("jei.complicated_bees.modifier", humidityModifier.getTranslationKey()), 99, 12, 0xFFFFFF, true);
        widget.horizontalAlign(TextWidget.Alignment.CENTER);
        widget.verticalAlign(TextWidget.Alignment.CENTER);

        var widget2 = widgets.addText(Component.translatable("jei.complicated_bees.consumption_chance", String.format("%.0f%%", consumeChance * 100)), 99, 29, 0xFFFFFF, true);
        widget2.horizontalAlign(TextWidget.Alignment.CENTER);
        widget2.verticalAlign(TextWidget.Alignment.CENTER);
    }
}
