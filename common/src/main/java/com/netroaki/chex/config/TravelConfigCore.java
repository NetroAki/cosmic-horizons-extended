package com.netroaki.chex.config;

import com.google.gson.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** Loader-neutral parser for chex-travel.json5. */
public final class TravelConfigCore {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private TravelConfigCore() {}

  public static Optional<Map<Integer, Set<String>>> load(Path file) {
    try {
      if (!Files.exists(file)) {
        return Optional.empty();
      }
      try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
        com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
        jr.setLenient(true);
        JsonObject root = GSON.fromJson(jr, JsonObject.class);
        Map<Integer, Set<String>> mapping = new HashMap<>();
        if (root.has("tiers") && root.get("tiers").isJsonArray()) {
          JsonArray tiers = root.getAsJsonArray("tiers");
          for (JsonElement te : tiers) {
            if (!te.isJsonObject()) continue;
            JsonObject to = te.getAsJsonObject();
            int tier = to.has("tier") ? Math.max(1, Math.min(10, to.get("tier").getAsInt())) : 1;
            Set<String> planets = new HashSet<>();
            if (to.has("planets") && to.get("planets").isJsonArray()) {
              JsonArray arr = to.getAsJsonArray("planets");
              for (JsonElement pe : arr) {
                try {
                  planets.add(pe.getAsString());
                } catch (Exception ignored) {
                }
              }
            }
            if (!planets.isEmpty()) mapping.put(tier, planets);
          }
        }
        return Optional.of(mapping);
      }
    } catch (Exception ex) {
      return Optional.empty();
    }
  }
}
