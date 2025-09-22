package com.netroaki.chex.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.netroaki.chex.registry.PlanetDef;
import com.netroaki.chex.registry.PlanetRegistry;
import net.minecraft.resources.ResourceLocation;

/**
 * Loader-neutral core for travel graph lookups and storage. Uses String planet IDs to avoid loader
 * dependencies.
 */
public final class TravelGraphCore {
  private TravelGraphCore() {}

  private static final Map<Integer, Set<String>> TIER_TO_PLANETS = new ConcurrentHashMap<>();
  private static final Map<String, Integer> PLANET_TO_TIER = new ConcurrentHashMap<>();
  private static final Map<String, String> PLANET_SOURCES = new ConcurrentHashMap<>();

  public static void clear() {
    TIER_TO_PLANETS.clear();
    PLANET_TO_TIER.clear();
    PLANET_SOURCES.clear();
  }

  public static void initializeDefaultGraph() {
    clear();
    add(1, "cosmos:earth_moon", "cosmos");
    add(2, "cosmos:venus", "cosmos");
    add(3, "cosmos:mars", "cosmos");
  }

  public static void loadFromMapping(Map<Integer, Set<String>> mapping) {
    clear();
    mapping.forEach((tier, set) -> set.forEach(id -> add(tier, id, ns(id))));
  }

  public static void add(int tier, String planetId, String source) {
    TIER_TO_PLANETS.computeIfAbsent(tier, k -> new HashSet<>()).add(planetId);
    PLANET_TO_TIER.put(planetId, tier);
    PLANET_SOURCES.put(planetId, source);
  }

  public static Set<String> getPlanetsForTier(int tier) {
    return new HashSet<>(TIER_TO_PLANETS.getOrDefault(tier, Collections.emptySet()));
  }

  public static int getTierForPlanet(String planetId) {
    return PLANET_TO_TIER.getOrDefault(planetId, Integer.MAX_VALUE);
  }

  public static boolean isPlanetAccessible(String planetId, int tier) {
    return getTierForPlanet(planetId) <= tier;
  }

  public static String getPlanetSource(String planetId) {
    return PLANET_SOURCES.getOrDefault(planetId, "unknown");
  }

  public static Set<String> getAllPlanets() {
    return new HashSet<>(PLANET_TO_TIER.keySet());
  }

