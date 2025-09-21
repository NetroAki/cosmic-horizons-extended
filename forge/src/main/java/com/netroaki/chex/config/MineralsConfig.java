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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MineralsConfig {
    public static final String FILE_NAME = "chex/chex-minerals.json5";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final class Distribution {
        public String tag;
        public String block;
        public String vein;
        public int count;
        public int minY;
        public int maxY;
        public List<String> biomes = List.of();
    }

    public static final class TierEntry {
        public String gtTier;
        public List<Distribution> distributions = new ArrayList<>();
    }

    public static final class PlanetEntry {
        public int rocketTier;
        public List<TierEntry> mineralTiers = new ArrayList<>();
    }

    private final Map<String, PlanetEntry> byPlanet = new HashMap<>();

    private MineralsConfig() {
    }

    public static MineralsConfig load(Path configDir) {
        MineralsConfig data = new MineralsConfig();
        try {
            Path path = configDir.resolve(FILE_NAME);
            File file = path.toFile();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                String sample = "{\n" +
                        "  \"cosmichorizons:earth_moon\": {\n" +
                        "    \"rocketTier\": 2,\n" +
                        "    \"mineralTiers\": [\n" +
                        "      { \"gtTier\": \"LV\", \"distributions\": [\n" +
                        "        { \"tag\": \"gtceu:ores/titanium\", \"vein\": \"small\", \"count\": 6, \"minY\": -32, \"maxY\": 40 }\n"
                        +
                        "      ] }\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  \"cosmichorizons:mars_lands\": {\n" +
                        "    \"rocketTier\": 4,\n" +
                        "    \"mineralTiers\": [\n" +
                        "      { \"gtTier\": \"HV\", \"distributions\": [\n" +
                        "        { \"tag\": \"gtceu:ores/tungsten\", \"vein\": \"large\", \"count\": 3, \"minY\": -48, \"maxY\": 8 },\n"
                        +
                        "        { \"tag\": \"gtceu:ores/molybdenum\", \"vein\": \"medium\", \"count\": 4, \"minY\": -32, \"maxY\": 24 }\n"
                        +
                        "      ] }\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  \"cosmic_horizons_extended:exoplanet_alpha\": {\n" +
                        "    \"rocketTier\": 6,\n" +
                        "    \"mineralTiers\": [\n" +
                        "      { \"gtTier\": \"EV\", \"distributions\": [\n" +
                        "        { \"tag\": \"gtceu:ores/naquadah\", \"vein\": \"patch\", \"count\": 2, \"minY\": -64, \"maxY\": 32 }\n"
                        +
                        "      ] }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}\n";
                Files.writeString(path, sample);
                CHEX.LOGGER.warn("CHEX wrote sample minerals config to {}.", path);
                return data;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
                jr.setLenient(true);
                JsonObject root = GSON.fromJson(jr, JsonObject.class);
                for (Map.Entry<String, JsonElement> e : root.entrySet()) {
                    if (!e.getValue().isJsonObject())
                        continue;
                    data.byPlanet.put(e.getKey(), parsePlanet(e.getValue().getAsJsonObject()));
                }
            }
        } catch (Exception ex) {
            CHEX.LOGGER.error("Failed to read minerals config: {}", ex.toString());
        }
        return data;
    }

    private static PlanetEntry parsePlanet(JsonObject obj) {
        PlanetEntry pe = new PlanetEntry();
        if (obj.has("rocketTier"))
            pe.rocketTier = Math.max(1, obj.get("rocketTier").getAsInt());
        if (obj.has("mineralTiers")) {
            for (JsonElement te : obj.getAsJsonArray("mineralTiers")) {
                if (!te.isJsonObject())
                    continue;
                pe.mineralTiers.add(parseTier(te.getAsJsonObject()));
            }
        }
        return pe;
    }

    private static TierEntry parseTier(JsonObject obj) {
        TierEntry te = new TierEntry();
        if (obj.has("gtTier"))
            te.gtTier = obj.get("gtTier").getAsString();
        if (obj.has("distributions")) {
            for (JsonElement de : obj.getAsJsonArray("distributions")) {
                if (!de.isJsonObject())
                    continue;
                te.distributions.add(parseDistribution(de.getAsJsonObject()));
            }
        }
        return te;
    }

    private static Distribution parseDistribution(JsonObject obj) {
        Distribution d = new Distribution();
        if (obj.has("tag"))
            d.tag = obj.get("tag").getAsString();
        if (obj.has("vein"))
            d.vein = obj.get("vein").getAsString();
        if (obj.has("count"))
            d.count = obj.get("count").getAsInt();
        if (obj.has("minY"))
            d.minY = obj.get("minY").getAsInt();
        if (obj.has("maxY"))
            d.maxY = obj.get("maxY").getAsInt();
        if (obj.has("biomes")) {
            List<String> biomes = new ArrayList<>();
            for (JsonElement be : obj.getAsJsonArray("biomes")) {
                biomes.add(be.getAsString());
            }
            d.biomes = biomes;
        }
        return d;
    }

    public Map<String, PlanetEntry> planets() {
        return byPlanet;
    }
}

