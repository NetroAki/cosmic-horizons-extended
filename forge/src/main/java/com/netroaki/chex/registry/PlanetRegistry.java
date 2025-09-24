package com.netroaki.chex.registry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.config.CHEXConfig;
import com.netroaki.chex.config.PlanetOverrideMerger;
import com.netroaki.chex.config.PlanetOverridesCore;
import com.netroaki.chex.integration.CosmicHorizonsIntegration;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

/** Registry for all planets in the CHEX mod system. */
public class PlanetRegistry {

  private static final Map<ResourceLocation, PlanetDef> PLANETS = new HashMap<>();
  private static final Map<ResourceLocation, PlanetDef> CHEX_PLANETS = new HashMap<>();
  private static final Set<ResourceLocation> DISCOVERED_PLANET_IDS = new HashSet<>();
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private static void clearDiscoveredPlanets() {
    if (DISCOVERED_PLANET_IDS.isEmpty()) {
      return;
    }
    DISCOVERED_PLANET_IDS.forEach(PLANETS::remove);
    DISCOVERED_PLANET_IDS.clear();
  }

  private static void registerDiscoveredPlanet(PlanetDef planet) {
    registerPlanet(planet.id(), planet);
    DISCOVERED_PLANET_IDS.add(planet.id());
  }

  public static void initialize() {
    CHEX.LOGGER.info("Initializing planet registry...");

    // Initialize Cosmic Horizons integration
    CosmicHorizonsIntegration.initialize();

    registerExistingCHPlanets();
    registerCHEXPlanets();

    CHEX.LOGGER.info(
        "Planet registry initialized with {} total planets ({} CHEX planets)",
        PLANETS.size(),
        CHEX_PLANETS.size());
    CHEX.LOGGER.info("Integration status: {}", CosmicHorizonsIntegration.getIntegrationStatus());
  }

  private static void registerExistingCHPlanets() {
    Map<ResourceLocation, PlanetOverridesCore.Entry> overrides = loadPlanetOverrides();

    if (registerDiscoveredPlanets(overrides)) {
      return;
    }

    CHEX.LOGGER.info("[CHEX] Falling back to static Cosmic Horizons planet definitions.");

    PlanetDef earthMoon =
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath("cosmos", "earth_moon"),
            "Earth Moon",
            "The natural satellite of Earth",
            NoduleTiers.T1,
            "chex:suits/suit1",
            "minecraft:lava",
            1,
            false,
            true,
            Set.of("vacuum"),
            Set.of("iron", "silicon", "aluminum"),
            "lunar",
            false);
    registerDiscoveredPlanet(applyOverrides(earthMoon, overrides));

