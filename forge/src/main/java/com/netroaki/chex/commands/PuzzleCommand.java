package com.netroaki.chex.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

public class PuzzleCommand implements Command<CommandSourceStack> {
  private static final PuzzleCommand CMD = new PuzzleCommand();

  public static ArgumentBuilder<CommandSourceStack, ?> register(
      CommandDispatcher<CommandSourceStack> dispatcher) {
    return Commands.literal("puzzle")
        .requires(cs -> cs.hasPermission(2)) // Requires op/cheat permissions
        // List/solve/reset disabled pending library implementation
        .then(Commands.literal("list").executes(ctx -> 0))
        .then(
            Commands.literal("solve")
                .then(
                    Commands.argument("puzzle", StringArgumentType.string())
                        .suggests((ctx, builder) -> builder.buildFuture())
                        .executes(ctx -> 0)
                        .then(
                            Commands.argument("targets", EntityArgument.players())
                                .executes(ctx -> 0))))
        .then(
            Commands.literal("reset")
                .executes(ctx -> 0)
                .then(Commands.argument("targets", EntityArgument.players()).executes(ctx -> 0)));
  }

  // Methods for library features are disabled

  @Override
  public int run(CommandContext<CommandSourceStack> context) {
    return 0;
  }
}
