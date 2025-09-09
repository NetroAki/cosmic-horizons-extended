package com.netroaki.chex.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.netroaki.chex.CHEX;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class PlanetOverrides {
    public static final String FILE_NAME = "chex/chex-planets.json5";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final class Entry {
        public int requiredRocketTier = 1;
        public String requiredSuitTag = "chex:suit1";
        public String name = null;
    }

    private final Map<String, Entry> overridesById = new HashMap<>();

    private PlanetOverrides() {
    }

    public static PlanetOverrides load(Path configDir) {
        PlanetOverrides data = new PlanetOverrides();
        try {
            Path path = configDir.resolve(FILE_NAME);
            File file = path.toFile();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                String sample = "{\n" +
                        "  \"cosmichorizons:earth_moon\": { \"requiredRocketTier\": 2, \"requiredSuitTag\": \"cosmic_horizons_extended:suit1\", \"name\": \"Moon\" },\n"
                        +
                        "  \"cosmichorizons:mars_lands\": { \"requiredRocketTier\": 4, \"requiredSuitTag\": \"cosmic_horizons_extended:suit2\", \"name\": \"Mars\" },\n"
                        +
                        "  \"cosmic_horizons_extended:exoplanet_alpha\": { \"requiredRocketTier\": 6, \"requiredSuitTag\": \"cosmic_horizons_extended:suit3\", \"name\": \"Exoplanet Alpha\" }\n"
                        +
                        "}\n";
                Files.writeString(path, sample);
                CHEX.LOGGER.warn("CHEX wrote sample planet overrides to {}.", path);
                return data;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
                jr.setLenient(true); // accept json5-like (comments, etc.)
                JsonObject root = GSON.fromJson(jr, JsonObject.class);
                for (Map.Entry<String, JsonElement> e : root.entrySet()) {
                    // allow either flat object of planetId -> entry, or nested groups
                    if (e.getValue().isJsonObject()) {
                        JsonObject section = e.getValue().getAsJsonObject();
                        for (Map.Entry<String, JsonElement> pe : section.entrySet()) {
                            if (pe.getValue().isJsonObject()) {
                                data.put(pe.getKey(), pe.getValue().getAsJsonObject());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            CHEX.LOGGER.error("Failed to read planet overrides: {}", ex.toString());
        }
        return data;
    }

    private void put(String planetId, JsonObject obj) {
        Entry entry = new Entry();
        if (obj.has("requiredRocketTier")) {
            entry.requiredRocketTier = Math.max(1, obj.get("requiredRocketTier").getAsInt());
        }
        if (obj.has("requiredSuitTag")) {
            entry.requiredSuitTag = obj.get("requiredSuitTag").getAsString();
        }
        if (obj.has("name")) {
            entry.name = obj.get("name").getAsString();
        }
        overridesById.put(planetId, entry);
    }

    public Map<String, Entry> getAll() {
        return overridesById;
    }
}
