package com.netroaki.chex.config.legacy;

import com.google.gson.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class PlanetOverridesCore {
  public static final class Entry {
    public final Integer requiredRocketTier;
    public final String requiredSuitTag;
    public final String name;
    public final String description;
    public final String fuelType;
    public final Integer gravityLevel;
    public final Boolean hasAtmosphere;
    public final Boolean requiresOxygen;
    public final Set<String> hazards;
    public final Set<String> availableMinerals;
    public final String biomeType;
    public final Boolean isOrbit;

    public Entry(
        Integer requiredRocketTier,
        String requiredSuitTag,
        String name,
        String description,
        String fuelType,
        Integer gravityLevel,
        Boolean hasAtmosphere,
        Boolean requiresOxygen,
        Set<String> hazards,
        Set<String> availableMinerals,
        String biomeType,
        Boolean isOrbit) {
      this.requiredRocketTier = requiredRocketTier;
      this.requiredSuitTag = requiredSuitTag;
      this.name = name;
      this.description = description;
      this.fuelType = fuelType;
      this.gravityLevel = gravityLevel;
      this.hasAtmosphere = hasAtmosphere;
      this.requiresOxygen = requiresOxygen;
      this.hazards = hazards != null ? Set.copyOf(hazards) : Set.of();
      this.availableMinerals = availableMinerals != null ? Set.copyOf(availableMinerals) : Set.of();
      this.biomeType = biomeType;
      this.isOrbit = isOrbit;
    }
  }

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private PlanetOverridesCore() {}

  public static Optional<Map<String, Entry>> load(Path file) {
    try {
      if (!Files.exists(file)) {
        return Optional.empty();
      }
      try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
        com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
        jr.setLenient(true);
        JsonObject root = GSON.fromJson(jr, JsonObject.class);
        Map<String, Entry> map = new HashMap<>();

        for (Map.Entry<String, JsonElement> e : root.entrySet()) {
          if (e.getValue().isJsonObject()) {
            JsonObject obj = e.getValue().getAsJsonObject();

            // Required fields with defaults
            Integer tier =
                obj.has("requiredRocketTier")
                    ? Math.max(1, obj.get("requiredRocketTier").getAsInt())
                    : null;

            String suit =
                obj.has("requiredSuitTag") ? obj.get("requiredSuitTag").getAsString() : null;

            // Optional fields
            String name = obj.has("name") ? obj.get("name").getAsString() : null;
            String description =
                obj.has("description") ? obj.get("description").getAsString() : null;
            String fuelType = obj.has("fuelType") ? obj.get("fuelType").getAsString() : null;
            Integer gravityLevel =
                obj.has("gravityLevel") ? obj.get("gravityLevel").getAsInt() : null;
            Boolean hasAtmosphere =
                obj.has("hasAtmosphere") ? obj.get("hasAtmosphere").getAsBoolean() : null;
            Boolean requiresOxygen =
                obj.has("requiresOxygen") ? obj.get("requiresOxygen").getAsBoolean() : null;
            String biomeType = obj.has("biomeType") ? obj.get("biomeType").getAsString() : null;
            Boolean isOrbit = obj.has("isOrbit") ? obj.get("isOrbit").getAsBoolean() : null;

            // Handle hazards array
            Set<String> hazards = new java.util.HashSet<>();
            if (obj.has("hazards") && obj.get("hazards").isJsonArray()) {
              for (JsonElement hazard : obj.getAsJsonArray("hazards")) {
                hazards.add(hazard.getAsString().toLowerCase());
              }
            }

            // Handle availableMinerals array
            Set<String> availableMinerals = new java.util.HashSet<>();
            if (obj.has("availableMinerals") && obj.get("availableMinerals").isJsonArray()) {
              for (JsonElement mineral : obj.getAsJsonArray("availableMinerals")) {
                availableMinerals.add(mineral.getAsString().toLowerCase());
              }
            }

            map.put(
                e.getKey(),
                new Entry(
                    tier,
                    suit,
                    name,
                    description,
                    fuelType,
                    gravityLevel,
                    hasAtmosphere,
                    requiresOxygen,
                    hazards,
                    availableMinerals,
                    biomeType,
                    isOrbit));
          }
        }
        return Optional.of(map);
      }
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
