package com.netroaki.chex.world.feature;

import com.netroaki.chex.registry.CHEXBlocks;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;

public class BioluminescentForestConfiguredFeatures {
  // Configured feature keys
  public static final ResourceKey<ConfiguredFeature<?, ?>> BIOLUMINESCENT_TREE =
      createKey("bioluminescent_tree");
  public static final ResourceKey<ConfiguredFeature<?, ?>> BIOLUMINESCENT_FOREST =
      createKey("bioluminescent_forest");
  public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWING_MUSHROOMS =
      createKey("glowing_mushrooms");
  public static final ResourceKey<ConfiguredFeature<?, ?>> BIOLUMINESCENT_VEGETATION =
      createKey("bioluminescent_vegetation");

  // Helper method to create resource keys
  private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
    return ResourceKey.create(
        net.minecraft.core.registries.Registries.CONFIGURED_FEATURE,
        new net.minecraft.resources.ResourceLocation("chex", name));
  }

  // Register all configured features
  public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
    // Bioluminescent Tree
    register(context, BIOLUMINESCENT_TREE, Feature.TREE, createBioluminescentTree().build());

    // Bioluminescent Forest
    register(
        context,
        BIOLUMINESCENT_FOREST,
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfiguration(
            List.of(
                new net.minecraft.world.level.levelgen.feature.configurations
                    .RandomFeatureConfiguration(
                    Holder.direct(createBioluminescentTree().build()), 0.5f)),
            Holder.direct(createBioluminescentTree().build())));

    // Glowing Mushrooms
    register(
        context,
        GLOWING_MUSHROOMS,
        Feature.RANDOM_PATCH,
        FeatureUtils.simplePatchConfiguration(
            Feature.SIMPLE_BLOCK,
            new SimpleBlockConfiguration(
                BlockStateProvider.simple(CHEXBlocks.GLOWING_MUSHROOM.get())),
            List.of(Blocks.GRASS_BLOCK, CHEXBlocks.PANDORA_GRASS_BLOCK.get())));

    // Bioluminescent Vegetation
    register(
        context,
        BIOLUMINESCENT_VEGETATION,
        Feature.RANDOM_PATCH,
        FeatureUtils.simplePatchConfiguration(
            Feature.SIMPLE_BLOCK,
            new SimpleBlockConfiguration(
                BlockStateProvider.simple(CHEXBlocks.BIOLUMINESCENT_GRASS.get())),
            List.of(Blocks.GRASS_BLOCK, CHEXBlocks.PANDORA_GRASS_BLOCK.get())));
  }

  // Helper method to register a configured feature
  private static <
          FC extends net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration,
          F extends Feature<FC>>
      void register(
          BootstapContext<ConfiguredFeature<?, ?>> context,
          ResourceKey<ConfiguredFeature<?, ?>> key,
          F feature,
          FC config) {
    context.register(key, new ConfiguredFeature<>(feature, config));
  }

  // Create a bioluminescent tree configuration
  private static TreeConfiguration.TreeConfigurationBuilder createBioluminescentTree() {
    return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(CHEXBlocks.BIOLUMINESCENT_LOG.get()),
            new FancyTrunkPlacer(5, 5, 3),
            BlockStateProvider.simple(CHEXBlocks.BIOLUMINESCENT_LEAVES.get()),
            new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
            new TwoLayersFeatureSize(1, 0, 2))
        .ignoreVines()
        .dirt(BlockStateProvider.simple(CHEXBlocks.PANDORA_GRASS_BLOCK.get()));
  }
}
