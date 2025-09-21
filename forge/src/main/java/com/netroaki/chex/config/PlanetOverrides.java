package com.netroaki.chex.config;

import com.netroaki.chex.CHEX;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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
    public int requiredRocketTier = 1;
    public String requiredSuitTag = "chex:suit1";
    public String name = null;
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
                    en.requiredRocketTier = e.requiredRocketTier;
                    en.requiredSuitTag = e.requiredSuitTag;
                    en.name = e.name;
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
