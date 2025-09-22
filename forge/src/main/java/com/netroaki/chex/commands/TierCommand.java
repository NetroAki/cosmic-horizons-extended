package com.netroaki.chex.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.capabilities.PlayerTierCapability;
import com.netroaki.chex.network.CHEXNetwork;
import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/** Command for managing player tiers and milestones */
public class TierCommand {
  private static final SuggestionProvider<CommandSourceStack> SUGGEST_MILESTONES =
      (context, builder) -> {
        builder.suggest("first_launch");
        builder.suggest("first_planet");
        builder.suggest("tier5_rocket");
        builder.suggest("tier10_rocket");
        builder.suggest("tier5_suit");
        builder.suggest("all_planets");
        builder.suggest("all_minerals");
        return builder.buildFuture();
      };

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(
        Commands.literal("chex:tier")
            .requires(source -> source.hasPermission(0))
            .then(
                Commands.literal("info")
                    .executes(TierCommand::showInfo)
                    .then(
                        Commands.argument("player", EntityArgument.player())
                            .requires(source -> source.hasPermission(2))
                            .executes(TierCommand::showInfoOther)))
            .then(
                Commands.literal("set")
                    .requires(source -> source.hasPermission(2))
                    .then(
                        Commands.argument("player", EntityArgument.player())
                            .then(
                                Commands.argument("tier", IntegerArgumentType.integer(1, 10))
                                    .executes(TierCommand::setTier))))
            .then(
                Commands.literal("milestone")
                    .requires(source -> source.hasPermission(2))
                    .then(
                        Commands.argument("player", EntityArgument.players())
                            .then(
                                Commands.literal("grant")
                                    .then(
                                        Commands.argument("milestone", StringArgumentType.word())
                                            .suggests(SUGGEST_MILESTONES)
                                            .executes(TierCommand::grantMilestone)))
                            .then(
                                Commands.literal("revoke")
                                    .then(
                                        Commands.argument("milestone", StringArgumentType.word())
                                            .suggests(SUGGEST_MILESTONES)
                                            .executes(TierCommand::revokeMilestone)))))
            .then(
                Commands.literal("reset")
                    .requires(source -> source.hasPermission(2))
                    .then(
                        Commands.argument("player", EntityArgument.players())
                            .executes(TierCommand::resetTier))));
  }

  private static int showInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    ServerPlayer player = context.getSource().getPlayerOrException();
    return showInfoForPlayer(context, player);
  }

  private static int showInfoOther(CommandContext<CommandSourceStack> context)
      throws CommandSyntaxException {
    ServerPlayer target = EntityArgument.getPlayer(context, "player");
    return showInfoForPlayer(context, target);
  }

  private static int showInfoForPlayer(CommandContext<CommandSourceStack> context, ServerPlayer player) {
    player.getCapability(PlayerTierCapability.INSTANCE).ifPresent(capability -> {
      CommandSourceStack source = context.getSource();
      
      // Basic info
      source.sendSuccess(() -> Component.literal("§6=== " + player.getDisplayName().getString() + "'s Tier Info ==="), false);
      source.sendSuccess(() -> Component.literal("§7Rocket Tier: §fT" + capability.getRocketTier()), false);
      source.sendSuccess(() -> Component.literal("§7Suit Tier: §fT" + capability.getSuitTier()), false);
      
      // Unlocked planets count
      int planetCount = capability.getUnlockedPlanets().size();
      source.sendSuccess(() -> Component.literal("§7Unlocked Planets: §f" + planetCount), false);
      
      // Discovered minerals count
      int mineralCount = capability.getDiscoveredMinerals().size();
      source.sendSuccess(() -> Component.literal("§7Discovered Minerals: §f" + mineralCount), false);
      
      // Active milestones
      source.sendSuccess(() -> Component.literal("§7Milestones:"), false);
      if (capability.hasMilestone(PlayerTierCapability.MILESTONE_FIRST_LAUNCH)) {
        source.sendSuccess(() -> Component.literal("  §a✓ First Launch"), false);
      }
      if (capability.hasMilestone(PlayerTierCapability.MILESTONE_FIRST_PLANET)) {
        source.sendSuccess(() -> Component.literal("  §a✓ First Planet Visit"), false);
      }
      if (capability.hasMilestone(PlayerTierCapability.MILESTONE_TIER_5_ROCKET)) {
        source.sendSuccess(() -> Component.literal("  §a✓ Reached Rocket Tier 5"), false);
      }
      if (capability.hasMilestone(PlayerTierCapability.MILESTONE_TIER_10_ROCKET)) {
        source.sendSuccess(() -> Component.literal("  §a✓ Reached Rocket Tier 10"), false);
      }
      if (capability.hasMilestone(PlayerTierCapability.MILESTONE_TIER_5_SUIT)) {
        source.sendSuccess(() -> Component.literal("  §a✓ Reached Suit Tier 5"), false);
      }
      if (capability.hasMilestone(PlayerTierCapability.MILESTONE_ALL_PLANETS)) {
        source.sendSuccess(() -> Component.literal("  §6★ Discovered All Planets"), false);
      }
      if (capability.hasMilestone(PlayerTierCapability.MILESTONE_ALL_MINERALS)) {
        source.sendSuccess(() -> Component.literal("  §6★ Discovered All Minerals"), false);
      }
    });
    return 1;
  }

  private static int setTier(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    ServerPlayer target = EntityArgument.getPlayer(context, "player");
    int tier = IntegerArgumentType.getInteger(context, "tier");
    
    target.getCapability(PlayerTierCapability.INSTANCE).ifPresent(capability -> {
      capability.setRocketTier(tier);
      capability.setSuitTier(Math.min(tier, 5)); // Suit tier caps at 5
      
      // Sync to client
      if (!target.level().isClientSide) {
        CHEXNetwork.sendPlayerTierToClient(target);
      }
      
      // Notify
      context.getSource().sendSuccess(
          () -> Component.literal("§aSet " + target.getDisplayName().getString() + "'s tiers to Rocket T" + tier + ", Suit T" + Math.min(tier, 5)),
          true);
      target.displayClientMessage(
          Component.literal("§aYour tiers have been updated to Rocket T" + tier + ", Suit T" + Math.min(tier, 5)),
          false);
    });
    
    return 1;
  }

  private static int grantMilestone(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "player");
    String milestoneName = StringArgumentType.getString(context, "milestone");
    int milestone = getMilestoneFromName(milestoneName);
    
    if (milestone == -1) {
      context.getSource().sendFailure(Component.literal("§cInvalid milestone: " + milestoneName));
      return 0;
    }
    
    int count = 0;
    for (ServerPlayer target : targets) {
      target.getCapability(PlayerTierCapability.INSTANCE).ifPresent(capability -> {
        capability.setMilestone(milestone);
        
        // Sync to client
        if (!target.level().isClientSide) {
          CHEXNetwork.sendPlayerTierToClient(target);
        }
      });
      count++;
    }
    
    context.getSource().sendSuccess(
        () -> Component.literal("§aGranted milestone '" + milestoneName + "' to " + count + " player(s)"),
        true);
    
    return count;
  }
  
  private static int revokeMilestone(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "player");
    String milestoneName = StringArgumentType.getString(context, "milestone");
    int milestone = getMilestoneFromName(milestoneName);
    
    if (milestone == -1) {
      context.getSource().sendFailure(Component.literal("§cInvalid milestone: " + milestoneName));
      return 0;
    }
    
    int count = 0;
    for (ServerPlayer target : targets) {
      target.getCapability(PlayerTierCapability.INSTANCE).ifPresent(capability -> {
        capability.clearMilestone(milestone);
        
        // Sync to client
        if (!target.level().isClientSide) {
          CHEXNetwork.sendPlayerTierToClient(target);
        }
      });
      count++;
    }
    
    context.getSource().sendSuccess(
        () -> Component.literal("§aRevoked milestone '" + milestoneName + "' from " + count + " player(s)"),
        true);
    
    return count;
  }
  
  private static int resetTier(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "player");
    
    for (ServerPlayer target : targets) {
      target.getCapability(PlayerTierCapability.INSTANCE).ifPresent(capability -> {
        capability.resetProgression();
        
        // Sync to client
        if (!target.level().isClientSide) {
          CHEXNetwork.sendPlayerTierToClient(target);
        }
        
        // Notify
        target.displayClientMessage(Component.literal("§eYour progression has been reset to defaults"), false);
      });
    }
    
    context.getSource().sendSuccess(
        () -> Component.literal("§aReset progression for " + targets.size() + " player(s)"),
        true);
    
    return targets.size();
  }
  
  private static int getMilestoneFromName(String name) {
    return switch (name.toLowerCase()) {
      case "first_launch" -> PlayerTierCapability.MILESTONE_FIRST_LAUNCH;
      case "first_planet" -> PlayerTierCapability.MILESTONE_FIRST_PLANET;
      case "tier5_rocket" -> PlayerTierCapability.MILESTONE_TIER_5_ROCKET;
      case "tier10_rocket" -> PlayerTierCapability.MILESTONE_TIER_10_ROCKET;
      case "tier5_suit" -> PlayerTierCapability.MILESTONE_TIER_5_SUIT;
      case "all_planets" -> PlayerTierCapability.MILESTONE_ALL_PLANETS;
      case "all_minerals" -> PlayerTierCapability.MILESTONE_ALL_MINERALS;
      default -> -1;
    };
  }
}
