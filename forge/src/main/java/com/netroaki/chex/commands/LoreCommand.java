package com.netroaki.chex.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.netroaki.chex.capability.ILoreKnowledge;
import com.netroaki.chex.capability.LoreKnowledgeImpl;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Command for testing and managing lore fragments */
public class LoreCommand {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(
        Commands.literal("lore")
            .requires(source -> source.hasPermission(2))
            .then(
                Commands.literal("add")
                    .then(
                        Commands.argument("targets", EntityArgument.players())
                            .then(
                                Commands.argument("fragment", StringArgumentType.string())
                                    .executes(LoreCommand::addFragment))))
            .then(Commands.literal("list").executes(LoreCommand::listFragments))
            .then(Commands.literal("clear").executes(LoreCommand::clearFragments)));
  }

  private static int addFragment(CommandContext<CommandSourceStack> context)
      throws CommandSyntaxException {
    Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
    String fragmentId = StringArgumentType.getString(context, "fragment");
    ResourceLocation fragment = ResourceLocation.parse(fragmentId);

    int count = 0;
    for (ServerPlayer player : players) {
      ILoreKnowledge knowledge = LoreKnowledgeImpl.get(player);
      if (!knowledge.hasLoreFragment(fragment)) {
        knowledge.addLoreFragment(fragment);
        count++;

        // Sync to client
        knowledge.sync(player);
      }
    }
    final int finalCount = count;

    if (count > 0) {
      context
          .getSource()
          .sendSuccess(
              () ->
                  Component.literal(
                      "Added lore fragment " + fragmentId + " to " + finalCount + " players"),
              true);
    } else {
      context
          .getSource()
          .sendFailure(
              Component.literal("No players received the fragment (they may already have it)"));
    }

    return count;
  }

  private static int listFragments(CommandContext<CommandSourceStack> context)
      throws CommandSyntaxException {
    ServerPlayer player = context.getSource().getPlayerOrException();
    ILoreKnowledge knowledge = LoreKnowledgeImpl.get(player);

    context.getSource().sendSuccess(() -> Component.literal("Collected Lore Fragments:"), false);

    for (ResourceLocation fragment : knowledge.getCollectedFragments()) {
      context.getSource().sendSuccess(() -> Component.literal("- " + fragment), false);
    }

    return Command.SINGLE_SUCCESS;
  }

  private static int clearFragments(CommandContext<CommandSourceStack> context)
      throws CommandSyntaxException {
    ServerPlayer player = context.getSource().getPlayerOrException();
    ILoreKnowledge knowledge = LoreKnowledgeImpl.get(player);

    // Create a copy of the fragments to avoid concurrent modification
    int count = knowledge.getCollectedFragments().size();
    knowledge.getCollectedFragments().clear();

    // Sync to client
    knowledge.sync(player);

    context
        .getSource()
        .sendSuccess(() -> Component.literal("Cleared " + count + " lore fragments"), true);

    return count;
  }
}
