package com.netroaki.chex.config;

import com.google.gson.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class FallbackOresCore {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private FallbackOresCore() {}

  public static Optional<Map<String, String>> load(Path file) {
    try {
      if (!Files.exists(file)) return Optional.empty();
      try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
        com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
        jr.setLenient(true);
        JsonObject root = GSON.fromJson(jr, JsonObject.class);
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> e : root.entrySet()) {
          if (e.getValue().isJsonPrimitive()) map.put(e.getKey(), e.getValue().getAsString());
        }
        return Optional.of(map);
      }
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }
}
