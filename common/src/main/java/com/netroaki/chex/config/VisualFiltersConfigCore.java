package com.netroaki.chex.config;

import com.google.gson.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** Parser for chex-visual-filters.json5 */
public final class VisualFiltersConfigCore {
  public static final class Entry {
    public final String dimension;
    public final double fogR, fogG, fogB; // 0..1
    public final double skyTint; // multiplier

    public Entry(String dimension, double fogR, double fogG, double fogB, double skyTint) {
      this.dimension = dimension;
      this.fogR = fogR;
      this.fogG = fogG;
      this.fogB = fogB;
      this.skyTint = skyTint;
    }
  }

  public static final class Config {
    public final java.util.List<Entry> entries;

    public Config(java.util.List<Entry> entries) {
      this.entries = entries;
    }
  }

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private VisualFiltersConfigCore() {}

  public static Optional<Config> load(Path file) {
    try {
      if (!Files.exists(file)) return Optional.empty();
      try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
        com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
        jr.setLenient(true);
        JsonObject root = GSON.fromJson(jr, JsonObject.class);
        if (root == null) return Optional.empty();
        java.util.List<Entry> entries = new ArrayList<>();
        if (root.has("entries") && root.get("entries").isJsonArray()) {
          for (JsonElement e : root.getAsJsonArray("entries")) {
            if (!e.isJsonObject()) continue;
            JsonObject o = e.getAsJsonObject();
            String dim = o.has("dimension") ? o.get("dimension").getAsString() : "";
            JsonArray fog =
                o.has("fog") && o.get("fog").isJsonArray() ? o.getAsJsonArray("fog") : null;
            double r = 0.6, g = 0.6, b = 0.6;
            if (fog != null && fog.size() >= 3) {
              r = fog.get(0).getAsDouble();
              g = fog.get(1).getAsDouble();
              b = fog.get(2).getAsDouble();
            }
            double skyTint = o.has("skyTint") ? o.get("skyTint").getAsDouble() : 1.0;
            entries.add(new Entry(dim, clamp01(r), clamp01(g), clamp01(b), skyTint));
          }
        }
        return Optional.of(new Config(entries));
      }
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private static double clamp01(double v) {
    return Math.max(0.0, Math.min(1.0, v));
  }
}
