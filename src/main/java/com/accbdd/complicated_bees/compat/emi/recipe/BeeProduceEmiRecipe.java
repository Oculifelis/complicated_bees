package com.accbdd.complicated_bees.compat.emi.recipe;

import com.accbdd.complicated_bees.ComplicatedBees;
import com.accbdd.complicated_bees.bees.Species;
import com.accbdd.complicated_bees.compat.emi.ComplicatedBeesEMI;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeProduceEmiRecipe implements EmiRecipe {
    private final ResourceLocation id;
    private final EmiIngredient beeInput;
    private final List<EmiStack> products;
    private final List<EmiStack> specialtyProducts;
    private final List<EmiIngredient> catalysts;

    public BeeProduceEmiRecipe(Species species) {
        ResourceLocation speciesId = Minecraft.getInstance().level.registryAccess().registryOrThrow(SpeciesRegistration.SPECIES_REGISTRY_KEY).getKey(species);
        this.id = ResourceLocation.fromNamespaceAndPath(ComplicatedBees.MODID, "/bee_produce/" + speciesId.toString().replace(":", "/"));

        this.beeInput = EmiStack.of(species.toStack(ItemsRegistration.QUEEN.get()));

        this.products = species.getProducts().stream().map(p -> {
            EmiStack test = EmiStack.of(p.getStack()).setChance(p.getChance());
            if (p.getStack().is(ItemsRegistration.COMB.get()))
                return test.comparison(Comparison.compareNbt());
            return test;
        }).toList();
        this.specialtyProducts = species.getSpecialtyProducts().stream().map(p -> {
            EmiStack test = EmiStack.of(p.getStack()).setChance(p.getChance());
            if (p.getStack().is(ItemsRegistration.COMB.get()))
                return test.comparison(Comparison.compareNbt());
            return test;
        }).toList();
        catalysts = new ArrayList<>();
        catalysts.add(EmiIngredient.of(Ingredient.of(
                species.toStack(ItemsRegistration.PRINCESS.get()),
                species.toStack(ItemsRegistration.DRONE.get()))));
        catalysts.add(EmiIngredient.of(Ingredient.of(
                species.toStack(ItemsRegistration.DRONE.get()),
                species.toStack(ItemsRegistration.PRINCESS.get()))));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ComplicatedBeesEMI.BEE_PRODUCE_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(beeInput);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return Stream.concat(products.stream(), specialtyProducts.stream()).toList();
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
    }

    @Override
    public int getDisplayWidth() {
        return 160;
    }

    @Override
    public int getDisplayHeight() {
        return 64;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/jei/bee_products.png"), 0, 0, 160, 64, 0, 0, 160, 64, 160, 64);

        widgets.addSlot(beeInput, 13, 23)
                .drawBack(false);

        for (int i = 0; i < products.size(); i++) {
            widgets.addSlot(products.get(i), 65 + 18 * i, 13)
                    .recipeContext(this);
        }
        for (int i = 0; i < specialtyProducts.size(); i++) {
            widgets.addSlot(specialtyProducts.get(i), 65 + 18 * i, 34)
                    .recipeContext(this);
        }
    }
}
