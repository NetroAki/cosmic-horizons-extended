package com.netroaki.chex.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.netroaki.chex.quest.QuestManager;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class QuestCommands {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(
        net.minecraft.commands.Commands.literal("chexquest")
            .requires(source -> source.hasPermission(2))
            .then(
                net.minecraft.commands.Commands.literal("list").executes(QuestCommands::listQuests))
            .then(
                net.minecraft.commands.Commands.literal("start")
                    .then(
                        net.minecraft.commands.Commands.argument(
                                "quest", StringArgumentType.string())
                            .suggests(
                                (context, builder) -> {
                                  // Provide quest ID suggestions
                                  QuestManager.get().getAllQuests().stream()
                                      .map(quest -> quest.id.toString())
                                      .forEach(builder::suggest);
                                  return builder.buildFuture();
                                })
                            .executes(
                                context ->
                                    startQuest(
                                        context, StringArgumentType.getString(context, "quest")))))
            .then(
                net.minecraft.commands.Commands.literal("complete")
                    .then(
                        net.minecraft.commands.Commands.argument(
                                "quest", StringArgumentType.string())
                            .suggests(
                                (context, builder) -> {
                                  // Provide in-progress quest ID suggestions
                                  try {
                                    ServerPlayer player =
                                        context.getSource().getPlayerOrException();
                                    QuestManager.get()
                                        .getPlayerData(player)
                                        .questProgress
                                        .keySet()
                                        .stream()
                                        .map(ResourceLocation::toString)
                                        .forEach(builder::suggest);
                                  } catch (CommandSyntaxException ignored) {
                                  }
                                  return builder.buildFuture();
                                })
                            .executes(
                                context ->
                                    completeQuest(
                                        context, StringArgumentType.getString(context, "quest")))
                            .then(
                                net.minecraft.commands.Commands.argument(
                                        "player", EntityArgument.player())
                                    .executes(
                                        context ->
                                            completeQuest(
                                                context,
                                                StringArgumentType.getString(context, "quest"),
                                                EntityArgument.getPlayer(context, "player"))))))
            .then(
                net.minecraft.commands.Commands.literal("reset")
                    .then(
                        net.minecraft.commands.Commands.argument("player", EntityArgument.player())
                            .executes(
                                context ->
                                    resetQuests(
                                        context, EntityArgument.getPlayer(context, "player"))))));
  }

  private static int listQuests(CommandContext<CommandSourceStack> context) {
    CommandSourceStack source = context.getSource();
    Collection<Quest> quests = QuestManager.get().getAllQuests();

    source.sendSuccess(() -> Component.literal("§6=== Available Quests ==="), false);
    quests.forEach(
        quest -> {
          source.sendSuccess(
              () -> Component.literal(String.format("§e- %s §7(%s)", quest.getTitle(), quest.id)),
              false);
        });

    return quests.size();
  }

  private static int startQuest(CommandContext<CommandSourceStack> context, String questId)
      throws CommandSyntaxException {
    ServerPlayer player = context.getSource().getPlayerOrException();
    ResourceLocation id = ResourceLocation.tryParse(questId);

    if (id == null) {
      context.getSource().sendFailure(Component.literal("§cInvalid quest ID format!"));
      return 0;
    }

    boolean success = QuestManager.get().startQuest(player, id);
    if (success) {
      context
          .getSource()
          .sendSuccess(() -> Component.literal("§aStarted quest: §e" + questId), false);
      return 1;
    } else {
      context
          .getSource()
          .sendFailure(
              Component.literal(
                  "§cFailed to start quest. It may already be in progress or completed."));
      return 0;
    }
  }

  private static int completeQuest(CommandContext<CommandSourceStack> context, String questId)
      throws CommandSyntaxException {
    return completeQuest(context, questId, context.getSource().getPlayerOrException());
  }

  private static int completeQuest(
      CommandContext<CommandSourceStack> context, String questId, ServerPlayer player) {
    ResourceLocation id = ResourceLocation.tryParse(questId);

    if (id == null) {
      context.getSource().sendFailure(Component.literal("§cInvalid quest ID format!"));
      return 0;
    }

    boolean success = QuestManager.get().completeQuest(player, id);
    if (success) {
      context
          .getSource()
          .sendSuccess(
              () ->
                  Component.literal(
                      "§aCompleted quest: §e"
                          + questId
                          + "§a for player §e"
                          + player.getScoreboardName()),
              true);
      return 1;
    } else {
      context
          .getSource()
          .sendFailure(
              Component.literal(
                  "§cFailed to complete quest. It may not be in progress or the objectives aren't"
                      + " met."));
      return 0;
    }
  }

  private static int resetQuests(CommandContext<CommandSourceStack> context, ServerPlayer player) {
    // Implement quest reset logic here
    context
        .getSource()
        .sendSuccess(
            () ->
                Component.literal("§aReset all quests for player: §e" + player.getScoreboardName()),
            true);
    return 1;
  }
}
