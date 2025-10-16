package com.accbdd.complicated_bees.compat.emi.recipe;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.compat.emi.ComplicatedBeesEMI;
import com.accbdd.complicated_bees.compat.emi.ingredient.EmiBlock;
import com.accbdd.complicated_bees.compat.emi.ingredient.EmiFlowerBlocks;
import com.accbdd.complicated_bees.registry.FlowerRegistration;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class FlowerTypeEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final List<EmiIngredient> flowerBlocks;
    private final List<EmiIngredient> catalysts;
    private final Flower flower;

    public FlowerTypeEmiRecipe(Flower flower) {
        ResourceLocation flowerId = Minecraft.getInstance().level.registryAccess().registryOrThrow(FlowerRegistration.FLOWER_REGISTRY_KEY).getKey(flower);
        this.id = ResourceLocation.fromNamespaceAndPath(ComplicatedBees.MODID, "/flower_type/" + flowerId.toString().replace(":", "/"));
        this.flower = flower;
        this.catalysts = new ArrayList<>(List.of(ComplicatedBeesEMI.APIARY, ComplicatedBeesEMI.MELLARIUM));
        this.flowerBlocks = flower.getAllFlowerBlocks().stream().map(block -> (EmiIngredient)new EmiBlock(block)).toList();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ComplicatedBeesEMI.FLOWER_TYPE_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return flowerBlocks;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
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
        widgets.addTexture(ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/jei/flower.png"), 0, 0, 143, 40, 0, 0, 143, 40, 143, 40);

        //todo: add custom wrapped text widget
        widgets.addText(GeneticHelper.getTranslationKey(flower), 45, 20, 0xFFFFFF, true)
                .verticalAlign(TextWidget.Alignment.CENTER)
                .horizontalAlign(TextWidget.Alignment.CENTER);

        widgets.addSlot(new EmiFlowerBlocks(flower), 116, 11);
    }
}
