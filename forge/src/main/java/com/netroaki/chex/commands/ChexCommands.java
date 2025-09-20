package com.netroaki.chex.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.netroaki.chex.capabilities.PlayerTierCapability;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.netroaki.chex.registry.PlanetDef;
import com.netroaki.chex.registry.PlanetRegistry;
import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.travel.TravelGraph;
import com.netroaki.chex.registry.FuelRegistry;
import com.netroaki.chex.hooks.LaunchHooks;
import com.netroaki.chex.debug.LagProfiler;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;

public class ChexCommands {

        private static final SuggestionProvider<CommandSourceStack> PLANET_SUGGESTIONS = (context, builder) -> {
                var server = context.getSource().getServer();
                if (server != null) {
                        var dimRegistryOpt = server.registryAccess().registry(Registries.DIMENSION);
                        if (dimRegistryOpt.isPresent()) {
                                var dimRegistry = dimRegistryOpt.get();
                                var existing = PlanetRegistry.getAllPlanets().keySet().stream()
                                                .filter(id -> dimRegistry.containsKey(
                                                                ResourceKey.create(Registries.DIMENSION, id)))
                                                .toList();
                                return SharedSuggestionProvider.suggestResource(existing.stream(), builder);
                        }
                }
                return SharedSuggestionProvider.suggestResource(PlanetRegistry.getAllPlanets().keySet(), builder);
        };

