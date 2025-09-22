package com.netroaki.chex.config;

import com.google.gson.*;
import com.netroaki.chex.CHEX;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class PlanetOverridesCore {
  public static final class Entry {
    public final int requiredRocketTier;
    public final String requiredSuitTag;
    public final String name;
    public final String fuelType;
    public final String description;
    public final Set<String> hazards;
    public final Set<String> availableMinerals;
    public final String biomeType;
    public final Integer gravity;
    public final Boolean hasAtmosphere;
    public final Boolean requiresOxygen;
    public final Boolean isOrbit;
    public final Float temperature;
    public final Integer radiationLevel;
    public final Float baseOxygen;

    public Entry(int requiredRocketTier, String requiredSuitTag, String name, String fuelType, 
                String description, Set<String> hazards, Set<String> availableMinerals,
                String biomeType, Integer gravity, Boolean hasAtmosphere, Boolean requiresOxygen,
                Boolean isOrbit, Float temperature, Integer radiationLevel, Float baseOxygen) {
      this.requiredRocketTier = requiredRocketTier;
      this.requiredSuitTag = requiredSuitTag != null ? requiredSuitTag : "";
      this.name = name != null ? name : "";
      this.fuelType = fuelType != null ? fuelType : "";
      this.description = description != null ? description : "";
      this.hazards = hazards != null ? Set.copyOf(hazards) : Set.of();
      this.availableMinerals = availableMinerals != null ? Set.copyOf(availableMinerals) : Set.of();
      this.biomeType = biomeType != null ? biomeType : "";
      this.gravity = gravity;
      this.hasAtmosphere = hasAtmosphere;
      this.requiresOxygen = requiresOxygen;
      this.isOrbit = isOrbit;
      this.temperature = temperature;
      this.radiationLevel = radiationLevel;
      this.baseOxygen = baseOxygen;
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
            JsonObject section = e.getValue().getAsJsonObject();
            for (Map.Entry<String, JsonElement> pe : section.entrySet()) {
              if (pe.getValue().isJsonObject()) {
                JsonObject obj = pe.getValue().getAsJsonObject();
                
                // Required fields with defaults
                int tier = obj.has("requiredRocketTier")
                    ? Math.max(1, obj.get("requiredRocketTier").getAsInt())
                    : 1;
                String suit = obj.has("requiredSuitTag")
                    ? obj.get("requiredSuitTag").getAsString()
                    : "chex:suits/suit1";
                
                // Optional fields
                String name = obj.has("name") ? obj.get("name").getAsString() : null;
                String fuelType = obj.has("fuelType") ? obj.get("fuelType").getAsString() : null;
                String description = obj.has("description") ? obj.get("description").getAsString() : null;
                
                // Collections
                Set<String> hazards = new HashSet<>();
                if (obj.has("hazards") && obj.get("hazards").isJsonArray()) {
                  JsonArray hazardsArray = obj.getAsJsonArray("hazards");
                  for (JsonElement hazard : hazardsArray) {
                    hazards.add(hazard.getAsString());
                  }
                }
                
                Set<String> minerals = new HashSet<>();
                if (obj.has("availableMinerals") && obj.get("availableMinerals").isJsonArray()) {
                  JsonArray mineralsArray = obj.getAsJsonArray("availableMinerals");
                  for (JsonElement mineral : mineralsArray) {
                    minerals.add(mineral.getAsString());
                  }
                }
                
                // Environment fields
                String biomeType = obj.has("biomeType") ? obj.get("biomeType").getAsString() : null;
                Integer gravity = obj.has("gravity") ? obj.get("gravity").getAsInt() : null;
                Boolean hasAtmosphere = obj.has("hasAtmosphere") ? obj.get("hasAtmosphere").getAsBoolean() : null;
                Boolean requiresOxygen = obj.has("requiresOxygen") ? obj.get("requiresOxygen").getAsBoolean() : null;
                Boolean isOrbit = obj.has("isOrbit") ? obj.get("isOrbit").getAsBoolean() : null;
                Float temperature = obj.has("temperature") ? obj.get("temperature").getAsFloat() : null;
                Integer radiationLevel = obj.has("radiationLevel") ? obj.get("radiationLevel").getAsInt() : null;
                Float baseOxygen = obj.has("baseOxygen") ? obj.get("baseOxygen").getAsFloat() : null;
                
                map.put(pe.getKey(), new Entry(
                    tier, suit, name, fuelType, description, 
                    hazards.isEmpty() ? null : hazards, 
                    minerals.isEmpty() ? null : minerals,
                    biomeType, gravity, hasAtmosphere, requiresOxygen, isOrbit,
                    temperature, radiationLevel, baseOxygen
                ));
              }
            }
          }
        }
        return Optional.of(map);
      }
    } catch (Exception e) {
      CHEX.LOGGER.error("Failed to load planet overrides from " + file, e);
      return Optional.empty();
    }
  }
}
