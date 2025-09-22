package com.netroaki.chex.world.biome;

import com.mojang.datafixers.util.Pair;
import com.netroaki.chex.CHEX;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.treedecorators.BeehiveDecorator;

import java.util.List;
import java.util.function.BiConsumer;

public class KeplerBiomeFeatures {
    // Biome Feature Keys
    public static final ResourceKey<ConfiguredFeature<?, ?>> KEPLER_FOREST_TREES = registerKey("kepler_forest_trees");
    public static final ResourceKey<PlacedFeature> PLACED_KEPLER_FOREST_TREES = PlacementUtils.createKey("kepler_forest_trees_placed");
    
    // Tree Configuration
    public static TreeConfiguration.TreeConfigurationBuilder keplerTreeConfig() {
        return new TreeConfiguration.TreeConfigurationBuilder(
            Blocks.OAK_LOG.defaultBlockState(),
            Blocks.OAK_LEAVES.defaultBlockState()
        ).decorators(List.of(new BeehiveDecorator(0.05f)));
    }
    
    // Ore Configuration
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_KEPSILON = registerKey("ore_kepsilon");
    public static final ResourceKey<PlacedFeature> PLACED_ORE_KEPSILON = PlacementUtils.createKey("ore_kepsilon_placed");
    
    // Vegetation
    public static final ResourceKey<PlacedFeature> PATCH_KEPSILON_GRASS = PlacementUtils.createKey("patch_kepsilon_grass");
    public static final ResourceKey<PlacedFeature> PATCH_KEPSILON_FLOWERS = PlacementUtils.createKey("patch_kepsilon_flowers");
    
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // Register tree features
        register(context, KEPLER_FOREST_TREES, Feature.TREE, 
            keplerTreeConfig().build()
        );
        
        // Register ores
        register(context, ORE_KEPSILON, Feature.ORE,
            new OreConfiguration(
                List.of(
                    OreConfiguration.target(
                        net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest.forBlock(Blocks.STONE),
                        Blocks.IRON_ORE.defaultBlockState()
                    )
                ),
                8 // Vein size
            )
        );
    }
    
    public static void bootstrapPlacements(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        
        // Tree placement
        register(context, PLACED_KEPLER_FOREST_TREES, configuredFeatures.getOrThrow(KEPLER_FOREST_TREES),
            VegetationPlacements.treePlacement(
                PlacementUtils.countExtra(10, 0.1f, 2) // Count, extra chance, extra count
            )
        );
        
        // Ore placement
        register(context, PLACED_ORE_KEPSILON, configuredFeatures.getOrThrow(ORE_KEPSILON),
            commonOrePlacement(7, // Veins per chunk
                HeightRangePlacement.uniform(
                    VerticalAnchor.bottom(),
                    VerticalAnchor.absolute(64)
                )
            )
        );
        
        // Vegetation patches
        register(context, PATCH_KEPSILON_GRASS, configuredFeatures.getOrThrow(Feature.VEGETATION_PATCH),
            VegetationPlacements.grassPatch(32)
        );
        
        register(context, PATCH_KEPSILON_FLOWERS, configuredFeatures.getOrThrow(Feature.FLOWER),
            RarityFilter.onAverageOnceEvery(32),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        );
    }
    
    // Helper methods
    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(CHEX.MOD_ID, name));
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
        BootstapContext<ConfiguredFeature<?, ?>> context,
        ResourceKey<ConfiguredFeature<?, ?>> key,
        F feature, FC configuration
    ) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
    
    private static void register(
        BootstapContext<PlacedFeature> context,
        ResourceKey<PlacedFeature> key,
        Holder<ConfiguredFeature<?, ?>> configuration,
        PlacementModifier... modifiers
    ) {
        context.register(key, new PlacedFeature(configuration, List.of(modifiers)));
    }
    
    private static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange) {
        return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }
    
    private static List<PlacementModifier> commonOrePlacement(int veinsPerChunk, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(veinsPerChunk), heightRange);
    }
}
