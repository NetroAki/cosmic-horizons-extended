package com.netroaki.chex.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.netroaki.chex.capabilities.PlayerTierCapability;
import com.netroaki.chex.registry.PlanetRegistry;
import com.netroaki.chex.registry.RocketTiers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ChexCommands {

        public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
                dispatcher.register(Commands.literal("chex")
                                .then(Commands.literal("test")
                                                .executes(ChexCommands::testCommand))
                                .then(Commands.literal("unlock")
                                                .requires(source -> source.hasPermission(2)) // OP only
                                                .then(Commands.literal("rocket")
                                                                .then(Commands.argument("tier",
                                                                                IntegerArgumentType.integer(1, 10))
                                                                                .then(Commands.argument("player",
                                                                                                EntityArgument.player())
                                                                                                .executes(ChexCommands::unlockRocketTier))))
                                                .then(Commands.literal("suit")
                                                                .then(Commands.argument("tier",
                                                                                IntegerArgumentType.integer(1, 5))
                                                                                .then(Commands.argument("player",
                                                                                                EntityArgument.player())
                                                                                                .executes(ChexCommands::unlockSuitTier))))
                                                .then(Commands.literal("planet")
                                                                .then(Commands.argument("planet",
                                                                                com.mojang.brigadier.arguments.StringArgumentType
                                                                                                .string())
                                                                                .then(Commands.argument("player",
                                                                                                EntityArgument.player())
                                                                                                .executes(ChexCommands::unlockPlanet)))))
                                .then(Commands.literal("status")
                                                .then(Commands.argument("player", EntityArgument.player())
                                                                .executes(ChexCommands::showPlayerStatus)))
                                .then(Commands.literal("dumpPlanets")
                                                .executes(ChexCommands::dumpPlanets))
                                .then(Commands.literal("reload")
                                                .requires(source -> source.hasPermission(2))
                                                .executes(ChexCommands::reloadConfigs))
                                .then(Commands.literal("travel")
                                                .then(Commands.argument("tier", IntegerArgumentType.integer(1, 10))
                                                                .executes(ChexCommands::listReachablePlanets)))
                                .then(Commands.literal("minerals")
                                                .then(Commands.argument("planet",
                                                                com.mojang.brigadier.arguments.StringArgumentType
                                                                                .string())
                                                                .executes(ChexCommands::dumpMinerals))));
        }

        private static int testCommand(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();
                source.sendSuccess(() -> Component.literal("CHEX mod is working!"), false);
                return 1;
        }

        private static int unlockRocketTier(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                CommandSourceStack source = context.getSource();
                int tier = IntegerArgumentType.getInteger(context, "tier");
                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
                if (capability != null) {
                        boolean success = capability.unlockRocketTier(tier);
                        if (success) {
                                source.sendSuccess(() -> Component.literal("§aUnlocked rocket tier T" + tier + " for "
                                                + player.getDisplayName().getString()), true);
                                player.displayClientMessage(
                                                Component.literal("§aYou've unlocked rocket tier T" + tier + "!"),
                                                false);
                        } else {
                                source.sendSuccess(
                                                () -> Component.literal("§cFailed to unlock rocket tier T" + tier
                                                                + " for " + player.getDisplayName().getString()),
                                                false);
                        }
                } else {
                        source.sendSuccess(() -> Component.literal("§cFailed to access player capability"), false);
                }
                return 1;
        }

        private static int unlockSuitTier(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                CommandSourceStack source = context.getSource();
                int tier = IntegerArgumentType.getInteger(context, "tier");
                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
                if (capability != null) {
                        boolean success = capability.unlockSuitTier(tier);
                        if (success) {
                                source.sendSuccess(() -> Component.literal("§aUnlocked suit tier T" + tier + " for "
                                                + player.getDisplayName().getString()), true);
                                player.displayClientMessage(
                                                Component.literal("§aYou've unlocked suit tier T" + tier + "!"), false);
                        } else {
                                source.sendSuccess(
                                                () -> Component.literal("§cFailed to unlock suit tier T" + tier
                                                                + " for " + player.getDisplayName().getString()),
                                                false);
                        }
                } else {
                        source.sendSuccess(() -> Component.literal("§cFailed to access player capability"), false);
                }
                return 1;
        }

        private static int unlockPlanet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                CommandSourceStack source = context.getSource();
                String planetId = com.mojang.brigadier.arguments.StringArgumentType.getString(context, "planet");
                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
                if (capability != null) {
                        capability.unlockPlanet(planetId);
                        source.sendSuccess(() -> Component.literal("§aUnlocked planet " + planetId + " for "
                                        + player.getDisplayName().getString()), true);
                        player.displayClientMessage(Component.literal("§aYou've discovered planet " + planetId + "!"),
                                        false);
                } else {
                        source.sendSuccess(() -> Component.literal("§cFailed to access player capability"), false);
                }
                return 1;
        }

        private static int showPlayerStatus(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                CommandSourceStack source = context.getSource();
                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
                if (capability != null) {
                        source.sendSuccess(() -> Component
                                        .literal("§6=== " + player.getDisplayName().getString() + "'s CHEX Status ==="),
                                        false);
                        source.sendSuccess(() -> Component.literal("§eRocket Tier: T" + capability.getRocketTier()),
                                        false);
                        source.sendSuccess(() -> Component.literal("§eSuit Tier: T" + capability.getSuitTier()), false);
                        source.sendSuccess(() -> Component
                                        .literal("§eUnlocked Planets: " + capability.getUnlockedPlanets().size()),
                                        false);
                        source.sendSuccess(() -> Component
                                        .literal("§eDiscovered Minerals: " + capability.getDiscoveredMinerals().size()),
                                        false);

                        // Show accessible planets
                        var accessiblePlanets = PlanetRegistry.getAllPlanets().values().stream()
                                        .filter(planet -> capability.canAccessRocketTier(planet.requiredRocketTier()))
                                        .toList();
                        source.sendSuccess(() -> Component.literal("§eAccessible Planets: " + accessiblePlanets.size()
                                        + "/" + PlanetRegistry.getAllPlanets().size()), false);
                } else {
                        source.sendSuccess(() -> Component.literal("§cFailed to access player capability"), false);
                }
                return 1;
        }

        private static int dumpPlanets(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();

                source.sendSuccess(() -> Component.literal("§6=== CHEX Planet Registry ==="), false);
                source.sendSuccess(() -> Component.literal("§eTotal Planets: " + PlanetRegistry.getAllPlanets().size()),
                                false);

                PlanetRegistry.getAllPlanets().forEach((id, planet) -> {
                        source.sendSuccess(() -> Component.literal(String.format("§7- %s: T%d rocket, %s suit, %s",
                                        id, planet.requiredRocketTier().getTier(), planet.requiredSuitTag(),
                                        planet.biomeType())), false);
                });

                return 1;
        }

        private static int reloadConfigs(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();

                // Reload planet registry
                PlanetRegistry.initialize();

                source.sendSuccess(() -> Component.literal("§aCHEX configs reloaded successfully!"), true);
                return 1;
        }

        private static int listReachablePlanets(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();
                int tier = IntegerArgumentType.getInteger(context, "tier");

                source.sendSuccess(() -> Component.literal("§6=== Planets reachable with T" + tier + " rocket ==="),
                                false);

                var reachablePlanets = PlanetRegistry.getAllPlanets().values().stream()
                                .filter(planet -> planet.requiredRocketTier().getTier() <= tier)
                                .toList();

                if (reachablePlanets.isEmpty()) {
                        source.sendSuccess(() -> Component.literal("§cNo planets reachable with T" + tier + " rocket"),
                                        false);
                } else {
                        reachablePlanets.forEach(planet -> {
                                source.sendSuccess(() -> Component.literal(String.format("§7- %s: T%d rocket, %s suit",
                                                planet.id(), planet.requiredRocketTier().getTier(),
                                                planet.requiredSuitTag())), false);
                        });
                }

                return 1;
        }

        private static int dumpMinerals(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();
                String planetId = com.mojang.brigadier.arguments.StringArgumentType.getString(context, "planet");

                var mineralData = com.netroaki.chex.gt.GregTechBridge.getMineralDataForPlanet(planetId);

                source.sendSuccess(() -> Component.literal("§6=== Minerals on " + planetId + " ==="), false);

                if (mineralData.isEmpty()) {
                        source.sendSuccess(() -> Component.literal("§cNo mineral data found for planet: " + planetId),
                                        false);
                } else {
                        mineralData.forEach((mineral, data) -> {
                                source.sendSuccess(() -> Component.literal(String.format("§7- %s: %s", mineral, data)),
                                                false);
                        });
                }

                return 1;
        }
}