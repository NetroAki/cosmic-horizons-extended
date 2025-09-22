package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.CHEXBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public final class PandoraFeatures {
    private static final String MOD_ID = CHEX.MOD_ID;
    
    // Rule tests for feature placement
    public static final RuleTest PANDORA_STONE = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    public static final RuleTest PANDORA_DIRT = new TagMatchTest(BlockTags.DIRT);
    public static final RuleTest PANDORA_GRASS = new BlockMatchTest(Blocks.GRASS_BLOCK);
    
    // Configured Features
    public static final ResourceKey<ConfiguredFeature<?, ?>> FUNGAL_TOWER = createKey("fungal_tower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SKYBARK_TREE = createKey("skybark_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PANDORA_KELP = createKey("pandora_kelp");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAGMA_SPIRE = createKey("magma_spire");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CLOUDSTONE_ISLAND = createKey("cloudstone_island");
    
    // Placed Features
    public static final ResourceKey<PlacedFeature> PANDORA_FUNGI = createPlacedKey("pandora_fungi");
    public static final ResourceKey<PlacedFeature> PANDORA_TREES = createPlacedKey("pandora_trees");
    public static final ResourceKey<PlacedFeature> PANDORA_UNDERWATER_KELP = createPlacedKey("pandora_underwater_kelp");
    public static final ResourceKey<PlacedFeature> PANDORA_MAGMA_FIELDS = createPlacedKey("pandora_magma_fields");
    public static final ResourceKey<PlacedFeature> PANDORA_FLOATING_ISLANDS = createPlacedKey("pandora_floating_islands");
    
    private PandoraFeatures() {} // Prevent instantiation
    
    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("chex", name));
    }
    
    private static ResourceKey<PlacedFeature> createPlacedKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("chex", name));
    }
    
    public static void bootstrapConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // Register configured features
        register(context, FUNGAL_TOWER, CHEXFeatures.FUNGAL_TOWER.get(), NoneFeatureConfiguration.INSTANCE);
        
        // Skybark Tree Configuration
        register(context, SKYBARK_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(CHEXBlocks.SKYBARK_LOG.get()),
            new StraightTrunkPlacer(5, 2, 0),
            BlockStateProvider.simple(CHEXBlocks.SKYBARK_LEAVES.get()),
            new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
        ).build());
        
        // Pandora Kelp Configuration
        register(context, PANDORA_KELP, Feature.KELP, NoneFeatureConfiguration.INSTANCE);
        
        // Magma Spire Configuration
        register(context, MAGMA_SPIRE, CHEXFeatures.MAGMA_SPIRE.get(), NoneFeatureConfiguration.INSTANCE);
        
        // Cloudstone Island Configuration
        register(context, CLOUDSTONE_ISLAND, CHEXFeatures.CLOUDSTONE_ISLAND.get(), NoneFeatureConfiguration.INSTANCE);
                new StraightTrunkPlacer(5, 2, 3),
                BlockStateProvider.simple(Blocks.OAK_LEAVES),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        ).build());
        
        register(context, PANDORA_KELP, Feature.KELP, NoneFeatureConfiguration.INSTANCE);
        register(context, MAGMA_SPIRE, Feature.BASALT_PILLAR, NoneFeatureConfiguration.INSTANCE);
        register(context, CLOUDSTONE_ISLAND, Feature.DISK, NoneFeatureConfiguration.INSTANCE);
    }
    
    public static void bootstrapPlacedFeatures(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        
        // Fungal Tower Placement
        registerPlacedFeature(context, PANDORA_FUNGI, configuredFeatures.getOrThrow(FUNGAL_TOWER),
            RarityFilter.onAverageOnceEvery(24),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()
        );
        
        // Skybark Tree Placement
        registerPlacedFeature(context, PANDORA_TREES, configuredFeatures.getOrThrow(SKYBARK_TREE),
            RarityFilter.onAverageOnceEvery(3),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            SurfaceWaterDepthFilter.forMaxDepth(0),
            BiomeFilter.biome()
        );
        
        // Underwater Kelp Placement
        registerPlacedFeature(context, PANDORA_UNDERWATER_KELP, configuredFeatures.getOrThrow(PANDORA_KELP),
            RarityFilter.onAverageOnceEvery(15),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
            BiomeFilter.biome(),
            CountOnEveryLayerPlacement.of(2),
            BiomeFilter.biome()
        );
        
        // Magma Fields Placement
        registerPlacedFeature(context, PANDORA_MAGMA_FIELDS, configuredFeatures.getOrThrow(MAGMA_SPIRE),
            RarityFilter.onAverageOnceEvery(40),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome(),
            CountPlacement.of(UniformInt.of(1, 3)),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64))
        );
        
        // Floating Islands Placement
        registerPlacedFeature(context, PANDORA_FLOATING_ISLANDS, configuredFeatures.getOrThrow(CLOUDSTONE_ISLAND),
            RarityFilter.onAverageOnceEvery(50),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.absolute(100), VerticalAnchor.absolute(220)),
            CountPlacement.of(1),
            InSquarePlacement.spread(),
            BiomeFilter.biome(),
            NoiseBasedCountPlacement.of(80, 0.3, 0.0)
        );
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
        BootstapContext<ConfiguredFeature<?, ?>> context,
        ResourceKey<ConfiguredFeature<?, ?>> key,
        F feature,
        FC configuration
    ) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
    
    @SafeVarargs
    private static void registerPlacedFeature(
        BootstapContext<PlacedFeature> context,
        ResourceKey<PlacedFeature> key,
        Holder<ConfiguredFeature<?, ?>> configuration,
        PlacementModifier... modifiers
    ) {
        context.register(key, new PlacedFeature(configuration, List.of(modifiers)));
    }
}
