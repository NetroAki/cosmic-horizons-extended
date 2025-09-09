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

public final class FallbackOres {
    public static final String FILE_NAME = "chex/fallback_ores.json5";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<String, String> tagToBlock = new HashMap<>();

    private FallbackOres() {
    }

    public static FallbackOres load(Path configDir) {
        FallbackOres data = new FallbackOres();
        try {
            Path path = configDir.resolve(FILE_NAME);
            File file = path.toFile();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                String sample = "{\n" +
                        "  \"gtceu:ores/titanium\": \"minecraft:iron_ore\",\n" +
                        "  \"gtceu:ores/tungsten\": \"minecraft:ancient_debris\",\n" +
                        "  \"gtceu:ores/molybdenum\": \"minecraft:deepslate_coal_ore\"\n" +
                        "}\n";
                Files.writeString(path, sample);
                CHEX.LOGGER.warn("CHEX wrote sample fallback ores to {}.", path);
                return data;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
                jr.setLenient(true);
                JsonObject root = GSON.fromJson(jr, JsonObject.class);
                for (Map.Entry<String, JsonElement> e : root.entrySet()) {
                    if (!e.getValue().isJsonPrimitive())
                        continue;
                    data.tagToBlock.put(e.getKey(), e.getValue().getAsString());
                }
            }
        } catch (Exception ex) {
            CHEX.LOGGER.error("Failed to read fallback ores: {}", ex.toString());
        }
        return data;
    }

    public String getBlockForTag(String tag) {
        return tagToBlock.get(tag);
    }
}
