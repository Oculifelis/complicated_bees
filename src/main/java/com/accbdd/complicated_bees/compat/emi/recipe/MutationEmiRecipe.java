package com.accbdd.complicated_bees.compat.emi.recipe;

import com.accbdd.complicated_bees.bees.mutation.Mutation;
import com.accbdd.complicated_bees.bees.mutation.condition.IMutationCondition;
import com.accbdd.complicated_bees.bees.tracking.BreedingTracker;
import com.accbdd.complicated_bees.compat.emi.ComplicatedBeesEMI;
import com.accbdd.complicated_bees.config.ServerConfig;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import com.accbdd.complicated_bees.registry.MutationRegistration;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MutationEmiRecipe implements EmiRecipe {
    private final Mutation mutation;
    private final ResourceLocation id;
    private final EmiIngredient first;
    private final EmiIngredient second;
    private final EmiIngredient result;
    private final EmiIngredient lookups;

    public MutationEmiRecipe(Mutation mutation) {
        this.mutation = mutation;

        id = ResourceLocation.tryBuild(MODID,
                "/mutation/first/" +
                        mutation.getFirst().toString().replace(":", "/") +
                        "/second/" +
                        mutation.getSecond().toString().replace(":", "/") +
                        "/result/" +
                        mutation.getResult().toString().replace(":", "/")
        );
        first = EmiIngredient.of(Ingredient.of(
                mutation.getFirstSpecies().toStack(ItemsRegistration.DRONE.get()),
                mutation.getFirstSpecies().toStack(ItemsRegistration.PRINCESS.get())
        ));
        second = EmiIngredient.of(Ingredient.of(
                mutation.getSecondSpecies().toStack(ItemsRegistration.PRINCESS.get()),
                mutation.getSecondSpecies().toStack(ItemsRegistration.DRONE.get())
        ));
        result = EmiIngredient.of(Ingredient.of(
                mutation.getResultSpecies().toStack(ItemsRegistration.DRONE.get()),
                mutation.getResultSpecies().toStack(ItemsRegistration.PRINCESS.get())
        ));
        lookups = EmiIngredient.of(Ingredient.of(
                mutation.getFirstSpecies().toStack(ItemsRegistration.QUEEN.get()),
                mutation.getSecondSpecies().toStack(ItemsRegistration.QUEEN.get()),
                mutation.getResultSpecies().toStack(ItemsRegistration.QUEEN.get())
        ));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return ComplicatedBeesEMI.MUTATION_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(first, second);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return result.getEmiStacks();
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(lookups);
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
        widgets.addTexture(ResourceLocation.tryBuild(MODID, "textures/gui/jei/mutations.png"), 0, 0, 143, 40, 0, 0, 143, 40, 143, 40);
        widgets.addSlot(first, 11, 11)
                .drawBack(false);
        widgets.addSlot(second, 58, 11)
                .drawBack(false);
        widgets.addSlot(result, 114, 11)
                .drawBack(false)
                .recipeContext(this);

        float chance = mutation.getChance() * 100;
        if (BreedingTracker.CLIENT_INSTANCE != null) {
            chance += BreedingTracker.CLIENT_INSTANCE.getResearchedMutations().contains(MutationRegistration.getResourceLocation(mutation)) ? ServerConfig.SERVER_CONFIG.researchBonus.get() * 100 : 0;
        }

        String chanceString = mutation.getConditions().isEmpty() ? String.format("%.0f%%", chance) : String.format("[%.0f%%]", chance);
        widgets.addText(Component.literal(chanceString), 95, 1, -1, true)
                .horizontalAlign(TextWidget.Alignment.CENTER);

        if (!mutation.getConditions().isEmpty()) {
            List<Component> tips = new ArrayList<>();
            tips.add(Component.translatable("gui.complicated_bees.mutations.has_conditions"));
            for (IMutationCondition condition : mutation.getConditions()) {
                tips.add(condition.getDescription());
            }
            widgets.addTooltipText(tips, 81, 1, 106 - 81, 10 - 1);
        }
    }
}
