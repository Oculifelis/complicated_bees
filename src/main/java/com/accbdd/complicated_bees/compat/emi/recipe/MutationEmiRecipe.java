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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class MutationEmiRecipe implements EmiRecipe {
    private final Mutation mutation;
    private final ResourceLocation id;
    private final List<EmiIngredient> first;
    private final List<EmiIngredient> second;
    private final List<EmiStack> result;
    private final List<EmiStack> extraResults;
    private final List<EmiIngredient> catalysts;

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
        first = List.of(
                EmiStack.of(mutation.getFirstSpecies().toStack(ItemsRegistration.DRONE.get())),
                EmiStack.of(mutation.getFirstSpecies().toStack(ItemsRegistration.PRINCESS.get()))
        );
        second = List.of(
                EmiStack.of(mutation.getSecondSpecies().toStack(ItemsRegistration.PRINCESS.get())),
                EmiStack.of(mutation.getSecondSpecies().toStack(ItemsRegistration.DRONE.get()))
        );
        result = List.of(
                EmiStack.of(mutation.getResultSpecies().toStack(ItemsRegistration.DRONE.get())).setChance(mutation.getChance()),
                EmiStack.of(mutation.getResultSpecies().toStack(ItemsRegistration.PRINCESS.get())).setChance(mutation.getChance())
        );
        extraResults = new ArrayList<>(List.of(result.get(0)));
        extraResults.add(EmiStack.of(mutation.getResultSpecies().toStack(ItemsRegistration.PRINCESS.get())));
        extraResults.add(EmiStack.of(mutation.getResultSpecies().toStack(ItemsRegistration.QUEEN.get())));
        catalysts = new ArrayList<>();
        catalysts.addAll(mutation.getFirstSpecies().toMembers().stream().map(EmiStack::of).toList());
        catalysts.addAll(mutation.getSecondSpecies().toMembers().stream().map(EmiStack::of).toList());
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
        return List.of(first.get(0), second.get(1));
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
    }

    @Override
    public List<EmiStack> getOutputs() {
        ArrayList<EmiStack> emiStacks = new ArrayList<>(extraResults);
        emiStacks.add(result.get(0));
        return emiStacks;
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
        // We use the same "unique" to ensure the slots stay synced. These ingredients may change once per interval.
        // If we wanted to guarantee a change after each interval, we could make our own cycling slot class.
        widgets.addGeneratedSlot(r -> getRandomIngredient(r, first), 1, 11, 11)
                .drawBack(false);
        widgets.addGeneratedSlot(r -> getRandomIngredient(r, second), 1, 58, 11)
                .drawBack(false);
        widgets.addGeneratedSlot(r -> getRandomIngredient(r, result), 2,114, 11)
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

    private EmiIngredient getRandomIngredient(Random random, List<? extends EmiIngredient> ingredients) {
        return ingredients.get(random.nextInt(ingredients.size()));
    }
}