    PlanetDef mercury =
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath("cosmos", "mercury_wasteland"),
            "Mercury",
            "The closest planet to the Sun",
            NoduleTiers.T2,
            "chex:suits/suit1",
            "minecraft:lava",
            2,
            false,
            true,
            Set.of("vacuum", "radiation"),
            Set.of("iron", "nickel", "sulfur"),
            "barren",
            false);
    registerDiscoveredPlanet(applyOverrides(mercury, overrides));

    PlanetDef mars =
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath("cosmos", "marslands"),
            "Mars",
            "The red planet",
            NoduleTiers.T2,
            "chex:suits/suit1",
            "minecraft:lava",
            2,
            true,
            false,
            Set.of("vacuum", "cryogenic"),
            Set.of("iron", "silicon", "aluminum", "water"),
            "desert",
            false);
    registerDiscoveredPlanet(applyOverrides(mars, overrides));
  }

  public static void reloadDiscoveredPlanets() {
    Map<ResourceLocation, PlanetOverridesCore.Entry> overrides = loadPlanetOverrides();
    registerDiscoveredPlanets(overrides);
  }

  private static PlanetDef buildPlanetFromDiscovery(JsonObject obj) {
    if (!obj.has("id")) {
      return null;
    }
    String idString = obj.get("id").getAsString();
    ResourceLocation id = ResourceLocation.tryParse(idString);
    if (id == null) {
      CHEX.LOGGER.warn("[CHEX] Skipping discovered planet with invalid id '{}'.", idString);
      return null;
    }

    String name = obj.has("name") ? obj.get("name").getAsString() : id.getPath();
    String description =
        obj.has("description")
            ? obj.get("description").getAsString()
            : String.format("Discovered planet %s", name);

    int tierValue = obj.has("requiredNoduleTier") ? obj.get("requiredNoduleTier").getAsInt() : 1;
    NoduleTiers tier = NoduleTiers.getByTier(tierValue);
    if (tier == null) {
      tier = NoduleTiers.T1;
    }

    String suitTag =
        obj.has("requiredSuitTag")
            ? obj.get("requiredSuitTag").getAsString()
            : tier.getRequiredSuitTagString();
    String fuel = CHEXConfig.getFuelForTier(tier);

    int gravityLevel = obj.has("gravityLevel") ? obj.get("gravityLevel").getAsInt() : 1;
    boolean hasAtmosphere =
        obj.has("hasAtmosphere") ? obj.get("hasAtmosphere").getAsBoolean() : true;
    boolean requiresOxygen =
        obj.has("requiresOxygen") ? obj.get("requiresOxygen").getAsBoolean() : true;
    boolean isOrbit = obj.has("isOrbit") ? obj.get("isOrbit").getAsBoolean() : false;
    String biomeType = obj.has("source") ? obj.get("source").getAsString() : "cosmos";

    Set<String> minerals = Collections.emptySet();
    if (obj.has("availableMinerals") && obj.get("availableMinerals").isJsonArray()) {
      HashSet<String> mineralSet = new HashSet<>();
      for (JsonElement mineralElement : obj.getAsJsonArray("availableMinerals")) {
        if (mineralElement.isJsonPrimitive()) {
          String mineralId = mineralElement.getAsString();
          if (!mineralId.isEmpty()) {
            mineralSet.add(mineralId);
          }
        }
      }
      if (!mineralSet.isEmpty()) {
        minerals = Set.copyOf(mineralSet);
      }
    }

    return new PlanetDef(
        id,
        name,
        description,
        tier,
        suitTag,
        fuel,
        gravityLevel,
        hasAtmosphere,
        requiresOxygen,
        Set.of(),
        minerals,
        biomeType,
        isOrbit);
  }

  private static void registerCHEXPlanets() {
    // Alpha Centauri A
    registerCHEXPlanet(
        ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "alpha_centauri_a"),
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "alpha_centauri_a"),
            "Alpha Centauri A",
            "The primary star of the Alpha Centauri system",
            NoduleTiers.T8,
            "chex:suits/suit4",
            "chex:lh2",
            5,
            true,
            false,
            Set.of("radiation", "extreme_heat"),
            Set.of("hydrogen", "helium", "lithium"),
            "stellar",
            false));

    // Kepler-452b
    registerCHEXPlanet(
        ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "kepler_452b"),
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "kepler_452b"),
            "Kepler-452b",
            "Earth's older cousin",
            NoduleTiers.T9,
            "chex:suits/suit5",
            "chex:lh2",
            3,
            true,
            true,
            Set.of("radiation"),
            Set.of("iron", "silicon", "aluminum", "water", "carbon"),
            "temperate",
            false));

    // PANDORA - consolidated planet (multiple sub-biomes)
    registerCHEXPlanet(
        ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora"),
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora"),
            "Pandora",
            "A lush world spanning forests, floating mountains, oceans, volcanic zones and sky"
                + " islands",
            NoduleTiers.T6,
            "chex:suits/suit3",
            "chex:rp1",
            4,
            true,
            true,
            Set.of("acid", "toxic_atmosphere"),
            Set.of(
                "unobtanium",
                "bioluminescent_crystal",
                "exotic_wood",
                "crystal_formations",
                "aquatic_crystals",
                "magnetic_ore",
                "wind_crystals",
                "cloud_essence",
                "deep_sea_crystal",
                "bioluminescent_essence",
                "oceanic_metal",
                "thermal_vent_crystals",
                "kelp_essence",
                "volcanic_crystal",
                "molten_metal",
                "thermal_energy_core",
                "ash_crystals",
                "lava_essence",
                "sky_crystal",
                "levitation_essence",
                "cloud_metal",
                "atmospheric_crystals",
                "wind_essence"),
            "pandora",
            false));

    // ARRAKIS - consolidated planet (multiple sub-biomes)
    registerCHEXPlanet(
        ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "arrakis"),
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "arrakis"),
            "Arrakis",
            "A harsh desert world spanning dunes, spice mines, polar caps, sietches and stormlands",
            NoduleTiers.T6,
            "chex:suits/suit3",
            "chex:kerosene",
            4,
            true,
            true,
            Set.of("vacuum", "cryogenic", "sandstorms"),
            Set.of(
                "spice_melange",
                "desert_glass",
                "sandworm_essence",
                "crystalline_salt",
                "water_crystals",
                "pure_spice",
                "crystallized_spice",
                "spice_essence",
                "mining_equipment",
                "industrial_crystals",
                "ice_spice",
                "polar_crystal",
                "frozen_essence",
                "ice_formations",
                "permafrost_crystals",
                "refined_spice",
                "sietch_technology",
                "fremen_artifacts",
                "water_reclamation",
                "defense_systems",
                "storm_spice",
                "lightning_crystal",
                "storm_essence",
                "electromagnetic_zones",
                "dust_devil_essence"),
            "arrakis",
            false));

    // EXOTICA SYSTEM (Unique Planetary Features)
    registerCHEXPlanet(
        ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "ringworld_prime"),
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "ringworld_prime"),
            "Ringworld Prime",
            "A planet with massive ring systems and unique low-gravity mining",
            NoduleTiers.T6,
            "chex:suits/suit3",
            "chex:rp1",
            4,
            true,
            true,
            Set.of("low_gravity", "radiation"),
            Set.of(
                "ring_particles",
                "ice_crystals",
                "metallic_dust",
                "solar_essence",
                "low_gravity_ore"),
            "ring_system",
            false));

    registerCHEXPlanet(
        ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "aqua_mundus"),
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "aqua_mundus"),
            "Aqua Mundus",
            "A water world with massive ice core and underwater ecosystems",
            NoduleTiers.T7,
            "chex:suits/suit4",
            "chex:lox",
            3,
            true,
            true,
            Set.of("high_pressure", "cryogenic"),
            Set.of(
                "ice_core",
                "underwater_crystals",
                "aquatic_metals",
                "frozen_gases",
                "deep_sea_essence"),
            "water_world",
            false));

    registerCHEXPlanet(
        ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "inferno_prime"),
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "inferno_prime"),
            "Inferno Prime",
            "A lava world with floating obsidian islands and extreme heat",
            NoduleTiers.T8,
            "chex:suits/suit4",
            "chex:lh2",
            2,
            false,
            false,
            Set.of("extreme_heat", "acid", "radiation"),
            Set.of(
                "molten_ore",
                "obsidian_crystals",
                "volcanic_glass",
                "lava_essence",
                "heat_resistant_metals"),
            "lava_world",
            false));

    registerCHEXPlanet(
        ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "crystalis"),
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "crystalis"),
            "Crystalis",
            "An ice giant with diamond core and diamond rain phenomena",
            NoduleTiers.T9,
            "chex:suits/suit5",
            "chex:lh2",
            1,
            false,
            false,
            Set.of("cryogenic", "high_pressure"),
            Set.of(
                "diamond_core",
                "ice_crystals",
                "frozen_gases",
                "diamond_rain",
                "pressure_crystals"),
            "ice_giant",
            false));

    registerCHEXPlanet(
        ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "stormworld"),
        new PlanetDef(
            ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "stormworld"),
            "Stormworld",
            "A gas giant with massive storm systems and atmospheric mining",
            NoduleTiers.T10,
            "chex:suits/suit5",
            "chex:lh2",
            0,
            false,
            false,
            Set.of("extreme_weather", "lightning", "high_pressure"),
            Set.of(
                "atmospheric_gases",
                "lightning_crystals",
                "storm_essence",
                "wind_energy",
                "gas_giant_core"),
            "gas_giant",
            false));
  }

  public static void registerPlanet(ResourceLocation id, PlanetDef planet) {
    PLANETS.put(id, planet);
    CHEX.LOGGER.debug("Registered planet: {} - {}", id, planet.name());
  }

  private static void registerCHEXPlanet(ResourceLocation id, PlanetDef planet) {
    registerPlanet(id, planet);
    CHEX_PLANETS.put(id, planet);
    CHEX.LOGGER.debug("Registered CHEX planet: {} - {}", id, planet.name());
  }

  public static PlanetDef getPlanet(ResourceLocation id) {
    return PLANETS.get(id);
  }

  public static Map<ResourceLocation, PlanetDef> getAllPlanets() {
    return new HashMap<>(PLANETS);
  }

  public static Map<ResourceLocation, PlanetDef> getCHEXPlanets() {
    return new HashMap<>(CHEX_PLANETS);
  }

  public static Map<ResourceLocation, PlanetDef> getPlanetsForTier(NoduleTiers tier) {
    Map<ResourceLocation, PlanetDef> accessible = new HashMap<>();
    for (Map.Entry<ResourceLocation, PlanetDef> entry : PLANETS.entrySet()) {
      if (entry.getValue().requiredRocketTier().getTier() <= tier.getTier()) {
        accessible.put(entry.getKey(), entry.getValue());
      }
    }
    return accessible;
  }

  public static boolean isPlanetAccessible(ResourceLocation planetId, NoduleTiers rocketTier) {
    PlanetDef planet = getPlanet(planetId);
    if (planet == null) return false;
    return planet.requiredRocketTier().getTier() <= rocketTier.getTier();
  }

  /** Load planet overrides from chex-planets.json5 configuration file */
  private static Map<ResourceLocation, PlanetOverridesCore.Entry> loadPlanetOverrides() {
    Path configFile = FMLPaths.CONFIGDIR.get().resolve("chex").resolve("chex-planets.json5");
    Optional<Map<String, PlanetOverridesCore.Entry>> overrides =
        PlanetOverridesCore.load(configFile);

    if (overrides.isEmpty()) {
      CHEX.LOGGER.debug("[CHEX] No planet overrides found at {}", configFile);
      return Collections.emptyMap();
    }

    Map<ResourceLocation, PlanetOverridesCore.Entry> result = new HashMap<>();
    for (Map.Entry<String, PlanetOverridesCore.Entry> entry : overrides.get().entrySet()) {
      try {
        ResourceLocation planetId = ResourceLocation.parse(entry.getKey());
        result.put(planetId, entry.getValue());
      } catch (Exception e) {
        CHEX.LOGGER.warn("[CHEX] Invalid planet ID in overrides: {}", entry.getKey());
      }
    }

    CHEX.LOGGER.info("[CHEX] Loaded {} planet overrides from {}", result.size(), configFile);
    return result;
  }

  /** 
   * Apply overrides to a planet definition.
   * @param base The base planet definition to apply overrides to
   * @param overrides Map of planet IDs to their override entries
   * @return A new PlanetDef with overrides applied
   */
  private static PlanetDef applyOverrides(
      PlanetDef base, Map<ResourceLocation, PlanetOverridesCore.Entry> overrides) {
    // If no override exists for this planet, return the base definition
    PlanetOverridesCore.Entry override = overrides.get(base.id());
    if (override == null) {
      return base;
    }

    // Create PlanetInfo for merging with all base values
    PlanetOverrideMerger.PlanetInfo baseInfo =
        new PlanetOverrideMerger.PlanetInfo(
            base.name(),
            base.requiredRocketTier().getTier(),
            base.requiredSuitTag(),
            base.fuelType(),
            base.description(),
            base.gravityLevel(),
            base.hasAtmosphere(),
            base.requiresOxygen(),
            base.hazards(),
            base.availableMinerals(),
            base.biomeType(),
            base.isOrbit());

    // Merge with overrides
    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(baseInfo, override);

    // Convert back to PlanetDef with all fields
    NoduleTiers tier = NoduleTiers.getByTier(merged.requiredRocketTier());
    if (tier == null) {
      tier = base.requiredRocketTier();
    }

    return new PlanetDef(
        base.id(),
        merged.name(),
        merged.description(),
        tier,
        merged.requiredSuitTag(),
        merged.fuel(),
        merged.gravityLevel(),
        merged.hasAtmosphere(),
        merged.requiresOxygen(),
        merged.hazards(),
        merged.availableMinerals(),
        merged.biomeType(),
        merged.isOrbit());
  }

  /** Register discovered planets with overrides applied */
  private static boolean registerDiscoveredPlanets(
      Map<ResourceLocation, PlanetOverridesCore.Entry> overrides) {
    Path discoveryFile =
        FMLPaths.CONFIGDIR.get().resolve("chex").resolve("_discovered_planets.json");
    if (!Files.exists(discoveryFile)) {
      CHEX.LOGGER.info(
          "[CHEX] No discovered planets snapshot found at {} - skipping Cosmic Horizons sync.",
          discoveryFile);
      return false;
    }

    clearDiscoveredPlanets();

    int registered = 0;
    List<String> rows = new ArrayList<>();
    try (BufferedReader reader = Files.newBufferedReader(discoveryFile)) {
      JsonObject root = GSON.fromJson(reader, JsonObject.class);
      if (root == null || !root.has("planets") || !root.get("planets").isJsonArray()) {
        CHEX.LOGGER.warn("[CHEX] Discovery file {} is missing a 'planets' array.", discoveryFile);
        return false;
      }

      JsonArray planets = root.getAsJsonArray("planets");
      for (JsonElement element : planets) {
        if (!element.isJsonObject()) {
          continue;
        }
        PlanetDef definition = buildPlanetFromDiscovery(element.getAsJsonObject());
        if (definition == null) {
          continue;
        }

        // Apply overrides to discovered planet
        PlanetDef overriddenDefinition = applyOverrides(definition, overrides);
        registerDiscoveredPlanet(overriddenDefinition);
        registered++;

        String row =
            String.format(
                "%-38s | %-22s | %-3s | %-18s | %s",
                overriddenDefinition.id(),
                overriddenDefinition.name(),
                "T" + overriddenDefinition.requiredRocketTier().getTier(),
                overriddenDefinition.requiredSuitTag(),
                overriddenDefinition.id().getNamespace());
        rows.add(row);
      }
    } catch (IOException ex) {
      CHEX.LOGGER.warn("[CHEX] Failed to read discovered planets from {}: {}", discoveryFile, ex);
      return false;
    }

    if (registered > 0) {
      String header =
          String.format(
              "%-38s | %-22s | %-3s | %-18s | %s",
              "Planet ID", "Name", "Tier", "Suit Tag", "Source");
      String separator = "-".repeat(header.length());
      CHEX.LOGGER.info(
          "[CHEX] Registered {} Cosmic Horizons planets from {} (with overrides applied).",
          registered,
          discoveryFile);
      CHEX.LOGGER.info("[CHEX] {}", header);
      CHEX.LOGGER.info("[CHEX] {}", separator);
      for (String row : rows) {
        CHEX.LOGGER.info("[CHEX] {}", row);
      }
      return true;
    }

    CHEX.LOGGER.warn("[CHEX] Discovery file {} contained no usable planet entries.", discoveryFile);
    return false;
  }
}
