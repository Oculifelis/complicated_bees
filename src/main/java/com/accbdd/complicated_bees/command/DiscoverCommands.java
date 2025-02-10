package com.accbdd.complicated_bees.command;

import com.accbdd.complicated_bees.genetics.GeneticHelper;
import com.accbdd.complicated_bees.genetics.Species;
import com.accbdd.complicated_bees.genetics.mutation.Mutation;
import com.accbdd.complicated_bees.genetics.tracking.BreedingTracker;
import com.accbdd.complicated_bees.registry.MutationRegistration;
import com.accbdd.complicated_bees.registry.SpeciesRegistration;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

public class DiscoverCommands implements Command<CommandSourceStack> {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root, CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext buildContext) {
        pDispatcher.register(root
                .then(Commands.literal("tracking")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.literal("species")
                                        .then(Commands.literal("clear").executes(context -> clearSpecies(context.getSource(), EntityArgument.getEntities(context, "targets"))))
                                        .then(Commands.literal("grant")
                                                .then(Commands.argument("species", ResourceArgument.resource(buildContext, SpeciesRegistration.SPECIES_REGISTRY_KEY)).executes(context -> discoverSpecies(context.getSource(), EntityArgument.getEntities(context, "targets"), ResourceArgument.getResource(context, "species", SpeciesRegistration.SPECIES_REGISTRY_KEY).get()))))
                                ).then(Commands.literal("mutation")
                                        .then(Commands.literal("clear").executes(context -> clearMutations(context.getSource(), EntityArgument.getEntities(context, "targets"))))
                                        .then(Commands.literal("grant")
                                            .then(Commands.argument("mutation", ResourceArgument.resource(buildContext, MutationRegistration.MUTATION_REGISTRY_KEY)).executes(context -> discoverMutation(context.getSource(), EntityArgument.getEntities(context, "targets"), ResourceArgument.getResource(context, "mutation", MutationRegistration.MUTATION_REGISTRY_KEY).get()))))
                                ).then(Commands.literal("research")
                                        .then(Commands.literal("clear").executes(context -> clearResearch(context.getSource(), EntityArgument.getEntities(context, "targets"))))
                                        .then(Commands.literal("grant")
                                            .then(Commands.argument("mutation", ResourceArgument.resource(buildContext, MutationRegistration.MUTATION_REGISTRY_KEY)).executes(context -> discoverResearch(context.getSource(), EntityArgument.getEntities(context, "targets"), ResourceArgument.getResource(context, "mutation", MutationRegistration.MUTATION_REGISTRY_KEY).get()))))
                )))
        );
    }

    private static int clearResearch(CommandSourceStack source, Collection<? extends Entity> targets) throws CommandSyntaxException {
        int i = 0;
        for (Entity entity : targets) {
            if (entity instanceof Player player) {
                ++i;
                BreedingTracker.getTracker(player).clearResearch();
            }
        }
        if (i == 0)
            throw new SimpleCommandExceptionType(Component.translatable("command.complicated_bees.no_players")).create();
        else {
            int finalI = i;
            source.sendSuccess(() -> Component.translatable("command.complicated_bees.discover.research.clear", finalI), true);
        }
        return i;
    }

    private static int discoverResearch(CommandSourceStack source, Collection<? extends Entity> targets, Mutation mutation) throws CommandSyntaxException {
        int i = 0;
        for (Entity entity : targets) {
            if (entity instanceof Player player) {
                var tracker = BreedingTracker.getTracker(player);
                if (!tracker.isResearched(mutation)) {
                    ++i;
                    BreedingTracker.getTracker(player).research(mutation);
                }
            }
        }
        if (i == 0)
            throw new SimpleCommandExceptionType(Component.translatable("command.complicated_bees.discover.research.no_change")).create();
        else {
            int finalI = i;
            source.sendSuccess(() -> Component.translatable("command.complicated_bees.discover.research", MutationRegistration.getResourceLocation(mutation), finalI), true);
        }
        return i;
    }

    private static int clearMutations(CommandSourceStack source, Collection<? extends Entity> targets) throws CommandSyntaxException {
        int i = 0;
        for (Entity entity : targets) {
            if (entity instanceof Player player) {
                ++i;
                BreedingTracker.getTracker(player).clearMutations();
            }
        }
        if (i == 0)
            throw new SimpleCommandExceptionType(Component.translatable("command.complicated_bees.no_players")).create();
        else {
            int finalI = i;
            source.sendSuccess(() -> Component.translatable("command.complicated_bees.discover.mutation.clear", finalI), true);
        }
        return i;
    }

    private static int discoverMutation(CommandSourceStack source, Collection<? extends Entity> targets, Mutation mutation) throws CommandSyntaxException {
        int i = 0;
        for (Entity entity : targets) {
            if (entity instanceof Player player) {
                var tracker = BreedingTracker.getTracker(player);
                if (!tracker.isDiscovered(mutation)) {
                    ++i;
                    BreedingTracker.getTracker(player).discover(mutation);
                }
            }
        }
        if (i == 0)
            throw new SimpleCommandExceptionType(Component.translatable("command.complicated_bees.discover.mutation.no_change")).create();
        else {
            int finalI = i;
            source.sendSuccess(() -> Component.translatable("command.complicated_bees.discover.mutation", MutationRegistration.getResourceLocation(mutation), finalI), true);
        }
        return i;
    }

    private static int clearSpecies(CommandSourceStack source, Collection<? extends Entity> targets) throws CommandSyntaxException {
        int i = 0;
        for (Entity entity : targets) {
            if (entity instanceof Player player) {
                ++i;
                BreedingTracker.getTracker(player).clearSpecies();
            }
        }
        if (i == 0)
            throw new SimpleCommandExceptionType(Component.translatable("command.complicated_bees.no_players")).create();
        else {
            int finalI = i;
            source.sendSuccess(() -> Component.translatable("command.complicated_bees.discover.species.clear", finalI), true);
        }
        return i;
    }

    private static int discoverSpecies(CommandSourceStack source, Collection<? extends Entity> targets, Species species) throws CommandSyntaxException {
        int i = 0;
        for (Entity entity : targets) {
            if (entity instanceof Player player) {
                var tracker = BreedingTracker.getTracker(player);
                if (!tracker.isDiscovered(species)) {
                    ++i;
                    BreedingTracker.getTracker(player).discover(species);
                }
            }
        }
        if (i == 0)
            throw new SimpleCommandExceptionType(Component.translatable("command.complicated_bees.discover.species.no_change")).create();
        else {
            int finalI = i;
            source.sendSuccess(() -> Component.translatable("command.complicated_bees.discover.species", GeneticHelper.getTranslationKey(species), finalI), true);
        }
        return i;
    }



    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return 0;
    }
}