  /**
   * Validates the travel graph and returns a list of validation issues.
   * @return List of validation messages, empty if no issues found
   */
  public static List<String> validate() {
    List<String> issues = new ArrayList<>();
    
    // Check for planets in the graph that aren't in the registry
    Set<String> unknownPlanets = new HashSet<>();
    for (String planetId : PLANET_TO_TIER.keySet()) {
      if (!PlanetRegistry.getAllPlanets().containsKey(ResourceLocation.tryParse(planetId))) {
        unknownPlanets.add(planetId);
      }
    }
    
    if (!unknownPlanets.isEmpty()) {
      issues.add("❌ Found " + unknownPlanets.size() + " unknown planet IDs in travel graph:");
      unknownPlanets.forEach(id -> issues.add("  - " + id + " (source: " + getPlanetSource(id) + ")"));
    }
    
    // Check for tier issues (planets that are in the registry but not in the travel graph)
    Set<String> missingPlanets = new HashSet<>();
    for (ResourceLocation planetId : PlanetRegistry.getAllPlanets().keySet()) {
      if (!PLANET_TO_TIER.containsKey(planetId.toString()) && !planetId.getPath().contains("orbit")) {
        missingPlanets.add(planetId.toString());
      }
    }
    
    if (!missingPlanets.isEmpty()) {
      issues.add("\n⚠️ Found " + missingPlanets.size() + " planets missing from travel graph:");
      missingPlanets.forEach(id -> {
        PlanetDef def = PlanetRegistry.getAllPlanets().get(ResourceLocation.tryParse(id));
        issues.add(String.format("  - %s (Tier %d)", id, def.requiredRocketTier().getTier()));
      });
    }
    
    // Check for tier consistency
    Map<String, String> tierMismatches = new LinkedHashMap<>();
    for (Map.Entry<ResourceLocation, PlanetDef> entry : PlanetRegistry.getAllPlanets().entrySet()) {
      String planetId = entry.getKey().toString();
      if (!PLANET_TO_TIER.containsKey(planetId)) continue;
      
      int registryTier = entry.getValue().requiredRocketTier().getTier();
      int graphTier = getTierForPlanet(planetId);
      
      if (registryTier != graphTier) {
        tierMismatches.put(planetId, 
            String.format("Tier mismatch - Registry: %d, Graph: %d", registryTier, graphTier));
      }
    }
    
    if (!tierMismatches.isEmpty()) {
      issues.add("\n⚠️ Found " + tierMismatches.size() + " planets with tier mismatches:");
      tierMismatches.forEach((id, msg) -> issues.add("  - " + id + ": " + msg));
    }
    
    // Check for reachability (all lower tier planets should be accessible)
    Map<String, Set<String>> unreachablePlanets = new HashMap<>();
    for (Map.Entry<Integer, Set<String>> entry : TIER_TO_PLANETS.entrySet()) {
      int currentTier = entry.getKey();
      for (String planetId : entry.getValue()) {
        ResourceLocation planetLoc = ResourceLocation.tryParse(planetId);
        if (planetLoc == null) continue;
        
        PlanetDef planet = PlanetRegistry.getAllPlanets().get(planetLoc);
        if (planet == null) continue;
        
        // Check if this planet requires visiting a higher tier planet first
        // Note: This assumes PlanetDef has a method getRequiredPlanets() that returns a Set<String>
        // If it doesn't, we'll need to add that to the PlanetDef class
        Set<String> requiredPlanets = new HashSet<>(); // Placeholder - replace with actual required planets
        // Uncomment the line below when getRequiredPlanets() is available
        // Set<String> requiredPlanets = planet.getRequiredPlanets();
        
        for (String requiredId : requiredPlanets) {
          int requiredTier = getTierForPlanet(requiredId);
          if (requiredTier > currentTier) {
            unreachablePlanets
                .computeIfAbsent(planetId, k -> new HashSet<>())
                .add(requiredId + " (Tier " + requiredTier + " > " + currentTier + ")");
          }
        }
      }
    }
    
    if (!unreachablePlanets.isEmpty()) {
      issues.add("\n❌ Found " + unreachablePlanets.size() + " potentially unreachable planets:");
      unreachablePlanets.forEach((planetId, requirements) -> {
        issues.add("  - " + planetId + " requires:");
        requirements.forEach(req -> issues.add("    - " + req));
      });
    }
    
    // Check for empty tiers
    List<Integer> emptyTiers = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      if (!TIER_TO_PLANETS.containsKey(i) || TIER_TO_PLANETS.get(i).isEmpty()) {
        emptyTiers.add(i);
      }
    }
    
    if (!emptyTiers.isEmpty()) {
      issues.add("\nℹ️ Found " + emptyTiers.size() + " empty tiers: " + emptyTiers);
    }
    
    // Add summary
    if (issues.isEmpty()) {
      issues.add("✅ Travel graph validation passed with no issues!");
    } else {
      issues.add(0, "\n=== Travel Graph Validation Report ===");
      issues.add("");
      issues.add("Summary:");
      issues.add("  - " + unknownPlanets.size() + " unknown planets");
      issues.add("  - " + missingPlanets.size() + " missing planets");
      issues.add("  - " + tierMismatches.size() + " tier mismatches");
      issues.add("  - " + unreachablePlanets.size() + " potentially unreachable planets");
      issues.add("  - " + emptyTiers.size() + " empty tiers");
      issues.add("");
    }
    
    return issues;
  }
              PlanetRegistry.getPlanet(ResourceLocation.tryParse(id))
                  .map(p -> p.requiredRocketTier().getTier())
                  .orElse(-1),
              tier)));

  private static String ns(String id) {
    return id.split(":")[0];
  }
  
  /**
   * Validates the travel graph and returns a formatted string with the results.
   * @return Formatted validation results
   */
  public static String validateAndFormat() {
    List<String> issues = validate();
    return String.join("\n", issues);
  }
  
  /**
   * Validates the travel graph and saves the results to a file.
   * @param outputPath The path to save the validation results to
   * @return A message indicating success or failure
   */
  public static String validateAndSaveToFile(String outputPath) {
    try {
      List<String> issues = validate();
      String output = String.join("\n", issues);
      java.nio.file.Files.write(java.nio.file.Paths.get(outputPath), output.getBytes(java.nio.charset.StandardCharsets.UTF_8));
      return "✓ Validation results saved to " + outputPath;
    } catch (java.io.IOException e) {
      return "✗ Failed to save validation results: " + e.getMessage();
    }
  }
}
