package com.netroaki.chex.worldgen;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.config.FallbackOres;
import com.netroaki.chex.config.MineralsConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Loads mineral distributions from configuration and exposes placed feature instances per biome.
 */
public final class MineralGenerationRegistry {

    private static final Map<ResourceLocation, List<Holder<PlacedFeature>>> FEATURES_BY_BIOME = new HashMap<>();
    private static final Map<ResourceLocation, List<Holder<PlacedFeature>>> FEATURES_BY_TAG = new HashMap<>();
    private static boolean initialized;
    private static int distributionCount;

    private MineralGenerationRegistry() {
    }

    /**
     * Reload mineral definitions from config and rebuild the placed feature cache.
     */
    public static void reload() {
        FEATURES_BY_BIOME.clear();
        FEATURES_BY_TAG.clear();
        distributionCount = 0;
        Path configDir = FMLPaths.CONFIGDIR.get();
        MineralsConfig mineralsConfig = MineralsConfig.load(configDir);
        FallbackOres fallback = FallbackOres.load(configDir);
        boolean hasGT = CHEX.gt().isAvailable();

        if (mineralsConfig.planets().isEmpty()) {
            CHEX.LOGGER.info("[CHEX] Minerals config contained no planet entries - skipping mineral feature registration.");
            initialized = false;
            return;
        }

        ITagManager<Block> blockTags = ForgeRegistries.BLOCKS.tags();

        mineralsConfig.planets().forEach((planetId, planetEntry) -> {
            planetEntry.mineralTiers.forEach(tierEntry -> tierEntry.distributions.forEach(dist -> {
                Holder<PlacedFeature> placed = buildPlacedFeature(dist, hasGT, fallback, blockTags, planetId);
                if (placed == null) {
                    return;
                }

                String descriptor = dist.tag != null && !dist.tag.isBlank() ? dist.tag : dist.block;
                if (dist.biomes == null || dist.biomes.isEmpty()) {
                    CHEX.LOGGER.warn("[CHEX] Mineral distribution {} for planet {} has no biomes specified; skipping.",
                            descriptor, planetId);
                    return;
                }

                for (String biomeId : dist.biomes) {
                    if (biomeId == null || biomeId.isBlank()) {
                        continue;
                    }
                    boolean isTagRef = biomeId.startsWith("#");
                    String idPart = isTagRef ? biomeId.substring(1) : biomeId;
                    ResourceLocation loc = ResourceLocation.tryParse(idPart);
                    if (loc == null) {
                        CHEX.LOGGER.warn("[CHEX] Invalid biome identifier '{}' in minerals config; skipping.", biomeId);
                        continue;
                    }
                    if (isTagRef) {
                        FEATURES_BY_TAG.computeIfAbsent(loc, rl -> new ArrayList<>()).add(placed);
                    } else {
                        FEATURES_BY_BIOME.computeIfAbsent(loc, rl -> new ArrayList<>()).add(placed);
                    }
                    distributionCount++;
                }
            }));
        });

        initialized = !FEATURES_BY_BIOME.isEmpty();
        CHEX.LOGGER.info("[CHEX] Mineral generation ready: {} placed feature entries across {} biomes and {} tags (GTCEu={}).",
                distributionCount, FEATURES_BY_BIOME.size(), FEATURES_BY_TAG.size(), hasGT);
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static List<Holder<PlacedFeature>> gatherFeatures(ResourceLocation biomeId, java.util.Collection<ResourceLocation> tags) {
        if (!initialized) {
            return List.of();
        }
        java.util.ArrayList<Holder<PlacedFeature>> combined = new java.util.ArrayList<>();
        java.util.HashSet<Holder<PlacedFeature>> seen = new java.util.HashSet<>();
        List<Holder<PlacedFeature>> direct = FEATURES_BY_BIOME.get(biomeId);
        if (direct != null) {
            for (Holder<PlacedFeature> feature : direct) {
                if (seen.add(feature)) {
                    combined.add(feature);
                }
            }
        }
        if (tags != null && !tags.isEmpty()) {
            for (ResourceLocation tagLoc : tags) {
                List<Holder<PlacedFeature>> tagged = FEATURES_BY_TAG.get(tagLoc);
                if (tagged == null) {
                    continue;
                }
                for (Holder<PlacedFeature> feature : tagged) {
                    if (seen.add(feature)) {
                        combined.add(feature);
                    }
                }
            }
        }
        return combined;
    }

    private static Holder<PlacedFeature> buildPlacedFeature(MineralsConfig.Distribution dist,
            boolean hasGT, FallbackOres fallback, ITagManager<Block> blockTags, String planetId) {
        boolean hasTag = dist.tag != null && !dist.tag.isBlank();
        boolean hasBlock = dist.block != null && !dist.block.isBlank();
        String descriptor = hasTag ? dist.tag : dist.block;
        if (!hasTag && !hasBlock) {
            CHEX.LOGGER.warn("[CHEX] Mineral distribution on {} missing tag/block entry; skipping.", planetId);
            return null;
        }

        BlockState oreState = resolveOreState(dist, hasGT, fallback, blockTags);
        if (oreState == null) {
            CHEX.LOGGER.warn("[CHEX] Unable to resolve ore block for '{}' (planet {}).", descriptor, planetId);
            return null;
        }

        int veinSize = resolveVeinSize(dist.vein);
        int count = dist.count <= 0 ? 1 : dist.count;
        int minY = Math.min(dist.minY, dist.maxY);
        int maxY = Math.max(dist.minY, dist.maxY);
        minY = Math.max(-64, minY);
        maxY = Math.min(320, maxY);
        if (minY >= maxY) {
            minY = Math.max(-64, maxY - 4);
        }

        List<OreConfiguration.TargetBlockState> targets = List.of(
                OreConfiguration.target(stoneRule(), oreState),
                OreConfiguration.target(deepslateRule(), oreState));
        OreConfiguration oreConfig = new OreConfiguration(targets, veinSize);
        ConfiguredFeature<OreConfiguration, ?> configured = new ConfiguredFeature<>(Feature.ORE, oreConfig);
        Holder<ConfiguredFeature<?, ?>> configuredHolder = Holder.direct(configured);

        List<PlacementModifier> modifiers = List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY)),
                BiomeFilter.biome());

        PlacedFeature placed = new PlacedFeature(configuredHolder, modifiers);
        return Holder.direct(placed);
    }

    private static RuleTest stoneRule() {
        return new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    }

    private static RuleTest deepslateRule() {
        return new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    }

    private static BlockState resolveOreState(MineralsConfig.Distribution dist, boolean hasGT, FallbackOres fallback,
            ITagManager<Block> blockTags) {
        if (dist.block != null && !dist.block.isBlank()) {
            ResourceLocation blockLoc = ResourceLocation.tryParse(dist.block);
            if (blockLoc == null) {
                CHEX.LOGGER.warn("[CHEX] Invalid ore block id '{}'.", dist.block);
            } else {
                Block explicit = ForgeRegistries.BLOCKS.getValue(blockLoc);
                if (explicit != null && explicit != Blocks.AIR) {
                    return explicit.defaultBlockState();
                }
                CHEX.LOGGER.warn("[CHEX] Ore block '{}' not found, falling back to tag handling.", dist.block);
            }
        }

        if (dist.tag == null || dist.tag.isBlank()) {
            return null;
        }

        ResourceLocation tagLoc = ResourceLocation.tryParse(dist.tag);
        if (tagLoc == null) {
            CHEX.LOGGER.warn("[CHEX] Invalid ore tag '{}'.", dist.tag);
            return null;
        }

        if (hasGT) {
            TagKey<Block> oreTag = TagKey.create(Registries.BLOCK, tagLoc);
            var holderSet = blockTags.getTag(oreTag);
            if (!holderSet.isEmpty()) {
                return holderSet.iterator().next().value().defaultBlockState();
            }
        }

        String fallbackId = fallback.getBlockForTag(dist.tag);
        if (fallbackId != null && !fallbackId.isBlank()) {
            ResourceLocation fallbackLoc = ResourceLocation.tryParse(fallbackId);
            if (fallbackLoc != null) {
                Block fallbackBlock = ForgeRegistries.BLOCKS.getValue(fallbackLoc);
                if (fallbackBlock != null && fallbackBlock != Blocks.AIR) {
                    return fallbackBlock.defaultBlockState();
                }
            }
        }

        return null;
    }
    private static int resolveVeinSize(String key) {
        if (key == null || key.isBlank()) {
            return 8;
        }
        return switch (key.toLowerCase(Locale.ROOT)) {
            case "tiny" -> 4;
            case "small" -> 6;
            case "medium" -> 10;
            case "large" -> 16;
            case "huge" -> 24;
            case "patch" -> 12;
            default -> 8;
        };
    }
}








