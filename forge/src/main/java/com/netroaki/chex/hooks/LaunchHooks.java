package com.netroaki.chex.hooks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.capabilities.PlayerTierCapability;
import com.netroaki.chex.network.CHEXNetwork;
import com.netroaki.chex.network.LaunchDenyMessage;
import com.netroaki.chex.registry.FuelRegistry;
import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.registry.PlanetRegistry;
import com.netroaki.chex.suits.SuitItems;
import com.netroaki.chex.suits.SuitTiers;
import com.netroaki.chex.travel.TravelGraph;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class LaunchHooks {
  private LaunchHooks() {}

  /** Comprehensive launch validation for players traveling to planets */
  public static boolean canLaunch(Player player, ResourceKey<Level> target, int rocketTierLevel) {
    if (player == null) {
      CHEX.LOGGER.warn("Launch validation called with null player");
      return false;
    }

    // Get player capability
    PlayerTierCapability playerTier =
        player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
    if (playerTier == null) {
      CHEX.LOGGER.warn("Player capability not found for {}", player.getGameProfile().getName());
      return false;
    }

    // Check if planet is accessible via travel graph
    if (!TravelGraph.isPlanetAccessible(target.location(), rocketTierLevel)) {
      int requiredTier = TravelGraph.getTierForPlanet(target.location());
      CHEXNetwork.sendTo(
          (net.minecraft.server.level.ServerPlayer) player,
          new LaunchDenyMessage(
              LaunchDenyMessage.Code.TIER,
              "Require T" + requiredTier + ", have T" + rocketTierLevel));
      return false;
    }

    // Check if player has unlocked this nodule tier
    if (!playerTier.canAccessNoduleTier(NoduleTiers.getByTier(rocketTierLevel))) {
      CHEXNetwork.sendTo(
          (net.minecraft.server.level.ServerPlayer) player,
          new LaunchDenyMessage(
              LaunchDenyMessage.Code.TIER, "Nodule tier not unlocked: T" + rocketTierLevel));
      return false;
    }

    // Check if player has discovered this planet
    if (!playerTier.hasUnlockedPlanet(target.location().toString())) {
      CHEXNetwork.sendTo(
          (net.minecraft.server.level.ServerPlayer) player,
          new LaunchDenyMessage(LaunchDenyMessage.Code.DISCOVERY, "Planet not discovered"));
      return false;
    }

    // Check suit requirements
    if (!validateSuitRequirements(player, target)) {
      return false;
    }

    // Check fuel requirements
    if (!validateFuelRequirements(player, rocketTierLevel)) {
      return false;
    }

    // Log successful validation
    CHEX.LOGGER.info(
        "Launch validation passed: player={}, target={}, tier={}",
        player.getGameProfile().getName(),
        target.location(),
        rocketTierLevel);

    // Record first launch milestone if this is the first time
    player.getCapability(PlayerTierCapability.INSTANCE).ifPresent(capability -> {
      capability.setMilestone(PlayerTierCapability.MILESTONE_FIRST_LAUNCH);
      
      // Record tier milestones
      if (rocketTierLevel >= 5) {
        capability.setMilestone(PlayerTierCapability.MILESTONE_TIER_5_ROCKET);
      }
      if (rocketTierLevel >= 10) {
        capability.setMilestone(PlayerTierCapability.MILESTONE_TIER_10_ROCKET);
      }
      
      // Sync capability to client
      if (!player.level().isClientSide) {
        CHEXNetwork.sendPlayerTierToClient((net.minecraft.server.level.ServerPlayer) player);
      }
    });

    return true;
  }

  /**
   * Consume fuel buckets from the player's inventory for a successful launch. Returns true if
   * consumption succeeded or was not required.
   */
  public static boolean consumeFuelForLaunch(Player player, int rocketTier) {
    var fuelReq = FuelRegistry.getFuelRequirement(rocketTier);
    if (fuelReq.isEmpty()) {
      return true;
    }
    int requiredVolume = FuelRegistry.getFuelVolume(rocketTier);
    int neededBuckets = Math.max(1, (int) Math.ceil(requiredVolume / 1000.0));

    net.minecraft.world.item.Item requiredBucket = null;
    String path = fuelReq.get().getFluidId().getPath();
    if (path.contains("kerosene"))
      requiredBucket = com.netroaki.chex.registry.items.CHEXItems.KEROSENE_BUCKET.get();
    else if (path.equals("rp1"))
      requiredBucket = com.netroaki.chex.registry.items.CHEXItems.RP1_BUCKET.get();
    else if (path.equals("lox"))
      requiredBucket = com.netroaki.chex.registry.items.CHEXItems.LOX_BUCKET.get();
    else if (path.equals("lh2"))
      requiredBucket = com.netroaki.chex.registry.items.CHEXItems.LH2_BUCKET.get();

    if (requiredBucket == null) return true;

    int toConsume = neededBuckets;
    var inventory = player.getInventory();
    // Consume from main inventory
    for (int i = 0; i < inventory.items.size() && toConsume > 0; i++) {
      var stack = inventory.items.get(i);
      if (stack.getItem() == requiredBucket) {
        int take = Math.min(stack.getCount(), toConsume);
        stack.shrink(take);
        toConsume -= take;
      }
    }
    // Consume from offhand
    for (int i = 0; i < inventory.offhand.size() && toConsume > 0; i++) {
      var stack = inventory.offhand.get(i);
      if (stack.getItem() == requiredBucket) {
        int take = Math.min(stack.getCount(), toConsume);
        stack.shrink(take);
        toConsume -= take;
      }
    }

    if (toConsume > 0) {
      CHEX.LOGGER.warn(
          "[CHEX] Fuel consumption shortfall: neededBuckets={}, remaining={} (should not happen"
              + " after validation)",
          neededBuckets,
          toConsume);
    }
    return true;
  }

  /** Validates that the player has the required suit tier for the destination */
  private static boolean validateSuitRequirements(Player player, ResourceKey<Level> target) {
    // Get required suit tier for the planet
    SuitTiers requiredSuit = getRequiredSuitForPlanet(target);
    if (requiredSuit == null) {
      return true; // No suit requirement
    }

    // Get player's current suit tier
    SuitTiers playerSuit = getPlayerSuitTier(player);
    if (playerSuit == null) {
      player.displayClientMessage(
          Component.literal(
              "§c[CHEX] You need a " + requiredSuit.getDisplayName() + " suit to travel here!"),
          true);
      return false;
    }

    // Check if player's suit is adequate
    if (playerSuit.getTier() < requiredSuit.getTier()) {
      player.displayClientMessage(
          Component.literal(
              "§c[CHEX] Suit tier too low! Required: "
                  + requiredSuit.getDisplayName()
                  + " (Tier "
                  + requiredSuit.getTier()
                  + "), Current: "
                  + playerSuit.getDisplayName()
                  + " (Tier "
                  + playerSuit.getTier()
                  + ")"),
          true);
      return false;
    }

    return true;
  }

  /** Get the required suit tier for a planet */
  private static SuitTiers getRequiredSuitForPlanet(ResourceKey<Level> dimension) {
    // Check CHEX planets first
    var planetDef = PlanetRegistry.getPlanet(dimension.location());
    if (planetDef != null) {
      // Extract suit tier from suit tag (e.g., "chex:suits/suit3" -> 3)
      String suitTag = planetDef.requiredSuitTag();
      if (suitTag != null && suitTag.contains("suit")) {
        try {
          String tierStr = suitTag.substring(suitTag.lastIndexOf("suit") + 4);
          int tier = Integer.parseInt(tierStr);
          return SuitTiers.getByTier(tier);
        } catch (NumberFormatException e) {
          CHEX.LOGGER.warn("Could not parse suit tier from tag: {}", suitTag);
        }
      }
    }

    // Check travel graph for tier requirements
    int tier = TravelGraph.getTierForPlanet(dimension.location());
    if (tier != Integer.MAX_VALUE) {
      // Map rocket tier to suit tier (simplified mapping)
      int suitTier = Math.max(1, Math.min(5, (tier + 1) / 2));
      return SuitTiers.getByTier(suitTier);
    }

    return null;
  }

  /** Get the player's current suit tier based on equipped armor */
  private static SuitTiers getPlayerSuitTier(Player player) {
    SuitTiers highestTier = null;

    for (int i = 0; i < 4; i++) {
      var armorItem = player.getInventory().armor.get(i);
      if (armorItem.getItem() instanceof SuitItems.SuitItem suitItem) {
        SuitTiers suitTier = suitItem.getSuitTier();
        if (highestTier == null || suitTier.getTier() > highestTier.getTier()) {
          highestTier = suitTier;
        }
      }
    }

    return highestTier;
  }

  /** Validates fuel requirements for rocket launch */
  private static boolean validateFuelRequirements(Player player, int rocketTier) {
    // Get fuel requirement for this rocket tier
    var fuelReq = FuelRegistry.getFuelRequirement(rocketTier);
    if (fuelReq.isEmpty()) {
      CHEX.LOGGER.warn("No fuel requirement found for nodule tier {}", rocketTier);
      return true; // Allow launch if no fuel requirement
    }

    int requiredVolume = FuelRegistry.getFuelVolume(rocketTier);
    String fuelName = fuelReq.get().getFluidId().toString();

    // Simplified survival check: require carrying enough buckets of the required
    // fuel
    CHEX.LOGGER.info(
        "Fuel validation: player={}, requiredFuel={}, requiredVolume={}mB, noduleTier={}",
        player.getGameProfile().getName(),
        fuelName,
        requiredVolume,
        rocketTier);

    int neededBuckets = Math.max(1, (int) Math.ceil(requiredVolume / 1000.0));
    int have = 0;
    net.minecraft.world.item.Item requiredBucket = null;
    String path = fuelReq.get().getFluidId().getPath();
    if (path.contains("kerosene"))
      requiredBucket = com.netroaki.chex.registry.items.CHEXItems.KEROSENE_BUCKET.get();
    else if (path.equals("rp1"))
      requiredBucket = com.netroaki.chex.registry.items.CHEXItems.RP1_BUCKET.get();
    else if (path.equals("lox"))
      requiredBucket = com.netroaki.chex.registry.items.CHEXItems.LOX_BUCKET.get();
    else if (path.equals("lh2"))
      requiredBucket = com.netroaki.chex.registry.items.CHEXItems.LH2_BUCKET.get();

    if (requiredBucket != null) {
      for (var stack : player.getInventory().items) {
        if (stack.getItem() == requiredBucket) have += stack.getCount();
      }
      for (var stack : player.getInventory().offhand) {
        if (stack.getItem() == requiredBucket) have += stack.getCount();
      }
      if (have < neededBuckets) {
        CHEXNetwork.sendTo(
            (net.minecraft.server.level.ServerPlayer) player,
            new LaunchDenyMessage(
                LaunchDenyMessage.Code.FUEL,
                "Need " + neededBuckets + " buckets of " + fuelName + ", have " + have));
        return false;
      }
    } else {
      player.displayClientMessage(
          Component.literal("§e[CHEX] Fuel required: " + fuelName + " (" + requiredVolume + "mB)"),
          false);
    }

    return true;
  }

  /** Validates launch requirements for a specific planet without a player context */
  public static boolean canLaunchToPlanet(ResourceKey<Level> target, int rocketTierLevel) {
    return TravelGraph.isPlanetAccessible(target.location(), rocketTierLevel);
  }

  /** Gets the minimum rocket tier required for a planet */
  public static int getRequiredRocketTier(ResourceKey<Level> target) {
    return TravelGraph.getTierForPlanet(target.location());
  }

  /** Gets the required suit tier for a planet */
  public static SuitTiers getRequiredSuitTier(ResourceKey<Level> target) {
    return getRequiredSuitForPlanet(target);
  }

  /** Gets all planets accessible with a given rocket tier */
  public static List<ResourceLocation> getAccessiblePlanets(int rocketTier) {
    return TravelGraph.getPlanetsForTier(rocketTier).stream().toList();
  }

  /** Get fuel information for a rocket tier */
  public static String getFuelInfo(int rocketTier) {
    var fuelReq = FuelRegistry.getFuelRequirement(rocketTier);
    if (fuelReq.isPresent()) {
      int volume = FuelRegistry.getFuelVolume(rocketTier);
      return fuelReq.get().getFluidId() + " (" + volume + "mB)";
    }
    return "No fuel requirement";
  }

  /** Check if a player can access a specific rocket tier */
  public static boolean canAccessRocketTier(Player player, int rocketTier) {
    PlayerTierCapability capability =
        player.getCapability(PlayerTierCapability.INSTANCE).orElse(null);
    if (capability == null) return false;
    return capability.canAccessRocketTier(NoduleTiers.getByTier(rocketTier));
  }
}
