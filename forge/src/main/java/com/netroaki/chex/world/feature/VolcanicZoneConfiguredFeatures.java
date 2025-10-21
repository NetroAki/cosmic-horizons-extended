package com.netroaki.chex.world.feature;

import com.netroaki.chex.registry.CHEXBlocks;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class VolcanicZoneConfiguredFeatures {
  // Rule tests for feature generation
  private static final RuleTest STONE_REPLACEABLES =
      new TagMatchTest(net.minecraft.tags.BlockTags.STONE_ORE_REPLACEABLES);
  private static final RuleTest DEEPSLATE_REPLACEABLES =
      new TagMatchTest(net.minecraft.tags.BlockTags.DEEPSLATE_ORE_REPLACEABLES);
  private static final RuleTest VOLCANIC_ROCK = new BlockMatchTest(CHEXBlocks.VOLCANIC_ROCK.get());

  // Configured feature keys
  public static final ResourceKey<ConfiguredFeature<?, ?>> VOLCANIC_ROCK_PATCH =
      createKey("volcanic_rock_patch");
  public static final ResourceKey<ConfiguredFeature<?, ?>> VOLCANIC_GEYSER =
      createKey("volcanic_geyser");
  public static final ResourceKey<ConfiguredFeature<?, ?>> VOLCANIC_ASH_LAYER =
      createKey("volcanic_ash_layer");
  public static final ResourceKey<ConfiguredFeature<?, ?>> VOLCANIC_VEGETATION =
      createKey("volcanic_vegetation");

  // Helper method to create resource keys
  private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
    return ResourceKey.create(
        net.minecraft.core.registries.Registries.CONFIGURED_FEATURE,
        new net.minecraft.resources.ResourceLocation("chex", name));
  }

  // Register all configured features
  public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
    // Volcanic Rock Patch
    register(
        context,
        VOLCANIC_ROCK_PATCH,
        Feature.ORE,
        new OreConfiguration(
            List.of(
                OreConfiguration.target(
                    STONE_REPLACEABLES, CHEXBlocks.VOLCANIC_ROCK.get().defaultBlockState()),
                OreConfiguration.target(
                    DEEPSLATE_REPLACEABLES, CHEXBlocks.VOLCANIC_ROCK.get().defaultBlockState())),
            33 // Vein size
            ));

    // Volcanic Geyser
    register(
        context,
        VOLCANIC_GEYSER,
        Feature.SIMPLE_BLOCK,
        new SimpleBlockConfiguration(BlockStateProvider.simple(CHEXBlocks.VOLCANIC_GEYSER.get())));

    // Volcanic Ash Layer
    register(
        context,
        VOLCANIC_ASH_LAYER,
        Feature.LAYER,
        new LayerConfiguration(
            1, // Height
            0, // Minimum height
            0 // Maximum height (0 means no limit)
            ));

    // Volcanic Vegetation
    register(
        context,
        VOLCANIC_VEGETATION,
        Feature.RANDOM_PATCH,
        FeatureUtils.simplePatchConfiguration(
            Feature.SIMPLE_BLOCK,
            new SimpleBlockConfiguration(
                BlockStateProvider.simple(CHEXBlocks.VOLCANIC_GRASS.get())),
            List.of(CHEXBlocks.VOLCANIC_ROCK.get())));
  }

  // Register placed features
  public static void registerPlacedFeatures(BootstapContext<PlacedFeature> context) {
    HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures =
        context.lookup(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE);

    // Volcanic Rock Patch Placement
    registerPlacedFeature(
        context,
        "volcanic_rock_patch_placed",
        configuredFeatures.getOrThrow(VOLCANIC_ROCK_PATCH),
        CountPlacement.of(UniformInt.of(0, 2)),
        InSquarePlacement.spread(),
        BiomeFilter.biome(),
        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(80)));

    // Volcanic Geyser Placement
    registerPlacedFeature(
        context,
        "volcanic_geyser_placed",
        configuredFeatures.getOrThrow(VOLCANIC_GEYSER),
        RarityFilter.onAverageOnceEvery(50), // Rarity
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome());

    // Volcanic Ash Layer Placement
    registerPlacedFeature(
        context,
        "volcanic_ash_layer_placed",
        configuredFeatures.getOrThrow(VOLCANIC_ASH_LAYER),
        CountPlacement.of(3), // Number of attempts per chunk
        InSquarePlacement.spread(),
        HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(100)),
        BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.AIR)),
        SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, 0, 0),
        BiomeFilter.biome());

    // Volcanic Vegetation Placement
    registerPlacedFeature(
        context,
        "volcanic_vegetation_placed",
        configuredFeatures.getOrThrow(VOLCANIC_VEGETATION),
        RarityFilter.onAverageOnceEvery(10),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_TOP_SOLID,
        BiomeFilter.biome());
  }

  // Helper method to register a configured feature
  private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
      BootstapContext<ConfiguredFeature<?, ?>> context,
      ResourceKey<ConfiguredFeature<?, ?>> key,
      F feature,
      FC config) {
    context.register(key, new ConfiguredFeature<>(feature, config));
  }

  // Helper method to register a placed feature
  private static void registerPlacedFeature(
      BootstapContext<PlacedFeature> context,
      String name,
      Holder<ConfiguredFeature<?, ?>> configuration,
      PlacementModifier... modifiers) {
    context.register(
        ResourceKey.create(
            net.minecraft.core.registries.Registries.PLACED_FEATURE,
            new net.minecraft.resources.ResourceLocation("chex", name)),
        new PlacedFeature(configuration, List.of(modifiers)));
  }
}
