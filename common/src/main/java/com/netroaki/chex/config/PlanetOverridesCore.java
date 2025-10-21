package com.netroaki.chex.config;

import com.google.gson.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class PlanetOverridesCore {
  /**
   * Represents a set of overrides that can be applied to a planet definition. All fields are
   * nullable, with null indicating that the field should not be overridden.
   */
  public static final class Entry {
    // Basic information
    private final String name;
    private final String description;

    // Requirements
    private final Integer requiredRocketTier;
    private final Integer requiredSuitTier;
    private final String requiredSuitTag;
    private final String fuelType;

    // Environmental properties
    private final Integer gravityLevel;
    private final Boolean hasAtmosphere;
    private final Boolean requiresOxygen;
    private final Boolean isOrbit;

    // Content
    private final Set<String> hazards;
    private final Set<String> availableMinerals;
    private final String biomeType;

    public Entry(
        Integer requiredRocketTier,
        Integer requiredSuitTier,
        String requiredSuitTag,
        String name,
        String description,
        String fuelType,
        Set<String> hazards,
        Integer gravityLevel,
        Boolean hasAtmosphere,
        Boolean requiresOxygen,
        Set<String> availableMinerals,
        String biomeType,
        Boolean isOrbit) {
      // Requirements
      this.requiredRocketTier = requiredRocketTier;
      this.requiredSuitTier = requiredSuitTier;
      this.requiredSuitTag = requiredSuitTag;
      this.fuelType = fuelType;

      // Basic information
      this.name = name;
      this.description = description;

      // Environmental properties
      this.gravityLevel = gravityLevel;
      this.hasAtmosphere = hasAtmosphere;
      this.requiresOxygen = requiresOxygen;
      this.isOrbit = isOrbit;

      // Content
      this.hazards = hazards != null ? Set.copyOf(hazards) : null;
      this.availableMinerals = availableMinerals != null ? Set.copyOf(availableMinerals) : null;
      this.biomeType = biomeType;
    }

    public Integer requiredRocketTier() {
      return requiredRocketTier;
    }

    public Integer requiredSuitTier() {
      return requiredSuitTier;
    }

    public String requiredSuitTag() {
      return requiredSuitTag;
    }

    public String name() {
      return name;
    }

    public String description() {
      return description;
    }

    public String fuelType() {
      return fuelType;
    }

    public Integer gravityLevel() {
      return gravityLevel;
    }

    public Boolean hasAtmosphere() {
      return hasAtmosphere;
    }

    public Boolean requiresOxygen() {
      return requiresOxygen;
    }

    public Boolean isOrbit() {
      return isOrbit;
    }

    public Set<String> availableMinerals() {
      return availableMinerals;
    }

    public String biomeType() {
      return biomeType;
    }

    public Set<String> hazards() {
      return hazards;
    }
  }

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private PlanetOverridesCore() {}

  /**
   * Loads planet overrides from a JSON5 file. The expected format is a flat JSON object where keys
   * are planet IDs and values are override objects. Example: { "cosmichorizons:earth_moon": {
   * "requiredRocketTier": 2, "name": "Moon" }, "cosmichorizons:mars_lands": { "requiredRocketTier":
   * 4, "requiredSuitTag": "chex:suits/suit2" } }
   */
  public static Optional<Map<String, Entry>> load(Path file) {
    try {
      if (!Files.exists(file)) {
        return Optional.empty();
      }
      try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
        com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
        jr.setLenient(true); // Allow comments and other JSON5 features
        JsonObject root = GSON.fromJson(jr, JsonObject.class);
        Map<String, Entry> map = new HashMap<>();

        // Iterate through each entry in the root object
        for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
          String planetId = entry.getKey();
          if (entry.getValue().isJsonObject()) {
            try {
              JsonObject overrideObj = entry.getValue().getAsJsonObject();
              Entry override = parseEntry(overrideObj);
              map.put(planetId, override);
            } catch (Exception e) {
              System.err.println(
                  "Failed to parse override for " + planetId + ": " + e.getMessage());
            }
          }
        }

        return Optional.of(map);
      }
    } catch (Exception e) {
      System.err.println("Failed to load planet overrides: " + e.getMessage());
      return Optional.empty();
    }
  }

  /**
   * Parses a JSON object into a PlanetOverridesCore.Entry object. Handles all possible fields that
   * can be overridden for a planet.
   */
  private static Entry parseEntry(JsonObject obj) {
    // Basic information
    String name = getString(obj, "name");
    String description = getString(obj, "description");

    // Requirements
    Integer requiredRocketTier = getInteger(obj, "requiredRocketTier", 1, 10);
    Integer requiredSuitTier = getInteger(obj, "requiredSuitTier", 1, 5);
    String requiredSuitTag = getString(obj, "requiredSuitTag");
    String fuelType = getString(obj, "fuelType");

    // Environmental properties
    Integer gravityLevel = getInteger(obj, "gravityLevel", 0, 10);
    Boolean hasAtmosphere = getBoolean(obj, "hasAtmosphere");
    Boolean requiresOxygen = getBoolean(obj, "requiresOxygen");
    Boolean isOrbit = getBoolean(obj, "isOrbit");

    // Content
    Set<String> hazards = parseStringSet(obj, "hazards");
    Set<String> availableMinerals = parseStringSet(obj, "availableMinerals");
    String biomeType = getString(obj, "biomeType");

    return new Entry(
        requiredRocketTier,
        requiredSuitTier,
        requiredSuitTag,
        name,
        description,
        fuelType,
        hazards,
        gravityLevel,
        hasAtmosphere,
        requiresOxygen,
        availableMinerals,
        biomeType,
        isOrbit);
  }

  private static Integer getInteger(JsonObject obj, String key, int min, int max) {
    if (!obj.has(key)) {
      return null;
    }
    try {
      int value = obj.get(key).getAsInt();
      if (value < min) value = min;
      if (value > max) value = max;
      return value;
    } catch (Exception ignored) {
      return null;
    }
  }

  /**
   * Parses a string set from a JSON array in the given object. Returns null if the key doesn't
   * exist or the value is not an array.
   */
  private static Set<String> parseStringSet(JsonObject obj, String key) {
    if (!obj.has(key) || !obj.get(key).isJsonArray()) {
      return null;
    }

    Set<String> result = new LinkedHashSet<>();
    JsonArray array = obj.getAsJsonArray(key);
    for (JsonElement element : array) {
      if (element.isJsonPrimitive()) {
        String value = element.getAsString().trim().toLowerCase();
        if (!value.isEmpty()) {
          result.add(value);
        }
      }
    }
    return result.isEmpty() ? null : result;
  }

  /** Gets a boolean value from a JSON object, returning null if the key doesn't exist. */
  private static Boolean getBoolean(JsonObject obj, String key) {
    if (!obj.has(key)) {
      return null;
    }
    try {
      return obj.get(key).getAsBoolean();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Gets a string value from a JSON object, returning null if the key doesn't exist or the value is
   * empty.
   */
  private static String getString(JsonObject obj, String key) {
    if (!obj.has(key)) {
      return null;
    }
    try {
      String value = obj.get(key).getAsString().trim();
      return value.isEmpty() ? null : value;
    } catch (Exception ignored) {
      return null;
    }
  }
}
