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
  public static final class Entry {
    private final Integer requiredRocketTier;
    private final Integer requiredSuitTier;
    private final String requiredSuitTag;
    private final String name;
    private final String description;
    private final String fuel;
    private final Set<String> hazards;

    public Entry(
        Integer requiredRocketTier,
        Integer requiredSuitTier,
        String requiredSuitTag,
        String name,
        String description,
        String fuel,
        Set<String> hazards) {
      this.requiredRocketTier = requiredRocketTier;
      this.requiredSuitTier = requiredSuitTier;
      this.requiredSuitTag = requiredSuitTag;
      this.name = name;
      this.description = description;
      this.fuel = fuel;
      this.hazards = hazards;
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

    public String fuel() {
      return fuel;
    }

    public Set<String> hazards() {
      return hazards;
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
                map.put(pe.getKey(), parseEntry(obj));
              }
            }
          }
        }
        return Optional.of(map);
      }
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  private static Entry parseEntry(JsonObject obj) {
    Integer tier = getInteger(obj, "requiredRocketTier", 1, 10);
    Integer suitTier = getInteger(obj, "requiredSuitTier", 1, 5);
    String suitTag = getString(obj, "requiredSuitTag");
    String name = getString(obj, "name");
    String description = getString(obj, "description");
    String fuel = getString(obj, "fuel");

    Set<String> hazards = null;
    if (obj.has("hazards") && obj.get("hazards").isJsonArray()) {
      hazards = new LinkedHashSet<>();
      JsonArray arr = obj.getAsJsonArray("hazards");
      for (JsonElement el : arr) {
        if (el.isJsonPrimitive()) {
          String value = el.getAsString().trim();
          if (!value.isEmpty()) {
            hazards.add(value.toLowerCase());
          }
        }
      }
    }

    return new Entry(tier, suitTier, suitTag, name, description, fuel, hazards);
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
