package com.netroaki.chex.config;

import com.netroaki.chex.CHEX;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class PlanetOverrides {
  public static final String FILE_NAME = "chex/chex-planets.json5";
  private static final String SAMPLE =
      "{\n"
          + "  \"cosmichorizons:earth_moon\": { \"requiredRocketTier\": 2, \"requiredSuitTag\":"
          + " \"cosmic_horizons_extended:suit1\", \"name\": \"Moon\" },\n"
          + "  \"cosmichorizons:mars_lands\": { \"requiredRocketTier\": 4, \"requiredSuitTag\":"
          + " \"cosmic_horizons_extended:suit2\", \"name\": \"Mars\" },\n"
          + "  \"cosmic_horizons_extended:exoplanet_alpha\": { \"requiredRocketTier\": 6,"
          + " \"requiredSuitTag\": \"cosmic_horizons_extended:suit3\", \"name\": \"Exoplanet"
          + " Alpha\" }\n"
          + "}\n";

  public static final class Entry {
    // Basic information
    public String name = null;
    public String description = "";

    // Requirements
    public int requiredRocketTier = 1;
    public String requiredSuitTag = "chex:suit1";
    public String fuelType = "minecraft:lava";

    // Environmental properties
    public int gravityLevel = 1;
    public boolean hasAtmosphere = true;
    public boolean requiresOxygen = true;

    // Content
    public Set<String> hazards = Set.of();
    public Set<String> availableMinerals = Set.of();
    public String biomeType = "default";
    public boolean isOrbit = false;
  }

  private final Map<String, Entry> overridesById = new HashMap<>();

  private PlanetOverrides() {}

  public static PlanetOverrides load(Path configDir) {
    PlanetOverrides data = new PlanetOverrides();
    try {
      Path path = configDir.resolve(FILE_NAME);
      File file = path.toFile();
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        Files.writeString(path, SAMPLE);
        CHEX.LOGGER.warn("CHEX wrote sample planet overrides to {}.", path);
        return data;
      }
      var core = PlanetOverridesCore.load(path);
      core.ifPresent(
          map ->
              map.forEach(
                  (id, e) -> {
                    Entry en = new Entry();

                    // Basic information
                    if (e.name() != null) {
                      en.name = e.name();
                    }
                    if (e.description() != null) {
                      en.description = e.description();
                    }

                    // Requirements
                    if (e.requiredRocketTier() != null) {
                      en.requiredRocketTier = e.requiredRocketTier();
                    }
                    if (e.requiredSuitTag() != null) {
                      en.requiredSuitTag = e.requiredSuitTag();
                    } else if (e.requiredSuitTier() != null) {
                      en.requiredSuitTag =
                          String.format(
                              "chex:suits/suit%d", Math.max(1, Math.min(5, e.requiredSuitTier())));
                    }
                    if (e.fuelType() != null) {
                      en.fuelType = e.fuelType();
                    }

                    // Environmental properties
                    if (e.gravityLevel() != null) {
                      en.gravityLevel = e.gravityLevel();
                    }
                    if (e.hasAtmosphere() != null) {
                      en.hasAtmosphere = e.hasAtmosphere();
                    }
                    if (e.requiresOxygen() != null) {
                      en.requiresOxygen = e.requiresOxygen();
                    }

                    // Content
                    if (e.hazards() != null && !e.hazards().isEmpty()) {
                      en.hazards = Set.copyOf(e.hazards());
                    }
                    if (e.availableMinerals() != null && !e.availableMinerals().isEmpty()) {
                      en.availableMinerals = Set.copyOf(e.availableMinerals());
                    }
                    if (e.biomeType() != null) {
                      en.biomeType = e.biomeType();
                    }
                    if (e.isOrbit() != null) {
                      en.isOrbit = e.isOrbit();
                    }
                    data.overridesById.put(id, en);
                  }));
    } catch (Exception ex) {
      CHEX.LOGGER.error("Failed to read planet overrides: {}", ex.toString());
    }
    return data;
  }

  public Map<String, Entry> getAll() {
    return overridesById;
  }
}
