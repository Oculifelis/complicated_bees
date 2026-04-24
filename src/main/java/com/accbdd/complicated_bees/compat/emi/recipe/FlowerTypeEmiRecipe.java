package com.accbdd.complicated_bees.compat.emi.recipe;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.bees.Flower;
import com.accbdd.complicated_bees.bees.GeneticHelper;
import com.accbdd.complicated_bees.compat.emi.ComplicatedBeesEMI;
import com.accbdd.complicated_bees.compat.emi.ingredient.EmiBlock;
import com.accbdd.complicated_bees.compat.emi.ingredient.EmiFlower;
import com.accbdd.complicated_bees.registry.FlowerRegistration;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class FlowerTypeEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final List<EmiIngredient> lookups;
    private final Flower flower;
    private final EmiFlower flowerStack;

    public FlowerTypeEmiRecipe(Flower flower) {
        ResourceLocation flowerId = GeneticHelper.getRegistryAccess().registryOrThrow(FlowerRegistration.FLOWER_REGISTRY_KEY).getKey(flower);
        this.id = ResourceLocation.fromNamespaceAndPath(ComplicatedBees.MODID, "/flower_type/" + flowerId.toString().replace(":", "/"));
        this.flower = flower;
        this.flowerStack = new EmiFlower(flower);
        this.lookups = new ArrayList<>();
        lookups.addAll(flower.getAllFlowerBlocks().stream().map(EmiBlock::new).toList());
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
        return List.of(flowerStack);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(flowerStack);
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return lookups;
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

        widgets.addSlot(EmiIngredient.of(lookups), 116, 11);
    }
}
