package com.accbdd.complicated_bees.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        var opRoot = Commands.literal("combees").requires(context -> context.hasPermission(2));
        DiscoverCommands.register(opRoot, dispatcher, buildContext);
    }
}
