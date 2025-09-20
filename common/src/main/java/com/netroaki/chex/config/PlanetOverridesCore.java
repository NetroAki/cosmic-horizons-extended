package com.netroaki.chex.config;

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
        public final int requiredRocketTier;
        public final String requiredSuitTag;
        public final String name;

        public Entry(int requiredRocketTier, String requiredSuitTag, String name) {
            this.requiredRocketTier = requiredRocketTier;
            this.requiredSuitTag = requiredSuitTag;
            this.name = name;
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private PlanetOverridesCore() {
    }

    public static Optional<Map<String, Entry>> load(Path file) {
        try {
            if (!Files.exists(file))
                return Optional.empty();
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
                                int tier = obj.has("requiredRocketTier")
                                        ? Math.max(1, obj.get("requiredRocketTier").getAsInt())
                                        : 1;
                                String suit = obj.has("requiredSuitTag") ? obj.get("requiredSuitTag").getAsString()
                                        : "chex:suit1";
                                String name = obj.has("name") ? obj.get("name").getAsString() : null;
                                map.put(pe.getKey(), new Entry(tier, suit, name));
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
}
