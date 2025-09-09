package com.netroaki.chex.worldgen;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.PlanetDef;
import com.netroaki.chex.registry.PlanetRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Map;

/**
 * World generation provider for CHEX planets
 */
public class CHEXWorldGenProvider {

    /**
     * Bootstrap configured features for CHEX planets
     */
    public static void bootstrapConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> context) {
        CHEX.LOGGER.info("Bootstrapping CHEX configured features...");

        // Get all CHEX planets and create configured features for their unique minerals
        Map<ResourceLocation, PlanetDef> chexPlanets = PlanetRegistry.getCHEXPlanets();

        for (Map.Entry<ResourceLocation, PlanetDef> entry : chexPlanets.entrySet()) {
            PlanetDef planet = entry.getValue();
            ResourceLocation planetId = entry.getKey();

            // Create mineral generation features for each planet based on its available
            // minerals
            for (String mineral : planet.availableMinerals()) {
                createMineralFeature(context, planetId, mineral, planet);
            }
        }

        CHEX.LOGGER.info("CHEX configured features bootstrapped successfully");
    }

    /**
     * Bootstrap placed features for CHEX planets
     */
    public static void bootstrapPlacedFeatures(BootstapContext<PlacedFeature> context) {
        CHEX.LOGGER.info("Bootstrapping CHEX placed features...");

        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Get all CHEX planets and create placed features
        Map<ResourceLocation, PlanetDef> chexPlanets = PlanetRegistry.getCHEXPlanets();

        for (Map.Entry<ResourceLocation, PlanetDef> entry : chexPlanets.entrySet()) {
            PlanetDef planet = entry.getValue();
            ResourceLocation planetId = entry.getKey();

            // Create placed features for each mineral on the planet
            for (String mineral : planet.availableMinerals()) {
                createPlacedMineralFeature(context, configuredFeatures, planetId, mineral, planet);
            }
        }

        CHEX.LOGGER.info("CHEX placed features bootstrapped successfully");
    }

    /**
     * Bootstrap biomes for CHEX planets
     */
    public static void bootstrapBiomes(BootstapContext<Biome> context) {
        CHEX.LOGGER.info("Bootstrapping CHEX biomes...");

        // Get all CHEX planets and create biomes for them
        Map<ResourceLocation, PlanetDef> chexPlanets = PlanetRegistry.getCHEXPlanets();

        for (Map.Entry<ResourceLocation, PlanetDef> entry : chexPlanets.entrySet()) {
            PlanetDef planet = entry.getValue();
            ResourceLocation planetId = entry.getKey();

            // Create biome based on planet's biome type
            createPlanetBiome(context, planetId, planet);
        }

        CHEX.LOGGER.info("CHEX biomes bootstrapped successfully");
    }

    /**
     * Bootstrap noise generator settings for CHEX planets
     */
    public static void bootstrapNoiseSettings(BootstapContext<NoiseGeneratorSettings> context) {
        CHEX.LOGGER.info("Bootstrapping CHEX noise settings...");

        // Get all CHEX planets and create noise settings
        Map<ResourceLocation, PlanetDef> chexPlanets = PlanetRegistry.getCHEXPlanets();

        for (Map.Entry<ResourceLocation, PlanetDef> entry : chexPlanets.entrySet()) {
            PlanetDef planet = entry.getValue();
            ResourceLocation planetId = entry.getKey();

            // Create noise settings based on planet properties
            createPlanetNoiseSettings(context, planetId, planet);
        }

        CHEX.LOGGER.info("CHEX noise settings bootstrapped successfully");
    }

    private static void createMineralFeature(BootstapContext<ConfiguredFeature<?, ?>> context,
            ResourceLocation planetId, String mineral, PlanetDef planet) {
        CHEX.LOGGER.debug("Creating mineral feature for {} on planet {}", mineral, planetId);

        // Get mineral data from GTCEu bridge
        var mineralData = com.netroaki.chex.gt.GregTechBridge.getMineralDataForPlanet(planetId.toString());

        if (mineralData.containsKey(mineral)) {
            var mineralInfo = (java.util.Map<String, Object>) mineralData.get(mineral);
            int tier = (Integer) mineralInfo.get("tier");
            double abundance = (Double) mineralInfo.get("abundance");
            int[] depth = (int[]) mineralInfo.get("depth");

            // Create ore generation feature based on mineral properties
            createOreFeature(context, planetId, mineral, tier, abundance, depth, planet);
        }
    }

    private static void createOreFeature(BootstapContext<ConfiguredFeature<?, ?>> context,
            ResourceLocation planetId, String mineral, int tier, double abundance,
            int[] depth, PlanetDef planet) {

        // For now, just log the mineral feature creation
        // In a full implementation, this would create actual ore generation features
        CHEX.LOGGER.debug("Would create ore feature for {} on {}: tier={}, abundance={}, depth={}-{}",
                mineral, planetId, tier, abundance, depth[0], depth[1]);

        // Calculate vein size based on tier and planet gravity
        int veinSize = calculateVeinSize(tier, planet.gravityLevel());
        CHEX.LOGGER.debug("Calculated vein size: {} for mineral {} on planet {}", veinSize, mineral, planetId);
    }

    private static net.minecraft.world.level.block.Block getOreBlockForMineral(String mineral) {
        return switch (mineral) {
            case "iron" -> net.minecraft.world.level.block.Blocks.IRON_ORE;
            case "copper" -> net.minecraft.world.level.block.Blocks.COPPER_ORE;
            case "gold" -> net.minecraft.world.level.block.Blocks.GOLD_ORE;
            case "tin" -> net.minecraft.world.level.block.Blocks.IRON_ORE; // Fallback
            case "silver" -> net.minecraft.world.level.block.Blocks.IRON_ORE; // Fallback
            case "lead" -> net.minecraft.world.level.block.Blocks.IRON_ORE; // Fallback
            case "nickel" -> net.minecraft.world.level.block.Blocks.IRON_ORE; // Fallback
            case "cobalt" -> net.minecraft.world.level.block.Blocks.IRON_ORE; // Fallback
            case "platinum" -> net.minecraft.world.level.block.Blocks.GOLD_ORE; // Fallback
            case "titanium" -> net.minecraft.world.level.block.Blocks.IRON_ORE; // Fallback
            case "tungsten" -> net.minecraft.world.level.block.Blocks.IRON_ORE; // Fallback
            case "uranium" -> net.minecraft.world.level.block.Blocks.IRON_ORE; // Fallback
            case "thorium" -> net.minecraft.world.level.block.Blocks.IRON_ORE; // Fallback
            default -> net.minecraft.world.level.block.Blocks.IRON_ORE;
        };
    }

    private static int calculateVeinSize(int tier, float gravityLevel) {
        // Base vein size from tier
        int baseSize = switch (tier) {
            case 1, 2 -> 8;
            case 3, 4 -> 12;
            case 5, 6 -> 16;
            case 7, 8 -> 20;
            case 9, 10 -> 24;
            default -> 8;
        };

        // Adjust for gravity (higher gravity = more compressed ores)
        float gravityMultiplier = 1.0f + (gravityLevel - 1.0f) * 0.2f;
        return Math.round(baseSize * gravityMultiplier);
    }

    private static void createPlacedMineralFeature(BootstapContext<PlacedFeature> context,
            HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures,
            ResourceLocation planetId, String mineral, PlanetDef planet) {
        // Placeholder implementation - would create placed features for minerals
        CHEX.LOGGER.debug("Creating placed mineral feature for {} on planet {}", mineral, planetId);

        // For now, we'll create a simple placed feature reference
        // In a full implementation, this would place the configured features in the
        // world
        // with appropriate placement rules based on planet properties
    }

    private static void createPlanetBiome(BootstapContext<Biome> context, ResourceLocation planetId, PlanetDef planet) {
        // Placeholder implementation - would create custom biomes
        CHEX.LOGGER.debug("Creating biome for planet {} with type {}", planetId, planet.biomeType());

        // For now, we'll create a simple biome reference
        // In a full implementation, this would create custom biomes with:
        // - Temperature and humidity based on planet properties
        // - Custom mob spawns
        // - Custom vegetation and terrain features
        // - Atmosphere effects
    }

    private static void createPlanetNoiseSettings(BootstapContext<NoiseGeneratorSettings> context,
            ResourceLocation planetId, PlanetDef planet) {
        // Placeholder implementation - would create custom terrain generation
        CHEX.LOGGER.debug("Creating noise settings for planet {} with gravity {}", planetId, planet.gravityLevel());

        // For now, we'll create a simple noise settings reference
        // In a full implementation, this would create terrain generation settings based
        // on:
        // - Gravity level affecting terrain height
        // - Atmosphere affecting surface features
        // - Biome type affecting terrain characteristics
        // - Available minerals affecting underground generation
    }

    /**
     * Get resource key for a planet's configured feature
     */
    public static ResourceKey<ConfiguredFeature<?, ?>> getPlanetMineralFeatureKey(ResourceLocation planetId,
            String mineral) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                CHEX.id(planetId.getPath() + "_" + mineral + "_ore"));
    }

    /**
     * Get resource key for a planet's placed feature
     */
    public static ResourceKey<PlacedFeature> getPlanetMineralPlacedFeatureKey(ResourceLocation planetId,
            String mineral) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                CHEX.id(planetId.getPath() + "_" + mineral + "_ore_placed"));
    }

    /**
     * Get resource key for a planet's biome
     */
    public static ResourceKey<Biome> getPlanetBiomeKey(ResourceLocation planetId) {
        return ResourceKey.create(Registries.BIOME,
                CHEX.id(planetId.getPath() + "_biome"));
    }

    /**
     * Get resource key for a planet's noise settings
     */
    public static ResourceKey<NoiseGeneratorSettings> getPlanetNoiseSettingsKey(ResourceLocation planetId) {
        return ResourceKey.create(Registries.NOISE_SETTINGS,
                CHEX.id(planetId.getPath() + "_noise"));
    }
}
