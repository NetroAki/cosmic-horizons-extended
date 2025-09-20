package com.netroaki.chex.config;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Loader-neutral parser for chex-minerals.json5.
 */
public final class MineralsConfigCore {

    public static final class DistributionEntry {
        public final String idOrTag;
        public final boolean isTag;
        public final String vein;
        public final int count;
        public final int minY;
        public final int maxY;
        public final List<String> biomes;

        public DistributionEntry(String idOrTag, boolean isTag, String vein, int count, int minY, int maxY,
                List<String> biomes) {
            this.idOrTag = idOrTag;
            this.isTag = isTag;
            this.vein = vein;
            this.count = count;
            this.minY = minY;
            this.maxY = maxY;
            this.biomes = biomes == null ? List.of() : List.copyOf(biomes);
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private MineralsConfigCore() {
    }

    public static Optional<Map<String, List<DistributionEntry>>> load(Path file) {
        try {
            if (!Files.exists(file))
                return Optional.empty();
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
                jr.setLenient(true);
                JsonObject root = GSON.fromJson(jr, JsonObject.class);
                if (root == null)
                    return Optional.empty();
                Map<String, List<DistributionEntry>> result = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                    if (!entry.getValue().isJsonObject())
                        continue;
                    JsonObject planet = entry.getValue().getAsJsonObject();
                    if (!planet.has("mineralTiers"))
                        continue;
                    JsonArray tiers = planet.getAsJsonArray("mineralTiers");
                    List<DistributionEntry> list = new ArrayList<>();
                    for (JsonElement te : tiers) {
                        if (!te.isJsonObject())
                            continue;
                        JsonObject tierObj = te.getAsJsonObject();
                        if (!tierObj.has("distributions"))
                            continue;
                        JsonArray dists = tierObj.getAsJsonArray("distributions");
                        for (JsonElement de : dists) {
                            if (!de.isJsonObject())
                                continue;
                            JsonObject dist = de.getAsJsonObject();
                            String id = dist.has("tag") ? dist.get("tag").getAsString()
                                    : dist.has("id") ? dist.get("id").getAsString() : null;
                            if (id == null)
                                continue;
                            boolean isTag = dist.has("tag");
                            String vein = dist.has("vein") ? dist.get("vein").getAsString() : "small";
                            int count = dist.has("count") ? dist.get("count").getAsInt() : 4;
                            int minY = dist.has("minY") ? dist.get("minY").getAsInt() : -32;
                            int maxY = dist.has("maxY") ? dist.get("maxY").getAsInt() : 40;
                            List<String> biomes = new ArrayList<>();
                            if (dist.has("biomes") && dist.get("biomes").isJsonArray()) {
                                for (JsonElement b : dist.getAsJsonArray("biomes")) {
                                    try {
                                        biomes.add(b.getAsString());
                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                            list.add(new DistributionEntry(id, isTag, vein, count, minY, maxY, biomes));
                        }
                    }
                    if (!list.isEmpty())
                        result.put(entry.getKey(), list);
                }
                return Optional.of(result);
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
