package com.netroaki.chex.config;

import com.google.gson.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class PlanetOverridesCore {
  public record Entry(
      Integer requiredRocketTier,
      Integer requiredSuitTier,
      String requiredSuitTag,
      String name,
      String description,
      String fuel,
      Set<String> hazards) {
    public Entry {
      if (requiredRocketTier != null && requiredRocketTier < 1) {
        requiredRocketTier = 1;
      }
      if (requiredSuitTier != null && requiredSuitTier < 1) {
        requiredSuitTier = 1;
      }
      if (hazards != null) {
        hazards = Collections.unmodifiableSet(new LinkedHashSet<>(hazards));
      }
      name = normalizeString(name);
      description = normalizeString(description);
      fuel = normalizeString(fuel);
      requiredSuitTag = normalizeString(requiredSuitTag);
    }

    private static String normalizeString(String input) {
      if (input == null) return null;
      String trimmed = input.trim();
      return trimmed.isEmpty() ? null : trimmed;
    }
  }

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private PlanetOverridesCore() {}

  public static Optional<Map<String, Entry>> load(Path file) {
    try {
      if (!Files.exists(file)) return Optional.empty();
      try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
        com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
        jr.setLenient(true);
        JsonObject root = GSON.fromJson(jr, JsonObject.class);
        if (root == null) return Optional.empty();

        Map<String, Entry> map = new HashMap<>();
        if (root.has("planets") && root.get("planets").isJsonObject()) {
          collectOverrides(root.getAsJsonObject("planets"), map);
        }
        if (root.has("overrides") && root.get("overrides").isJsonObject()) {
          collectOverrides(root.getAsJsonObject("overrides"), map);
        }
        // Legacy support: allow nested sections under arbitrary keys
        for (Map.Entry<String, JsonElement> section : root.entrySet()) {
          if (section.getValue().isJsonObject()
              && !section.getKey().equals("planets")
              && !section.getKey().equals("overrides")) {
            collectOverrides(section.getValue().getAsJsonObject(), map);
          }
        }
        return map.isEmpty() ? Optional.empty() : Optional.of(map);
      }
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  private static void collectOverrides(JsonObject container, Map<String, Entry> map) {
    for (Map.Entry<String, JsonElement> element : container.entrySet()) {
      if (!element.getValue().isJsonObject()) continue;
      Entry entry = parseEntry(element.getValue().getAsJsonObject());
      map.put(element.getKey(), entry);
    }
  }

  private static Entry parseEntry(JsonObject obj) {
    Integer requiredRocketTier = getInteger(obj, "requiredRocketTier", 1, 10);
    Integer requiredSuitTier = getInteger(obj, "requiredSuitTier", 1, 5);
    String requiredSuitTag = getString(obj, "requiredSuitTag");
    String name = getString(obj, "name");
    String description = getString(obj, "description");
    String fuel = getString(obj, "fuel");

    Set<String> hazards = null;
    if (obj.has("hazards")) {
      hazards = new LinkedHashSet<>();
      JsonElement hazardsElement = obj.get("hazards");
      if (hazardsElement.isJsonArray()) {
        JsonArray array = hazardsElement.getAsJsonArray();
        for (JsonElement entry : array) {
          if (entry.isJsonPrimitive()) {
            String value = entry.getAsString().trim();
            if (!value.isEmpty()) {
              hazards.add(value.toLowerCase(Locale.ROOT));
            }
          }
        }
      }
    }

    return new Entry(
        requiredRocketTier, requiredSuitTier, requiredSuitTag, name, description, fuel, hazards);
  }

  private static Integer getInteger(JsonObject obj, String key, int min, int max) {
    if (!obj.has(key)) return null;
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
    if (!obj.has(key)) return null;
    try {
      String value = obj.get(key).getAsString().trim();
      return value.isEmpty() ? null : value;
    } catch (Exception ignored) {
      return null;
    }
  }
}
