package com.netroaki.chex.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.config.MineralsConfigCore;
import net.minecraft.data.PackOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Minimal scaffolding that writes an empty minerals JSON5 if missing
 */
public class MineralsDataProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final PackOutput output;

    public MineralsDataProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public java.util.concurrent.CompletableFuture<Void> run(net.minecraft.data.CachedOutput cache) {
        Path cfg = net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR.get().resolve("chex/chex-minerals.json5");
        try {
            if (!Files.exists(cfg)) {
                Files.createDirectories(cfg.getParent());
                String sample = "{\n" +
                        "  // Define minerals per planet here (see TASKS.md section 7)\n" +
                        "  \"cosmichorizons:earth_moon\": {\n" +
                        "    \"rocketTier\": 2,\n" +
                        "    \"mineralTiers\": [ { \"gtTier\": \"LV\", \"distributions\": [ { \"tag\": \"gtceu:ores/ilmenite\", \"vein\": \"patch\", \"count\": 6, \"minY\": -16, \"maxY\": 48, \"biomes\": [\"#chex:moon_all\"] } ] } ]\n"
                        +
                        "  },\n" +
                        "  \"cosmic_horizons_extended:pandora\": {\n" +
                        "    \"rocketTier\": 3,\n" +
                        "    \"mineralTiers\": [\n" +
                        "      { \"gtTier\": \"HV\", \"distributions\": [\n" +
                        "        { \"tag\": \"gtceu:ores/naquadah\", \"vein\": \"small\", \"count\": 3, \"minY\": -32, \"maxY\": 24, \"biomes\": [\"#chex:pandora_all\"] }\n"
                        +
                        "      ] }\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  \"cosmic_horizons_extended:arrakis\": {\n" +
                        "    \"rocketTier\": 4,\n" +
                        "    \"mineralTiers\": [\n" +
                        "      { \"gtTier\": \"EV\", \"distributions\": [\n" +
                        "        { \"tag\": \"gtceu:ores/salt\", \"vein\": \"patch\", \"count\": 8, \"minY\": -16, \"maxY\": 64, \"biomes\": [\"#chex:arrakis_all\"] }\n"
                        +
                        "      ] }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}\n";
                Files.writeString(cfg, sample);
                CHEX.LOGGER.info("[CHEX] Wrote sample minerals config to {}", cfg);
            }

            var parsed = MineralsConfigCore.load(cfg);
            parsed.ifPresent(map -> map.forEach((planetId, entries) -> {
                for (var dist : entries) {
                    String keyBase = sanitize(planetId) + "_" + sanitize(dist.idOrTag);
                    int size = mapVeinSize(dist.vein);
                    writeConfiguredFeature(keyBase, size);
                    writePlacedFeature(keyBase, dist.count, dist.minY, dist.maxY);
                    JsonArray biomes = new JsonArray();
                    if (dist.biomes == null || dist.biomes.isEmpty()) {
                        biomes.add("#minecraft:is_overworld");
                    } else {
                        for (String b : dist.biomes)
                            biomes.add(b);
                    }
                    writeBiomeModifier(keyBase, biomes);
                }
            }));
        } catch (IOException e) {
            CHEX.LOGGER.warn("[CHEX] Minerals datagen failed: {}", e.toString());
        }
        return java.util.concurrent.CompletableFuture.completedFuture(null);
    }

    @Override
    public String getName() {
        return "CHEX Minerals DataGen";
    }

    private static String sanitize(String in) {
        return in.toLowerCase().replace(':', '_').replace('/', '_').replaceAll("[^a-z0-9_]+", "_");
    }

    private void writeConfiguredFeature(String id, int size) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:ore");
        JsonObject cfg = new JsonObject();
        cfg.addProperty("discard_chance_on_air_exposure", 0.0);
        cfg.addProperty("size", size);
        JsonArray targets = new JsonArray();
        JsonObject targetEntry = new JsonObject();
        JsonObject target = new JsonObject();
        target.addProperty("predicate_type", "minecraft:tag_match");
        target.addProperty("tag", "minecraft:stone_ore_replaceables");
        targetEntry.add("target", target);
        JsonObject state = new JsonObject();
        state.addProperty("Name", "minecraft:stone");
        targetEntry.add("state", state);
        targets.add(targetEntry);
        cfg.add("targets", targets);
        root.add("config", cfg);

        Path out = this.output.getOutputFolder()
                .resolve("data/" + CHEX.MOD_ID + "/worldgen/configured_feature/" + id + ".json");
        writeJson(out, root);
    }

    private void writePlacedFeature(String id, int count, int minY, int maxY) {
        JsonObject root = new JsonObject();
        root.addProperty("feature", CHEX.MOD_ID + ":" + id);
        JsonArray placement = new JsonArray();
        JsonObject countObj = new JsonObject();
        countObj.addProperty("type", "minecraft:count");
        countObj.addProperty("count", count);
        placement.add(countObj);
        JsonObject heightObj = new JsonObject();
        heightObj.addProperty("type", "minecraft:height_range");
        JsonObject height = new JsonObject();
        height.addProperty("type", "minecraft:uniform");
        JsonObject min = new JsonObject();
        min.addProperty("absolute", minY);
        JsonObject max = new JsonObject();
        max.addProperty("absolute", maxY);
        height.add("min_inclusive", min);
        height.add("max_inclusive", max);
        heightObj.add("height", height);
        placement.add(heightObj);
        root.add("placement", placement);

        Path out = this.output.getOutputFolder()
                .resolve("data/" + CHEX.MOD_ID + "/worldgen/placed_feature/" + id + ".json");
        writeJson(out, root);
    }

    private void writeBiomeModifier(String id, JsonArray biomes) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "forge:add_features");
        root.add("biomes", biomes);
        JsonArray features = new JsonArray();
        features.add(CHEX.MOD_ID + ":" + id);
        root.add("features", features);
        root.addProperty("step", "underground_ores");

        Path out = this.output.getOutputFolder()
                .resolve("data/" + CHEX.MOD_ID + "/forge/biome_modifier/" + id + ".json");
        writeJson(out, root);
    }

    private void writeJson(Path out, JsonObject obj) {
        try {
            Files.createDirectories(out.getParent());
            Files.writeString(out, GSON.toJson(obj));
        } catch (IOException e) {
            CHEX.LOGGER.warn("[CHEX] Failed to write datagen file {}: {}", out, e.toString());
        }
    }

    private int mapVeinSize(String vein) {
        return switch (vein.toLowerCase()) {
            case "patch" -> 4;
            case "small" -> 6;
            case "medium" -> 9;
            case "large" -> 17;
            default -> 6;
        };
    }
}