        public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
                dispatcher.register(Commands.literal("chex")
                                .then(Commands.literal("test")
                                                .executes(ChexCommands::testCommand))
                                .then(Commands.literal("teleport")
                                                .then(Commands.argument("planet", ResourceLocationArgument.id())
                                                                .suggests(PLANET_SUGGESTIONS)
                                                                .executes(ChexCommands::teleportToPlanet)))
                                .then(Commands.literal("launch")
                                                .then(Commands.argument("planet", ResourceLocationArgument.id())
                                                                .suggests(PLANET_SUGGESTIONS)
                                                                .executes(ChexCommands::launchToPlanet)))
                                .then(Commands.literal("lagprofiler")
                                                .then(Commands.literal("start")
                                                                .executes(ctx -> {
                                                                        boolean started = LagProfiler.start(20);
                                                                        ctx.getSource().sendSuccess(
                                                                                        () -> Component.literal(started
                                                                                                        ? "§aLagProfiler started"
                                                                                                        : "§eLagProfiler already running"),
                                                                                        false);
                                                                        return started ? 1 : 0;
                                                                }))
                                                .then(Commands.literal("stop")
                                                                .executes(ctx -> {
                                                                        ServerPlayer sp = ctx.getSource().getPlayer();
                                                                        boolean stopped = LagProfiler.stop(sp);
                                                                        ctx.getSource().sendSuccess(
                                                                                        () -> Component.literal(stopped
                                                                                                        ? "§aLagProfiler stopped"
                                                                                                        : "§eLagProfiler not running"),
                                                                                        false);
                                                                        return stopped ? 1 : 0;
                                                                }))
                                                .then(Commands.literal("status")
                                                                .executes(ctx -> {
                                                                        ctx.getSource().sendSuccess(() -> Component
                                                                                        .literal("§6LagProfiler: "
                                                                                                        + LagProfiler.status()),
                                                                                        false);
                                                                        return 1;
                                                                })))
                                .then(Commands.literal("unlock")
                                                .requires(source -> source.hasPermission(2)) // OP only
                                                .then(Commands.literal("rocket")
                                                                .then(Commands.argument("tier",
                                                                                IntegerArgumentType.integer(1, 10))
                                                                                .then(Commands.argument("player",
                                                                                                EntityArgument.player())
                                                                                                .executes(ChexCommands::unlockRocketTier))))
                                                .then(Commands.literal("nodule")
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
                                                                .executes(ChexCommands::dumpMinerals)))
                                .then(Commands.literal("getTier")
                                                .executes(ChexCommands::getTier))
                                .then(Commands.literal("setTier")
                                                .requires(source -> source.hasPermission(2))
                                                .then(Commands.argument("tier", IntegerArgumentType.integer(1, 10))
                                                                .then(Commands.argument("player",
                                                                                EntityArgument.player())
                                                                                .executes(ChexCommands::setTier))))
                                .then(Commands.literal("simulateTier")
                                                .requires(source -> source.hasPermission(2))
                                                .then(Commands.argument("tier", IntegerArgumentType.integer(1, 10))
                                                                .executes(ChexCommands::simulateTier)))
                                .then(Commands.literal("fuel")
                                                .then(Commands.argument("tier", IntegerArgumentType.integer(1, 10))
                                                                .executes(ChexCommands::showFuelInfo)))
                                .then(Commands.literal("travelGraph")
                                                .executes(ChexCommands::showTravelGraph))
                                .then(Commands.literal("dimensions").executes(ChexCommands::dumpDimensions)));
                // Admin/audit utilities
                dispatcher.register(Commands.literal("chex_audit_cosmic_data")
                                .requires(src -> src.hasPermission(2))
                                .executes(ChexCommands::auditCosmicData));
                dispatcher.register(Commands.literal("chex_snapshot_config")
                                .requires(src -> src.hasPermission(2))
                                .executes(ChexCommands::snapshotEffectiveConfig));
                // Place an arc scenery block at player's feet for testing
                dispatcher.register(Commands.literal("chex_place_arc").executes(ctx -> {
                        ServerPlayer sp = ctx.getSource().getPlayerOrException();
                        var pos = sp.blockPosition();
                        var level = sp.serverLevel();
                        var block = com.netroaki.chex.registry.blocks.CHEXBlocks.ARC_SCENERY.get();
                        boolean placed = level.setBlockAndUpdate(pos, block.defaultBlockState());
                        if (placed) {
                                ctx.getSource().sendSuccess(
                                                () -> net.minecraft.network.chat.Component
                                                                .literal("Placed ArcScenery at " + pos.toShortString()),
                                                false);
                                return 1;
                        }
                        ctx.getSource().sendFailure(
                                        net.minecraft.network.chat.Component.literal("Failed to place ArcScenery"));
                        return 0;
                }));
                // Quick teleport for debugging: /chex_tp_aurelia
                dispatcher.register(Commands.literal("chex_tp_aurelia").executes(ctx -> {
                        ServerPlayer sp = ctx.getSource().getPlayerOrException();
                        net.minecraft.server.level.ServerLevel level = sp.server
                                        .getLevel(net.minecraft.resources.ResourceKey.create(
                                                        net.minecraft.core.registries.Registries.DIMENSION,
                                                        com.netroaki.chex.CHEX.id("aurelia_ringworld")));
                        if (level == null) {
                                ctx.getSource().sendFailure(Component.literal("Aurelia dimension not found"));
                                return 0;
                        }
                        sp.teleportTo(level, 0.5, 200.0, 0.5, sp.getYRot(), sp.getXRot());
                        ctx.getSource().sendSuccess(() -> Component.literal("Teleported to Aurelia"), false);
                        return 1;
                }));
        }

        private static int teleportToPlanet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                CommandSourceStack source = context.getSource();
                ServerPlayer player = source.getPlayerOrException();
                ResourceLocation targetId = ResourceLocationArgument.getId(context, "planet");

                // Validate planet exists in our registry
                if (!PlanetRegistry.getAllPlanets().containsKey(targetId)) {
                        source.sendFailure(Component.literal("Unknown planet: " + targetId));
                        return 0;
                }

                // Dimension key assumed to match planet id
                ResourceKey<net.minecraft.world.level.Level> dimKey = ResourceKey.create(Registries.DIMENSION,
                                targetId);
                ServerLevel targetLevel = player.server.getLevel(dimKey);
                if (targetLevel == null) {
                        source.sendFailure(Component.literal("Dimension not found for planet: " + targetId));
                        return 0;
                }

                BlockPos spawn = targetLevel.getSharedSpawnPos();
                double tx = spawn.getX() + 0.5;
                double ty = Math.max(spawn.getY(), targetLevel.getMinBuildHeight() + 2);
                double tz = spawn.getZ() + 0.5;

                player.teleportTo(targetLevel, tx, ty, tz, player.getYRot(), player.getXRot());
                source.sendSuccess(() -> Component.literal("Teleported to " + targetId), false);
                return 1;
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
                                source.sendSuccess(() -> Component.literal("§aUnlocked nodule tier T" + tier + " for "
                                                + player.getDisplayName().getString()), true);
                                player.displayClientMessage(
                                                Component.literal("§aYou've unlocked nodule tier T" + tier + "!"),
                                                false);
                        } else {
                                source.sendSuccess(
                                                () -> Component.literal("§cFailed to unlock nodule tier T" + tier
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
                        source.sendSuccess(() -> Component.literal("§eNodule Tier: T" + capability.getRocketTier()),
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
                                        .filter(planet -> capability.canAccessNoduleTier(planet.requiredRocketTier()))
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
                        source.sendSuccess(() -> Component.literal(String.format("§7- %s: T%d nodule, %s suit, %s",
                                        id, planet.requiredRocketTier().getTier(), planet.requiredSuitTag(),
                                        planet.biomeType())), false);
                });

                // Also write JSON dump to run/chex_planets_dump.json
                try {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonObject root = new JsonObject();
                        root.addProperty("count", PlanetRegistry.getAllPlanets().size());
                        JsonArray arr = new JsonArray();
                        PlanetRegistry.getAllPlanets().forEach((id, planet) -> {
                                JsonObject o = new JsonObject();
                                o.addProperty("id", id.toString());
                                o.addProperty("name", planet.name());
                                o.addProperty("requiredNoduleTier", planet.requiredRocketTier().getTier());
                                o.addProperty("requiredSuitTag", planet.requiredSuitTag());
                                o.addProperty("biomeType", planet.biomeType());
                                o.addProperty("source", id.getNamespace().equals(com.netroaki.chex.CHEX.MOD_ID) ? "chex"
                                                : "cosmos");
                                o.addProperty("requiresOxygen", planet.requiresOxygen());
                                o.addProperty("hasAtmosphere", planet.hasAtmosphere());
                                o.addProperty("gravityLevel", planet.gravityLevel());
                                arr.add(o);
                        });
                        root.add("planets", arr);

                        java.nio.file.Path outDir = java.nio.file.Paths.get("run");
                        java.nio.file.Files.createDirectories(outDir);
                        java.nio.file.Path out = outDir.resolve("chex_planets_dump.json");
                        java.nio.file.Files.writeString(out, gson.toJson(root));
                        source.sendSuccess(() -> Component.literal("§aWrote planet dump to " + out.toString()), false);
                } catch (Exception e) {
                        source.sendFailure(Component.literal("Failed to write planet dump: " + e.getMessage()));
                }

                return 1;
        }

        private static int reloadConfigs(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();

                com.netroaki.chex.config.ConfigReloader.reloadAll();
                source.sendSuccess(() -> Component.literal("§aCHEX configs reloaded successfully!"), true);
                return 1;
        }

        private static int listReachablePlanets(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();
                int tier = IntegerArgumentType.getInteger(context, "tier");

                source.sendSuccess(() -> Component.literal("§6=== Planets reachable with T" + tier + " nodule ==="),
                                false);

                var reachablePlanets = PlanetRegistry.getAllPlanets().values().stream()
                                .filter(planet -> planet.requiredRocketTier().getTier() <= tier)
                                .toList();

                if (reachablePlanets.isEmpty()) {
                        source.sendSuccess(() -> Component.literal("§cNo planets reachable with T" + tier + " nodule"),
                                        false);
                } else {
                        reachablePlanets.forEach(planet -> {
                                source.sendSuccess(() -> Component.literal(String.format("§7- %s: T%d nodule, %s suit",
                                                planet.id(), planet.requiredRocketTier().getTier(),
                                                planet.requiredSuitTag())), false);
                        });
                }

                return 1;
        }

        private static int dumpMinerals(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();
                String planetId = com.mojang.brigadier.arguments.StringArgumentType.getString(context, "planet");

                var mineralData = com.netroaki.chex.CHEX.gt().getMineralDataForPlanet(planetId);

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

        private static int getTier(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                CommandSourceStack source = context.getSource();
                ServerPlayer player = source.getPlayerOrException();

                PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
                if (capability != null) {
                        source.sendSuccess(() -> Component.literal("§6Your CHEX Status:"), false);
                        source.sendSuccess(() -> Component.literal("§eNodule Tier: T" + capability.getRocketTier()),
                                        false);
                        source.sendSuccess(() -> Component.literal("§eSuit Tier: T" + capability.getSuitTier()), false);
                        source.sendSuccess(() -> Component
                                        .literal("§eUnlocked Planets: " + capability.getUnlockedPlanets().size()),
                                        false);
                        source.sendSuccess(() -> Component
                                        .literal("§eDiscovered Minerals: " + capability.getDiscoveredMinerals().size()),
                                        false);
                } else {
                        source.sendSuccess(() -> Component.literal("§cFailed to access player capability"), false);
                }
                return 1;
        }

        private static int setTier(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                CommandSourceStack source = context.getSource();
                int tier = IntegerArgumentType.getInteger(context, "tier");
                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                PlayerTierCapability capability = player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
                if (capability != null) {
                        capability.setRocketTier(tier);
                        source.sendSuccess(() -> Component.literal(
                                        "§aSet " + player.getDisplayName().getString() + "'s nodule tier to T" + tier),
                                        true);
                        player.displayClientMessage(
                                        Component.literal("§aYour nodule tier has been set to T" + tier + "!"), false);
                } else {
                        source.sendSuccess(() -> Component.literal("§cFailed to access player capability"), false);
                }
                return 1;
        }

        private static int simulateTier(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                CommandSourceStack source = context.getSource();
                int tier = IntegerArgumentType.getInteger(context, "tier");

                source.sendSuccess(() -> Component.literal("§6Simulating T" + tier + " nodule capabilities:"), false);

                // Show accessible planets
                var accessiblePlanets = TravelGraph.getPlanetsForTier(tier);
                source.sendSuccess(() -> Component.literal("§eAccessible Planets: " + accessiblePlanets.size()), false);

                accessiblePlanets.forEach(planet -> {
                        String sourceMod = TravelGraph.getPlanetSource(planet);
                        source.sendSuccess(() -> Component.literal("§7- " + planet + " (" + sourceMod + ")"), false);
                });

                // Show fuel requirements
                var fuelReq = FuelRegistry.getFuelRequirement(tier);
                if (fuelReq.isPresent()) {
                        int volume = FuelRegistry.getFuelVolume(tier);
                        source.sendSuccess(() -> Component.literal(
                                        "§eRequired Fuel: " + fuelReq.get().getFluidId() + " (" + volume + "mB)"),
                                        false);
                }

                return 1;
        }

        private static int showFuelInfo(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();
                int tier = IntegerArgumentType.getInteger(context, "tier");

                source.sendSuccess(() -> Component.literal("§6=== T" + tier + " Nodule Fuel Information ==="), false);

                var fuelReq = FuelRegistry.getFuelRequirement(tier);
                if (fuelReq.isPresent()) {
                        int volume = FuelRegistry.getFuelVolume(tier);
                        source.sendSuccess(() -> Component.literal("§eFuel Type: " + fuelReq.get().getFluidId()),
                                        false);
                        source.sendSuccess(() -> Component.literal("§eRequired Volume: " + volume + "mB"), false);
                        source.sendSuccess(
                                        () -> Component.literal("§eFluid: "
                                                        + fuelReq.get().getFluid().getFluidType().getDescriptionId()),
                                        false);
                } else {
                        source.sendSuccess(() -> Component.literal("§cNo fuel requirement found for T" + tier), false);
                }

                return 1;
        }

        private static int showTravelGraph(CommandContext<CommandSourceStack> context) {
                CommandSourceStack source = context.getSource();

                source.sendSuccess(() -> Component.literal("§6=== CHEX Travel Graph ==="), false);
                source.sendSuccess(() -> Component.literal(TravelGraph.getTravelGraphSummary()), false);

                return 1;
        }

        private static int dumpDimensions(CommandContext<CommandSourceStack> context) {
                var source = context.getSource();
                var server = source.getServer();
                if (server == null) {
                        source.sendFailure(Component.literal("Server not available"));
                        return 0;
                }
                var dimRegistryOpt = server.registryAccess().registry(Registries.DIMENSION);
                if (dimRegistryOpt.isEmpty()) {
                        source.sendFailure(Component.literal("Dimension registry not available"));
                        return 0;
                }
                var dimRegistry = dimRegistryOpt.get();
                source.sendSuccess(() -> Component.literal("§6=== Existing CHEX Dimensions ==="), false);
                dimRegistry.registryKeySet().stream()
                                .filter(k -> k.location().getNamespace().equals(com.netroaki.chex.CHEX.MOD_ID))
                                .forEach(k -> source.sendSuccess(() -> Component.literal("§7- " + k.location()),
                                                false));

                var missing = PlanetRegistry.getAllPlanets().keySet().stream()
                                .filter(id -> !dimRegistry.containsKey(ResourceKey.create(Registries.DIMENSION, id)))
                                .toList();
                if (!missing.isEmpty()) {
                        source.sendSuccess(() -> Component.literal("§ePlanets with missing dimensions:"), false);
                        missing.forEach(id -> source.sendSuccess(() -> Component.literal("§c- " + id), false));
                } else {
                        source.sendSuccess(() -> Component.literal("§aAll planet dimensions present."), false);
                }
                return 1;
        }

        private static int auditCosmicData(CommandContext<CommandSourceStack> context) {
                var source = context.getSource();
                var server = source.getServer();
                if (server == null) {
                        source.sendFailure(Component.literal("Server not available"));
                        return 0;
                }
                var rm = server.getResourceManager();
                java.util.List<ResourceLocation> resources = rm
                                .listResources("cosmic_data", rl -> rl.getPath().endsWith(".json")).keySet().stream()
                                .toList();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject report = new JsonObject();
                report.addProperty("count", resources.size());
                JsonArray bad = new JsonArray();
                for (ResourceLocation rl : resources) {
                        try {
                                var res = rm.getResource(rl);
                                if (res.isEmpty())
                                        continue;
                                try (var is = res.get().open()) {
                                        var reader = new java.io.InputStreamReader(is);
                                        var obj = gson.fromJson(reader, JsonObject.class);
                                        boolean hasAttached = obj.has("attached_dimention_id")
                                                        || obj.has("attached_dimension_id");
                                        boolean hasGui = obj.has("gui") || obj.has("gui_links") || obj.has("gui_link");
                                        if (!hasAttached || !hasGui) {
                                                JsonObject entry = new JsonObject();
                                                entry.addProperty("id", rl.toString());
                                                entry.addProperty("missing_attached_dimension", !hasAttached);
                                                entry.addProperty("missing_gui_links", !hasGui);
                                                bad.add(entry);
                                        }
                                }
                        } catch (Exception ignored) {
                        }
                }
                report.add("issues", bad);
                try {
                        java.nio.file.Path outDir = java.nio.file.Paths.get("run");
                        java.nio.file.Files.createDirectories(outDir);
                        java.nio.file.Path out = outDir.resolve("chex_cosmic_data_audit.json");
                        java.nio.file.Files.writeString(out, gson.toJson(report));
                        source.sendSuccess(() -> Component.literal("§aWrote cosmic data audit to " + out), false);
                } catch (Exception e) {
                        source.sendFailure(Component.literal("Failed to write audit: " + e.getMessage()));
                }
                source.sendSuccess(() -> Component
                                .literal("§6Cosmic data resources: " + resources.size() + ", issues: " + bad.size()),
                                false);
                return 1;
        }

        private static int snapshotEffectiveConfig(CommandContext<CommandSourceStack> context) {
                var source = context.getSource();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject root = new JsonObject();
                // Behavior
                JsonObject behavior = new JsonObject();
                behavior.addProperty("suitBounceBack", com.netroaki.chex.config.CHEXConfig.suitBounceBack());
                behavior.addProperty("enableTerraBlenderOverlay",
                                com.netroaki.chex.config.CHEXConfig.enableTerraBlenderOverlay());
                behavior.addProperty("acceptHigherTierFuel",
                                com.netroaki.chex.config.CHEXConfig.acceptHigherTierFuel());
                behavior.addProperty("radiationTickInterval",
                                com.netroaki.chex.config.CHEXConfig.radiationTickInterval());
                behavior.addProperty("radiationDamageBase", com.netroaki.chex.config.CHEXConfig.radiationDamageBase());
                behavior.addProperty("radiationDamagePerAmplifier",
                                com.netroaki.chex.config.CHEXConfig.radiationDamagePerAmplifier());
                root.add("behavior", behavior);
                // Travel graph summary
                root.addProperty("travelGraph", com.netroaki.chex.travel.TravelGraph.getTravelGraphSummary());
                try {
                        java.nio.file.Path outDir = java.nio.file.Paths.get("run");
                        java.nio.file.Files.createDirectories(outDir);
                        java.nio.file.Path out = outDir.resolve("chex_effective_config.json");
                        java.nio.file.Files.writeString(out, gson.toJson(root));
                        source.sendSuccess(() -> Component.literal("§aWrote snapshot to " + out), false);
                        return 1;
                } catch (Exception e) {
                        source.sendFailure(Component.literal("Failed to write snapshot: " + e.getMessage()));
                        return 0;
                }
        }

        private static int launchToPlanet(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                CommandSourceStack source = context.getSource();
                ServerPlayer player = source.getPlayerOrException();
                ResourceLocation targetId = ResourceLocationArgument.getId(context, "planet");

                if (!PlanetRegistry.getAllPlanets().containsKey(targetId)) {
                        source.sendFailure(Component.literal("Unknown planet: " + targetId));
                        return 0;
                }

                ResourceKey<net.minecraft.world.level.Level> dimKey = ResourceKey.create(Registries.DIMENSION,
                                targetId);
                ServerLevel targetLevel = player.server.getLevel(dimKey);
                if (targetLevel == null) {
                        source.sendFailure(Component.literal("Dimension not found for planet: " + targetId));
                        return 0;
                }

                int tier = player.getCapability(com.netroaki.chex.capabilities.PlayerTierCapability.INSTANCE)
                                .map(com.netroaki.chex.capabilities.PlayerTierCapability::getRocketTier)
                                .orElse(0);
                boolean ok = LaunchHooks.canLaunch(player, dimKey, tier);
                if (!ok) {
                        // Specific deny toasts/messages are sent by LaunchHooks via networking
                        source.sendFailure(Component.literal("Launch denied to " + targetId));
                        return 0;
                }

                // Consume fuel for launch (buckets)
                LaunchHooks.consumeFuelForLaunch(player, tier);

                BlockPos spawn = targetLevel.getSharedSpawnPos();
                double tx = spawn.getX() + 0.5;
                double ty = Math.max(spawn.getY(), targetLevel.getMinBuildHeight() + 2);
                double tz = spawn.getZ() + 0.5;
                player.teleportTo(targetLevel, tx, ty, tz, player.getYRot(), player.getXRot());
                source.sendSuccess(() -> Component.literal("Launched to " + targetId), false);
                return 1;
        }
}