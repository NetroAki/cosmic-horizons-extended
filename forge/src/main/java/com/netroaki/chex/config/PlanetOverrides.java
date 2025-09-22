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
          + "  \"cosmichorizons:earth_moon\": { \n"
          + "    \"requiredRocketTier\": 2, \n"
          + "    \"requiredSuitTag\": \"cosmic_horizons_extended:suit1\", \n"
          + "    \"name\": \"Moon\",
"
          + "    \"description\": \"Earth's natural satellite with low gravity and no atmosphere\",
"
          + "    \"fuel\": \"minecraft:coal\",
"
          + "    \"hazards\": [\"vacuum\", \"radiation\"],
"
          + "    \"minerals\": [\"minecraft:iron_ore\", \"minecraft:gold_ore\"]
"
          + "  },\n"
          + "  \"cosmichorizons:mars_lands\": { \n"
          + "    \"requiredRocketTier\": 4, 
"
          + "    \"requiredSuitTag\": \"cosmic_horizons_extended:suit2\", 
"
          + "    \"name\": \"Mars\",
"
          + "    \"description\": \"The red planet with a thin atmosphere\",
"
          + "    \"biomeType\": \"desert\",
"
          + "    \"gravity\": 3,
"
          + "    \"hasAtmosphere\": true,
"
          + "    \"requiresOxygen\": true,
"
          + "    \"isOrbit\": false
"
          + "  },\n"
          + "  \"cosmic_horizons_extended:exoplanet_alpha\": { 
"
          + "    \"requiredRocketTier\": 6,
"
          + "    \"requiredSuitTag\": \"cosmic_horizons_extended:suit3\", 
"
          + "    \"name\": \"Exoplanet Alpha\",
"
          + "    \"description\": \"A mysterious exoplanet with unknown properties\",
"
          + "    \"hazards\": [\"toxic_gas\", \"heat\"],
"
          + "    \"minerals\": [\"minecraft:diamond_ore\", \"minecraft:emerald_ore\"]
"
          + "  }\n"
          + "}\n";

  public static final class Entry {
    public int requiredRocketTier = 1;
    public String requiredSuitTag = "chex:suit1";
    public String name = null;
    public String description = "";
    public String fuel = "";
    public Set<String> hazards = new HashSet<>();
    public Set<String> minerals = new HashSet<>();
    public String biomeType = "";
    public Integer gravity = null;
    public Boolean hasAtmosphere = null;
    public Boolean requiresOxygen = null;
    public Boolean isOrbit = null;
    
    public Entry() {
      // Initialize with empty sets to avoid NPE
      this.hazards = new HashSet<>();
      this.minerals = new HashSet<>();
    }
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
                    if (e.requiredRocketTier() != null) {
                      en.requiredRocketTier = e.requiredRocketTier();
                    }
                    if (e.requiredSuitTag() != null) {
                      en.requiredSuitTag = e.requiredSuitTag();
                    } else if (e.requiredSuitTier() != null) {
                      en.requiredSuitTag =
                          String.format("chex:suits/suit%d", Math.max(1, Math.min(5, e.requiredSuitTier())));
                    }
                    if (e.name() != null) {
                      en.name = e.name();
                    }
                    if (e.description() != null) {
                      en.description = e.description();
                    }
                    if (e.fuel() != null) {
                      en.fuel = e.fuel();
                    }
                    if (e.hazards() != null && !e.hazards().isEmpty()) {
                      en.hazards = Set.copyOf(e.hazards());
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
