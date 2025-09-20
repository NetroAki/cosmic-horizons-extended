package com.netroaki.chex.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.config.FallbackOres;
import com.netroaki.chex.config.MineralsConfigCore;
import com.netroaki.chex.gt.GregTechBridge;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class CHEXWorldgenProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public CHEXWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.output = output;
        this.lookupProvider = lookupProvider;
    }

    @Override
    public java.util.concurrent.CompletableFuture<?> run(CachedOutput cache) {
        var root = output.getOutputFolder();
        var configDir = Path.of("config");
        var minerals = MineralsConfigCore.load(configDir.resolve("chex/chex-minerals.json5"));
        FallbackOres fallback = FallbackOres.load(configDir);
        boolean hasGT = com.netroaki.chex.CHEX.gt().isAvailable();
        minerals.ifPresent(minMap -> {
            for (Map.Entry<String, java.util.List<MineralsConfigCore.DistributionEntry>> planet : minMap.entrySet()) {
                String planetId = planet.getKey();
                for (MineralsConfigCore.DistributionEntry dist : planet.getValue()) {
                    String oreKey = dist.idOrTag;
                    String featureName = safeName(planetId) + ";" + safeName(oreKey) + ";" + dist.vein;
                    String placedName = featureName + ";placed";

                    ResourceLocation configuredId = ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, featureName);
                    ResourceLocation placedId = ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, placedName);

                    Map<String, Object> configured = createConfiguredFeatureJson(
                            hasGT ? oreKey : fallback.getBlockForTag(oreKey));
                    Map<String, Object> placed = createPlacedFeatureJson(configuredId.toString(), dist.count, dist.minY,
                            dist.maxY);

                    Path configuredPath = root.resolve(
                            "data/" + CHEX.MOD_ID + "/worldgen/configured_feature/" + configuredId.getPath() + ".json");
                    Path placedPath = root.resolve(
                            "data/" + CHEX.MOD_ID + "/worldgen/placed_feature/" + placedId.getPath() + ".json");
                    save(cache, configuredPath, configured);
                    save(cache, placedPath, placed);

                    Map<String, Object> modifier = createBiomeModifierJson(placedId.toString(), dist.biomes);
                    ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID,
                            featureName + ";biome_mod");
                    Path modifierPath = root
                            .resolve("data/" + CHEX.MOD_ID + "/forge/biome_modifier/" + modifierId.getPath() + ".json");
                    save(cache, modifierPath, modifier);
                }
            }
        });
        return java.util.concurrent.CompletableFuture.completedFuture(null);
    }

    private static void save(CachedOutput cache, Path path, Map<String, Object> data) {
        try {
            java.nio.file.Files.createDirectories(path.getParent());
            DataProvider.saveStable(cache, GSON.toJsonTree(data), path);
        } catch (IOException e) {
            CHEX.LOGGER.error("Failed to write {}: {}", path, e.toString());
        }
    }

    private static String safeName(String id) {
        return id.replace(':', '_').replace('/', '_');
    }

    private static Map<String, Object> createConfiguredFeatureJson(String blockOrTag) {
        Map<String, Object> root = new HashMap<>();
        if (blockOrTag == null || blockOrTag.isEmpty()) {
            blockOrTag = "minecraft:stone";
        }
        boolean isTag = blockOrTag.contains(":") && blockOrTag.contains("ores/");
        root.put("type", "minecraft:ore");
        Map<String, Object> config = new HashMap<>();
        Map<String, Object> target = new HashMap<>();
        target.put("predicate_type", "minecraft:always_true");
        config.put("targets", java.util.List.of(target));
        Map<String, Object> state = new HashMap<>();
        state.put("Name", isTag ? "minecraft:stone" : blockOrTag);
        config.put("state", state);
        root.put("config", config);
        return root;
    }

    private static Map<String, Object> createPlacedFeatureJson(String configuredId, int count, int minY, int maxY) {
        Map<String, Object> root = new HashMap<>();
        root.put("feature", configuredId);
        root.put("placement", java.util.List.of(
                Map.of("type", "minecraft:count", "count", count <= 0 ? 1 : count),
                Map.of("type", "minecraft:in_square"),
                Map.of("type", "minecraft:height_range", "height",
                        Map.of("type", "minecraft:uniform", "min_inclusive", Map.of("absolute", minY),
                                "max_inclusive", Map.of("absolute", maxY))),
                Map.of("type", "minecraft:biome")));
        return root;
    }

    private static Map<String, Object> createBiomeModifierJson(String placedId, java.util.List<String> biomes) {
        Map<String, Object> root = new HashMap<>();
        root.put("type", "forge:add_features");
        root.put("biomes", biomes == null || biomes.isEmpty() ? "#minecraft:is_overworld"
                : (biomes.size() == 1 ? biomes.get(0) : biomes));
        root.put("features", java.util.List.of(placedId));
        root.put("step", "underground_ores");
        return root;
    }

    @Override
    public String getName() {
        return "CHEX Worldgen";
    }
}
