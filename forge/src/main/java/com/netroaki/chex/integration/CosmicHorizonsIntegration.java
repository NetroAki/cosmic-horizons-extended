package com.netroaki.chex.integration;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.registry.PlanetDef;
import com.netroaki.chex.registry.PlanetRegistry;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Integration system for Cosmic Horizons mod Handles discovery of existing CH planets and proper
 * integration
 */
public class CosmicHorizonsIntegration {

  private static final String COSMIC_HORIZONS_MOD_ID = "cosmichorizons";
  private static final String COSMOS_MOD_ID = "cosmos";

  private static boolean isCosmicHorizonsLoaded = false;
  private static boolean isCosmosLoaded = false;

  /** Initialize Cosmic Horizons integration */
  public static void initialize() {
    CHEX.LOGGER.info("Initializing Cosmic Horizons integration...");

    // Check if Cosmic Horizons mod is loaded
    isCosmicHorizonsLoaded = ModList.get().isLoaded(COSMIC_HORIZONS_MOD_ID);
    isCosmosLoaded = ModList.get().isLoaded(COSMOS_MOD_ID);

    if (isCosmicHorizonsLoaded) {
      CHEX.LOGGER.info("Cosmic Horizons mod detected - enabling full integration");
      discoverExistingPlanets();
    } else if (isCosmosLoaded) {
      CHEX.LOGGER.info("Cosmos mod detected - enabling basic integration");
      discoverExistingPlanets();
    } else {
      CHEX.LOGGER.info("No Cosmic Horizons mod detected - using standalone mode");
    }

    CHEX.LOGGER.info("Cosmic Horizons integration initialized");
  }

