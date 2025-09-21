package com.netroaki.chex.minerals;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.capabilities.PlayerTierCapability;
import com.netroaki.chex.network.CHEXNetwork;
import com.netroaki.chex.registry.PlanetRegistry;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** System for handling mineral discovery and progression */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class MineralDiscoverySystem {

  /** Handle mineral discovery when player breaks blocks */
  @SubscribeEvent
  public static void onBlockBreak(PlayerEvent.BreakSpeed event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) {
      return;
    }

    // Check if the broken block is a mineral ore
    Block block = event.getState().getBlock();
    String mineral = getMineralFromBlock(block);

    if (mineral != null) {
      discoverMineral(player, mineral);
    }
  }

  /** Handle mineral discovery when player picks up items */
  @SubscribeEvent
  public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) {
      return;
    }

    ItemStack stack = event.getStack();
    String mineral = getMineralFromItem(stack);

    if (mineral != null) {
      discoverMineral(player, mineral);
    }
  }

  /** Discover a mineral for a player */
  public static void discoverMineral(ServerPlayer player, String mineral) {
    player
        .getCapability(PlayerTierCapability.INSTANCE)
        .ifPresent(
            capability -> {
              if (!capability.hasDiscoveredMineral(mineral)) {
                capability.discoverMineral(mineral);

                // Broadcast the discovery
                String playerName = player.getGameProfile().getName();
                CHEXNetwork.broadcastProgressionUpdate(
                    player, "mineral", playerName + " discovered " + mineral + "!");

                // Check if this unlocks new planets
                checkForPlanetUnlocks(player, capability, mineral);

                CHEX.LOGGER.info("Player {} discovered mineral: {}", playerName, mineral);
              }
            });
  }

  /** Check if discovering a mineral unlocks new planets */
  private static void checkForPlanetUnlocks(
      ServerPlayer player, PlayerTierCapability capability, String mineral) {
    int mineralTier = com.netroaki.chex.CHEX.gt().getMineralGTTier(mineral);

    // Check all planets to see if this mineral discovery unlocks any
    PlanetRegistry.getAllPlanets()
        .values()
        .forEach(
            planet -> {
              String planetId = planet.id().toString();

              // If player can access the planet's nodule tier and hasn't unlocked it yet
              if (capability.canAccessNoduleTier(planet.requiredRocketTier())
                  && !capability.hasUnlockedPlanet(planetId)) {

                // Check if this planet has this mineral
                if (com.netroaki.chex.CHEX.gt().isMineralAvailableOnPlanet(planetId, mineral)) {
                  capability.unlockPlanet(planetId);

                  // Broadcast the planet unlock
                  String playerName = player.getGameProfile().getName();
                  CHEXNetwork.broadcastProgressionUpdate(
                      player, "planet", playerName + " discovered planet " + planetId + "!");

                  CHEX.LOGGER.info(
                      "Player {} unlocked planet {} through mineral discovery",
                      playerName,
                      planetId);
                }
              }
            });
  }

  /** Get mineral name from block */
  private static String getMineralFromBlock(Block block) {
    String blockName = block.getDescriptionId().toLowerCase();

    // Map block names to minerals
    if (blockName.contains("iron_ore")) return "iron";
    if (blockName.contains("copper_ore")) return "copper";
    if (blockName.contains("tin_ore")) return "tin";
    if (blockName.contains("silver_ore")) return "silver";
    if (blockName.contains("lead_ore")) return "lead";
    if (blockName.contains("gold_ore")) return "gold";
    if (blockName.contains("nickel_ore")) return "nickel";
    if (blockName.contains("cobalt_ore")) return "cobalt";
    if (blockName.contains("platinum_ore")) return "platinum";
    if (blockName.contains("titanium_ore")) return "titanium";
    if (blockName.contains("tungsten_ore")) return "tungsten";
    if (blockName.contains("uranium_ore")) return "uranium";
    if (blockName.contains("thorium_ore")) return "thorium";

    return null;
  }

  /** Get mineral name from item */
  private static String getMineralFromItem(ItemStack stack) {
    String itemName = stack.getItem().getDescriptionId().toLowerCase();

    // Map item names to minerals
    if (itemName.contains("iron_ingot") || itemName.contains("iron_dust")) return "iron";
    if (itemName.contains("copper_ingot") || itemName.contains("copper_dust")) return "copper";
    if (itemName.contains("tin_ingot") || itemName.contains("tin_dust")) return "tin";
    if (itemName.contains("silver_ingot") || itemName.contains("silver_dust")) return "silver";
    if (itemName.contains("lead_ingot") || itemName.contains("lead_dust")) return "lead";
    if (itemName.contains("gold_ingot") || itemName.contains("gold_dust")) return "gold";
    if (itemName.contains("nickel_ingot") || itemName.contains("nickel_dust")) return "nickel";
    if (itemName.contains("cobalt_ingot") || itemName.contains("cobalt_dust")) return "cobalt";
    if (itemName.contains("platinum_ingot") || itemName.contains("platinum_dust"))
      return "platinum";
    if (itemName.contains("titanium_ingot") || itemName.contains("titanium_dust"))
      return "titanium";
    if (itemName.contains("tungsten_ingot") || itemName.contains("tungsten_dust"))
      return "tungsten";
    if (itemName.contains("uranium_ingot") || itemName.contains("uranium_dust")) return "uranium";
    if (itemName.contains("thorium_ingot") || itemName.contains("thorium_dust")) return "thorium";

    return null;
  }

  /** Get all available minerals for a planet */
  public static Set<String> getAvailableMineralsForPlanet(String planetId) {
    Map<String, Object> mineralData = com.netroaki.chex.CHEX.gt().getMineralDataForPlanet(planetId);
    return mineralData.keySet();
  }

  /** Get mineral abundance for a planet */
  public static double getMineralAbundance(String planetId, String mineral) {
    Map<String, Object> mineralData = com.netroaki.chex.CHEX.gt().getMineralDataForPlanet(planetId);
    if (mineralData.containsKey(mineral)) {
      Map<String, Object> mineralInfo = (Map<String, Object>) mineralData.get(mineral);
      return (Double) mineralInfo.get("abundance");
    }
    return 0.0;
  }
}
