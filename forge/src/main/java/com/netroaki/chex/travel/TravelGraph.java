package com.netroaki.chex.travel;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.config.TravelConfigLoader;
import com.netroaki.chex.core.TravelGraphCore;
import com.netroaki.chex.registry.PlanetDef;
import com.netroaki.chex.registry.PlanetRegistry;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Travel graph system that defines which planets are accessible at each rocket tier Based on the
 * checklist: T1: LEO, T2: Moon, T3: Mars orbit, T4: Mars surface, etc.
 */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class TravelGraph implements ResourceManagerReloadListener {

  private static final Map<Integer, Set<ResourceLocation>> TIER_TO_PLANETS =
      new ConcurrentHashMap<>();

  @SubscribeEvent
  public static void onServerStarting(ServerStartingEvent event) {
    initializeDefaultGraph();
    CHEX.LOGGER.info(
        "Travel graph initialized with {} planets across {} tiers",
        TravelGraphCore.getAllPlanets().size(),
        TIER_TO_PLANETS.size());
  }

  /** Initialize the default travel graph using the current PlanetRegistry snapshot. */
  public static void initializeDefaultGraph() {
    TravelGraphCore.clear();

    PlanetRegistry.getAllPlanets().forEach((id, def) -> addPlanetToCore(id, def));

    syncFromCore();

    CHEX.LOGGER.info(
        "Default travel graph loaded: {} planets across {} tiers",
        TravelGraphCore.getAllPlanets().size(),
        TIER_TO_PLANETS.size());
  }

  /** Load the travel graph from config overrides or regenerate defaults when absent. */
  public static void loadFromConfigOrDefaults() {
    boolean loaded = false;
    var mapOpt = TravelConfigLoader.load();
    if (mapOpt.isPresent() && !mapOpt.get().isEmpty()) {
      Map<Integer, Set<String>> asStrings = new HashMap<>();
      mapOpt
          .get()
          .forEach(
              (tier, set) -> {
                Set<String> ids = new HashSet<>();
                for (ResourceLocation rl : set) {
                  ids.add(rl.toString());
                }
                asStrings.put(tier, ids);
              });
      TravelGraphCore.loadFromMapping(asStrings);
      syncFromCore();
      loaded = true;
      CHEX.LOGGER.info(
          "Travel graph loaded from config: {} planets across {} tiers",
          TravelGraphCore.getAllPlanets().size(),
          TIER_TO_PLANETS.size());
    }
    if (!loaded) {
      initializeDefaultGraph();
    }
  }

  private static void addPlanetToCore(ResourceLocation id, PlanetDef def) {
    int tier = def.requiredRocketTier() != null ? def.requiredRocketTier().getTier() : 1;
    tier = Math.max(1, Math.min(tier, 10));
    TravelGraphCore.add(tier, id.toString(), id.getNamespace());
  }

  private static void syncFromCore() {
    TIER_TO_PLANETS.clear();
    for (int i = 1; i <= 10; i++) {
      Set<ResourceLocation> planets = new HashSet<>();
      for (String id : TravelGraphCore.getPlanetsForTier(i)) {
        planets.add(ResourceLocation.parse(id));
      }
      if (!planets.isEmpty()) {
        TIER_TO_PLANETS.put(i, planets);
      }
    }
  }

  /** Get all planets accessible at a given nodule tier */
  public static Set<ResourceLocation> getPlanetsForTier(int tier) {
    return new HashSet<>(TIER_TO_PLANETS.getOrDefault(tier, Collections.emptySet()));
  }

  /** Get the minimum nodule tier required to access a planet */
  public static int getTierForPlanet(ResourceLocation planet) {
    return TravelGraphCore.getTierForPlanet(planet.toString());
  }

  /** Check if a planet is accessible at a given nodule tier */
  public static boolean isPlanetAccessible(ResourceLocation planet, int noduleTier) {
    return TravelGraphCore.isPlanetAccessible(planet.toString(), noduleTier);
  }

  /** Get the source of a planet (cosmos, chex, etc.) */
  public static String getPlanetSource(ResourceLocation planet) {
    return TravelGraphCore.getPlanetSource(planet.toString());
  }

  /** Get all planets in the travel graph */
  public static Set<ResourceLocation> getAllPlanets() {
    Set<ResourceLocation> r = new HashSet<>();
    for (String id : TravelGraphCore.getAllPlanets()) {
      r.add(ResourceLocation.parse(id));
    }
    return r;
  }

  /** Get a summary of the travel graph for debugging */
  public static String getTravelGraphSummary() {
    StringBuilder sb = new StringBuilder();
    sb.append("Travel Graph Summary:\n");
    for (int tier = 1; tier <= 10; tier++) {
      Set<ResourceLocation> planets = getPlanetsForTier(tier);
      if (!planets.isEmpty()) {
        sb.append(String.format("T%d: %d planets\n", tier, planets.size()));
      }
    }
    return sb.toString();
  }

  /** Validation result for travel graph integrity checks */
  public static class ValidationResult {
    private final boolean valid;
    private final Set<String> unknownPlanets;
    private final Set<Integer> invalidTiers;
    private final Set<String> conflictingPlanets;
    private final int totalPlanets;
    private final int tiersWithPlanets;

    public ValidationResult(
        boolean valid,
        Set<String> unknownPlanets,
        Set<Integer> invalidTiers,
        Set<String> conflictingPlanets,
        int totalPlanets,
        int tiersWithPlanets) {
      this.valid = valid;
      this.unknownPlanets = unknownPlanets;
      this.invalidTiers = invalidTiers;
      this.conflictingPlanets = conflictingPlanets;
      this.totalPlanets = totalPlanets;
      this.tiersWithPlanets = tiersWithPlanets;
    }

    public boolean isValid() {
      return valid;
    }

    public Set<String> getUnknownPlanets() {
      return unknownPlanets;
    }

    public Set<Integer> getInvalidTiers() {
      return invalidTiers;
    }

    public Set<String> getConflictingPlanets() {
      return conflictingPlanets;
    }

    public int getTotalPlanets() {
      return totalPlanets;
    }

    public int getTiersWithPlanets() {
      return tiersWithPlanets;
    }
  }

  /** Validate the travel graph for integrity issues */
  public static ValidationResult validate() {
    Set<String> unknownPlanets = new HashSet<>();
    Set<Integer> invalidTiers = new HashSet<>();
    Set<String> conflictingPlanets = new HashSet<>();
    int totalPlanets = 0;
    int tiersWithPlanets = 0;

    // Get all registered planets from PlanetRegistry
    Set<ResourceLocation> registeredPlanets = PlanetRegistry.getAllPlanets().keySet();
    Set<String> registeredPlanetIds = new HashSet<>();
    registeredPlanets.forEach(p -> registeredPlanetIds.add(p.toString()));

    // Check each tier
    for (int tier = 1; tier <= 10; tier++) {
      Set<ResourceLocation> tierPlanets = getPlanetsForTier(tier);
      if (!tierPlanets.isEmpty()) {
        tiersWithPlanets++;
        totalPlanets += tierPlanets.size();

        // Check for unknown planets
        for (ResourceLocation planet : tierPlanets) {
          if (!registeredPlanetIds.contains(planet.toString())) {
            unknownPlanets.add(planet.toString());
          }
        }

        // Check for invalid tier assignments
        for (ResourceLocation planet : tierPlanets) {
          PlanetDef planetDef = PlanetRegistry.getPlanet(planet);
          if (planetDef != null) {
            int requiredTier = planetDef.requiredRocketTier().getTier();
            if (requiredTier != tier) {
              conflictingPlanets.add(
                  String.format(
                      "%s: assigned to T%d but requires T%d",
                      planet.toString(), tier, requiredTier));
            }
          }
        }
      }
    }

    // Check for planets that should be in travel graph but aren't
    for (ResourceLocation planet : registeredPlanets) {
      if (!getAllPlanets().contains(planet)) {
        unknownPlanets.add(planet.toString() + " (not in travel graph)");
      }
    }

    boolean valid =
        unknownPlanets.isEmpty() && invalidTiers.isEmpty() && conflictingPlanets.isEmpty();

    return new ValidationResult(
        valid, unknownPlanets, invalidTiers, conflictingPlanets, totalPlanets, tiersWithPlanets);
  }

  @Override
  public void onResourceManagerReload(ResourceManager resourceManager) {
    // Reload travel graph from config files if they exist
    loadFromConfigOrDefaults();
    CHEX.LOGGER.info("Travel graph reloaded from resources");
  }
}