  /** Discover existing planets from Cosmic Horizons/Cosmos mods */
  private static void discoverExistingPlanets() {
    CHEX.LOGGER.info("Discovering existing planets from Cosmic Horizons/Cosmos...");

    try {
      MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
      if (server != null) {
        Registry<LevelStem> dimensionRegistry =
            server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);

        int discoveredCount = 0;
        for (Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : dimensionRegistry.entrySet()) {
          ResourceKey<LevelStem> dimensionKey = entry.getKey();
          ResourceLocation dimensionId = dimensionKey.location();

          // Check if this is a Cosmic Horizons/Cosmos dimension
          if (isCosmicHorizonsDimension(dimensionId)) {
            discoverPlanet(dimensionId);
            discoveredCount++;
          }
        }

        CHEX.LOGGER.info(
            "Discovered {} existing planets from Cosmic Horizons/Cosmos", discoveredCount);
      }
    } catch (Exception e) {
      CHEX.LOGGER.warn("Failed to discover existing planets: {}", e.getMessage());
    }
  }

  /** Check if a dimension belongs to Cosmic Horizons/Cosmos */
  private static boolean isCosmicHorizonsDimension(ResourceLocation dimensionId) {
    String namespace = dimensionId.getNamespace();
    return namespace.equals(COSMIC_HORIZONS_MOD_ID) || namespace.equals(COSMOS_MOD_ID);
  }

  /** Discover a specific planet and register it with CHEX */
  private static void discoverPlanet(ResourceLocation dimensionId) {
    try {
      // Create a PlanetDef for the discovered planet
      PlanetDef planetDef = createPlanetDefForDiscoveredPlanet(dimensionId);

      // Register the planet with CHEX
      PlanetRegistry.registerPlanet(dimensionId, planetDef);

      CHEX.LOGGER.debug("Discovered and registered planet: {} - {}", dimensionId, planetDef.name());
    } catch (Exception e) {
      CHEX.LOGGER.warn("Failed to discover planet {}: {}", dimensionId, e.getMessage());
    }
  }

  /** Create a PlanetDef for a discovered planet */
  private static PlanetDef createPlanetDefForDiscoveredPlanet(ResourceLocation dimensionId) {
    // Get planet information from dimension ID
    String planetName = getPlanetNameFromDimensionId(dimensionId);
    String description = getPlanetDescriptionFromDimensionId(dimensionId);
    NoduleTiers requiredTier = getRequiredTierFromDimensionId(dimensionId);
    String suitTag = getSuitTagFromDimensionId(dimensionId);
    Set<String> minerals = getMineralsFromDimensionId(dimensionId);
    String biomeType = getBiomeTypeFromDimensionId(dimensionId);
    
    // Get default hazards based on biome type
    Set<String> defaultHazards = getDefaultHazardsForBiome(biomeType);
    
    // Determine planet properties based on biome type
    boolean hasAtmosphere = !biomeType.equals("airless");
    boolean requiresOxygen = !biomeType.equals("vacuum") && !biomeType.equals("airless");
    int gravityLevel = calculateGravityLevel(biomeType);
    float temperature = calculateTemperature(biomeType);
    int radiationLevel = calculateRadiationLevel(biomeType);
    float baseOxygen = calculateBaseOxygen(biomeType);
    
    // Determine if this is an orbit dimension
    boolean isOrbit = dimensionId.getPath().contains("orbit") || 
                     dimensionId.getPath().contains("station") ||
                     dimensionId.getPath().contains("satellite");

    return new PlanetDef(
        dimensionId,
        planetName,
        description,
        requiredTier,
        suitTag,
        determineDefaultFuel(biomeType),
        gravityLevel,
        hasAtmosphere,
        requiresOxygen,
        defaultHazards,
        minerals,
        biomeType,
        isOrbit,
        temperature,
        radiationLevel,
        baseOxygen
    );
  }
  
  /**
   * Calculate the base temperature for a planet based on its biome type
   * @param biomeType The biome type of the planet
   * @return Temperature in Kelvin (200-600K)
   */
  private static float calculateTemperature(String biomeType) {
    return switch (biomeType.toLowerCase()) {
      case "volcanic", "lava" -> 500 + (float)(Math.random() * 200); // 500-700K
      case "desert", "arid" -> 320 + (float)(Math.random() * 80);    // 320-400K
      case "jungle", "swamp" -> 300 + (float)(Math.random() * 40);    // 300-340K
      case "temperate", "plains" -> 280 + (float)(Math.random() * 40); // 280-320K
      case "taiga", "tundra" -> 250 + (float)(Math.random() * 40);    // 250-290K
      case "snow", "ice" -> 200 + (float)(Math.random() * 50);        // 200-250K
      case "ocean", "water" -> 275 + (float)(Math.random() * 30);     // 275-305K
      case "airless", "vacuum" -> 100 + (float)(Math.random() * 100); // 100-200K
      default -> 250 + (float)(Math.random() * 100);                   // 250-350K
    };
  }
  
  /**
   * Calculate the radiation level for a planet based on its biome type
   * @param biomeType The biome type of the planet
   * @return Radiation level (0-10)
   */
  private static int calculateRadiationLevel(String biomeType) {
    return switch (biomeType.toLowerCase()) {
      case "volcanic", "radioactive" -> 7 + (int)(Math.random() * 4); // 7-10
      case "desert", "arid" -> 4 + (int)(Math.random() * 4);          // 4-7
      case "jungle", "swamp" -> 2 + (int)(Math.random() * 3);         // 2-4
      case "temperate", "plains", "ocean" -> 1 + (int)(Math.random() * 2); // 1-2
      case "taiga", "tundra", "snow", "ice" -> 1;                    // 1
      case "airless", "vacuum" -> 8 + (int)(Math.random() * 3);       // 8-10
      default -> 3;                                                    // 3
    };
  }
  
  /**
   * Calculate the base oxygen level for a planet based on its biome type
   * @param biomeType The biome type of the planet
   * @return Base oxygen level (0.0-1.0)
   */
  private static float calculateBaseOxygen(String biomeType) {
    return switch (biomeType.toLowerCase()) {
      case "jungle", "swamp" -> 0.25f + (float)(Math.random() * 0.15f);  // 0.25-0.4
      case "temperate", "plains" -> 0.2f + (float)(Math.random() * 0.1f); // 0.2-0.3
      case "taiga", "tundra" -> 0.18f + (float)(Math.random() * 0.1f);    // 0.18-0.28
      case "ocean" -> 0.15f + (float)(Math.random() * 0.1f);              // 0.15-0.25
      case "desert", "arid" -> 0.1f + (float)(Math.random() * 0.1f);      // 0.1-0.2
      case "volcanic", "lava" -> 0.05f + (float)(Math.random() * 0.1f);   // 0.05-0.15
      case "airless", "vacuum" -> 0f;                                    // 0
      default -> 0.1f;                                                     // Default 0.1
    };
  }
  }
  
  /** Get default hazards based on biome type */
  private static Set<String> getDefaultHazardsForBiome(String biomeType) {
    // Add common hazards based on biome type
    Set<String> hazards = new HashSet<>();
    
    switch (biomeType.toLowerCase()) {
      case "volcanic":
        hazards.add("heat");
        hazards.add("toxic_gas");
        break;
      case "frozen":
        hazards.add("cold");
        hazards.add("low_gravity");
        break;
      case "toxic":
        hazards.add("toxic_gas");
        hazards.add("acid_rain");
        break;
      case "vacuum":
      case "airless":
        hazards.add("vacuum");
        hazards.add("radiation");
        break;
      case "stormy":
        hazards.add("electric_storms");
        hazards.add("high_winds");
        break;
      default:
        // No default hazards for standard planets
        break;
    }
    
    return hazards;
  }
  
  /** Calculate gravity level based on biome/planet type */
  private static int calculateGravityLevel(String biomeType) {
    // Return gravity level (1-10 scale where 1 is lowest, 10 is highest)
    return switch (biomeType.toLowerCase()) {
      case "volcanic" -> 8;  // Dense planets have higher gravity
      case "gas_giant" -> 10; // Very high gravity
      case "frozen" -> 4;    // Icy planets often have lower gravity
      case "airless" -> 2;   // Small airless bodies have very low gravity
      case "moon" -> 3;      // Moons have lower gravity
      default -> 6;          // Standard gravity for terrestrial planets
    };
  }
  
  /** Determine default fuel type based on biome */
  private static String determineDefaultFuel(String biomeType) {
    return switch (biomeType.toLowerCase()) {
      case "volcanic" -> "minecraft:lava";
      case "gas_giant" -> "minecraft:blaze_powder";
      case "frozen" -> "minecraft:ice";
      case "ocean" -> "minecraft:water_bucket";
      default -> "minecraft:coal";
    };
  }

  /** Get planet name from dimension ID */
  private static String getPlanetNameFromDimensionId(ResourceLocation dimensionId) {
    String path = dimensionId.getPath();

    // Convert snake_case to Title Case
    String[] words = path.split("_");
    StringBuilder name = new StringBuilder();
    for (String word : words) {
      if (name.length() > 0) name.append(" ");
      name.append(word.substring(0, 1).toUpperCase()).append(word.substring(1));
    }

    return name.toString();
  }

  /** Get planet description from dimension ID */
  private static String getPlanetDescriptionFromDimensionId(ResourceLocation dimensionId) {
    String path = dimensionId.getPath();

    // Create description based on planet type
    if (path.contains("moon")) {
      return "A natural satellite with unique geological features";
    } else if (path.contains("mars")) {
      return "The red planet with ancient geological formations";
    } else if (path.contains("venus")) {
      return "A hellish world with extreme atmospheric pressure";
    } else if (path.contains("jupiter")) {
      return "A gas giant with massive storm systems";
    } else if (path.contains("saturn")) {
      return "A ringed gas giant with unique atmospheric properties";
    } else if (path.contains("uranus")) {
      return "An ice giant with extreme axial tilt";
    } else if (path.contains("neptune")) {
      return "The windiest planet in the solar system";
    } else if (path.contains("pluto")) {
      return "A dwarf planet in the outer solar system";
    } else if (path.contains("europa")) {
      return "An icy moon with potential subsurface ocean";
    } else if (path.contains("titan")) {
      return "A moon with thick atmosphere and hydrocarbon lakes";
    } else if (path.contains("ganymede")) {
      return "The largest moon in the solar system";
    } else if (path.contains("triton")) {
      return "Neptune's largest moon with retrograde orbit";
    } else if (path.contains("enceladus")) {
      return "A moon with geysers and potential subsurface ocean";
    } else if (path.contains("ceres")) {
      return "The largest object in the asteroid belt";
    } else if (path.contains("alpha")) {
      return "An exoplanet in the Alpha Centauri system";
    } else if (path.contains("beta")) {
      return "An exoplanet with unique atmospheric composition";
    } else if (path.contains("gamma")) {
      return "An exoplanet with extreme environmental conditions";
    } else if (path.contains("glacio")) {
      return "An icy world with frozen landscapes";
    } else if (path.contains("gaia")) {
      return "A world with unique gravitational anomalies";
    } else {
      return "A mysterious world with unknown properties";
    }
  }

  /** Get required rocket tier from dimension ID */
  private static NoduleTiers getRequiredTierFromDimensionId(ResourceLocation dimensionId) {
    String path = dimensionId.getPath();

    // Determine tier based on planet type
    if (path.contains("moon") || path.contains("leo")) {
      return NoduleTiers.T1;
    } else if (path.contains("mars") || path.contains("venus")) {
      return NoduleTiers.T2;
    } else if (path.contains("jupiter") || path.contains("saturn")) {
      return NoduleTiers.T3;
    } else if (path.contains("uranus") || path.contains("neptune")) {
      return NoduleTiers.T4;
    } else if (path.contains("pluto") || path.contains("europa") || path.contains("titan")) {
      return NoduleTiers.T5;
    } else if (path.contains("ganymede") || path.contains("triton") || path.contains("enceladus")) {
      return NoduleTiers.T6;
    } else if (path.contains("ceres") || path.contains("alpha")) {
      return NoduleTiers.T7;
    } else if (path.contains("beta") || path.contains("gamma")) {
      return NoduleTiers.T8;
    } else if (path.contains("glacio") || path.contains("gaia")) {
      return NoduleTiers.T9;
    } else {
      return NoduleTiers.T10;
    }
  }

  /** Get suit tag from dimension ID */
  private static String getSuitTagFromDimensionId(ResourceLocation dimensionId) {
    String path = dimensionId.getPath();

    // Determine suit requirement based on planet type
    if (path.contains("moon") || path.contains("leo")) {
      return "chex:suits/suit1";
    } else if (path.contains("mars") || path.contains("venus")) {
      return "chex:suits/suit2";
    } else if (path.contains("jupiter") || path.contains("saturn")) {
      return "chex:suits/suit3";
    } else if (path.contains("uranus") || path.contains("neptune")) {
      return "chex:suits/suit4";
    } else {
      return "chex:suits/suit5";
    }
  }

  /** Get minerals from dimension ID */
  private static Set<String> getMineralsFromDimensionId(ResourceLocation dimensionId) {
    String path = dimensionId.getPath();

    // Determine minerals based on planet type
    if (path.contains("moon")) {
      return Set.of("iron", "silicon", "aluminum", "titanium");
    } else if (path.contains("mars")) {
      return Set.of("iron", "nickel", "sulfur", "water");
    } else if (path.contains("venus")) {
      return Set.of("sulfur", "carbon_dioxide", "nitrogen");
    } else if (path.contains("jupiter") || path.contains("saturn")) {
      return Set.of("hydrogen", "helium", "methane", "ammonia");
    } else if (path.contains("uranus") || path.contains("neptune")) {
      return Set.of("hydrogen", "helium", "methane", "water");
    } else if (path.contains("europa") || path.contains("titan")) {
      return Set.of("water", "methane", "nitrogen", "organic_compounds");
    } else if (path.contains("ceres")) {
      return Set.of("water", "clay", "carbonates", "organic_compounds");
    } else if (path.contains("alpha") || path.contains("beta") || path.contains("gamma")) {
      return Set.of("iron", "silicon", "aluminum", "exotic_elements");
    } else if (path.contains("glacio")) {
      return Set.of("ice", "water", "frozen_gases", "organic_compounds");
    } else if (path.contains("gaia")) {
      return Set.of("iron", "silicon", "aluminum", "gravitational_anomalies");
    } else {
      return Set.of("iron", "silicon", "aluminum", "unknown_elements");
    }
  }

  /** Get biome type from dimension ID */
  private static String getBiomeTypeFromDimensionId(ResourceLocation dimensionId) {
    String path = dimensionId.getPath();

    // Determine biome type based on planet type
    if (path.contains("moon")) {
      return "lunar";
    } else if (path.contains("mars")) {
      return "desert";
    } else if (path.contains("venus")) {
      return "volcanic";
    } else if (path.contains("jupiter") || path.contains("saturn")) {
      return "gas_giant";
    } else if (path.contains("uranus") || path.contains("neptune")) {
      return "ice_giant";
    } else if (path.contains("europa") || path.contains("titan")) {
      return "icy_moon";
    } else if (path.contains("ceres")) {
      return "asteroid";
    } else if (path.contains("alpha") || path.contains("beta") || path.contains("gamma")) {
      return "exoplanet";
    } else if (path.contains("glacio")) {
      return "ice_world";
    } else if (path.contains("gaia")) {
      return "anomalous";
    } else {
      return "unknown";
    }
  }

  /** Check if Cosmic Horizons is loaded */
  public static boolean isCosmicHorizonsLoaded() {
    return isCosmicHorizonsLoaded;
  }

  /** Check if Cosmos is loaded */
  public static boolean isCosmosLoaded() {
    return isCosmosLoaded;
  }

  /** Get integration status */
  public static String getIntegrationStatus() {
    if (isCosmicHorizonsLoaded) {
      return "Cosmic Horizons - Full Integration";
    } else if (isCosmosLoaded) {
      return "Cosmos - Basic Integration";
    } else {
      return "Standalone Mode";
    }
  }
}
