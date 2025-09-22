package com.netroaki.chex.worldgen.features;

import com.netroaki.chex.registry.ArrakisBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ArrakisFeatures {
    // Rule Tests
    public static final RuleTest ARRAKIS_STONE = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
    public static final RuleTest ARRAKIS_SAND = new BlockMatchTest(Blocks.SAND);
    
    // Configured Features
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPICE_GEODE = registerKey("spice_geode");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SANDSTONE_PILLAR = registerKey("sandstone_pillar");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPICE_CRYSTAL_CLUSTER = registerKey("spice_crystal_cluster");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DUNE_RIBBLE = registerKey("dune_ribble");
    
    // Placed Features
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SPICE = registerKey("ore_spice");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CRYSTALLINE_SALT = registerKey("ore_crystalline_salt");
    
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // Spice Geode
        register(context, SPICE_GEODE, Feature.GEODE,
            new GeodeConfiguration(
                new GeodeBlockSettings(
                    BlockStateProvider.simple(Blocks.AIR),
                    BlockStateProvider.simple(ArrakisBlocks.SPICE_NODE.get()),
                    BlockStateProvider.simple(ArrakisBlocks.ARAKITE_SANDSTONE.get()),
                    BlockStateProvider.simple(Blocks.CALCITE),
                    BlockStateProvider.simple(Blocks.SMOOTH_BASALT),
                    List.of(
                        ArrakisBlocks.SPICE_CRYSTAL.get().defaultBlockState()
                    ),
                    BlockTags.FEATURES_CANNOT_REPLACE,
                    BlockTags.GEODE_INVALID_BLOCKS
                ),
                new GeodeLayerSettings(1.7, 2.2, 3.2, 4.2),
                new GeodeCrackSettings(0.95, 2.0, 2),
                0.35, 0.083,
                true,
                UniformInt.of(4, 6),
                UniformInt.of(2, 4),
                UniformInt.of(1, 2),
                -16, 16,
                0.05,
                1
            )
        );
        
        // Sandstone Pillar
        register(context, SANDSTONE_PILLAR, Feature.BASALT_PILLAR,
            new ColumnPlacerConfiguration(
                UniformInt.of(4, 8),
                UniformInt.of(0, 2),
                UniformInt.of(-1, 1)
            )
        );
        
        // Spice Crystal Cluster
        register(context, SPICE_CRYSTAL_CLUSTER, Feature.FOREST_ROCK,
            new BlockPileConfiguration(BlockStateProvider.simple(ArrakisBlocks.SPICE_CRYSTAL.get()))
        );
        
        // Dune Ribble
        register(context, DUNE_RIBBLE, Feature.DISK,
            new DiskConfiguration(
                BlockStateProvider.simple(ArrakisBlocks.ARAKITE_SANDSTONE.get()),
                new TwoLayersFeatureSize(1, 0, 2),
                List.of(
                    Blocks.SAND.defaultBlockState(),
                    Blocks.RED_SAND.defaultBlockState()
                )
            )
        );
        
        // Ore Configurations
        register(context, ORE_SPICE, Feature.ORE,
            new OreConfiguration(
                List.of(
                    OreConfiguration.target(ARRAKIS_SAND, ArrakisBlocks.SPICE_NODE.get().defaultBlockState())
                ),
                4
            )
        );
        
        register(context, ORE_CRYSTALLINE_SALT, Feature.ORE,
            new OreConfiguration(
                List.of(
                    OreConfiguration.target(ARRAKIS_STONE, ArrakisBlocks.CRYSTALLINE_SALT.get().defaultBlockState())
                ),
                12
            )
        );
    }
    
    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("chex", name));
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
        BootstapContext<ConfiguredFeature<?, ?>> context,
        ResourceKey<ConfiguredFeature<?, ?>> key,
        F feature,
        FC configuration
    ) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
