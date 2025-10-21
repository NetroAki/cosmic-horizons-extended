package com.netroaki.chex.world.feature;

import com.netroaki.chex.registry.CHEXBlocks;
import java.util.List;
import java.util.OptionalInt;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
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

public class PandoraConfiguredFeatures {
  // Rule tests for feature generation
  private static final RuleTest STONE_ORE_REPLACEABLES =
      new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
  private static final RuleTest DEEPSLATE_ORE_REPLACEABLES =
      new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
  private static final RuleTest FLOATING_ISLAND_BLOCKS = new BlockMatchTest(Blocks.END_STONE);

  // Configured feature keys
  public static final ResourceKey<ConfiguredFeature<?, ?>> FLOATING_ISLAND =
      createKey("floating_island");
  public static final ResourceKey<ConfiguredFeature<?, ?>> FLOATING_CRYSTALS =
      createKey("floating_crystals");
  public static final ResourceKey<ConfiguredFeature<?, ?>> FLOATING_VEGETATION =
      createKey("floating_vegetation");
  public static final ResourceKey<ConfiguredFeature<?, ?>> BIOLUMINESCENT_FOREST =
      createKey("bioluminescent_forest");

  // Helper method to create resource keys
  private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
    return ResourceKey.create(
        Registries.CONFIGURED_FEATURE, new net.minecraft.resources.ResourceLocation("chex", name));
  }

  // Register all configured features
  public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
    // Floating Island
    register(
        context,
        FLOATING_ISLAND,
        Feature.NO_SURFACE_ORE,
        new OreConfiguration(
            FLOATING_ISLAND_BLOCKS, CHEXBlocks.FLOATING_STONE.get().defaultBlockState(), 33));

    // Floating Crystals
    register(
        context,
        FLOATING_CRYSTALS,
        Feature.ORE,
        new OreConfiguration(
            STONE_ORE_REPLACEABLES,
            CHEXBlocks.FLOATING_CRYSTAL_CLUSTER.get().defaultBlockState(),
            12));

    // Floating Vegetation
    register(
        context,
        FLOATING_VEGETATION,
        Feature.RANDOM_SELECTOR,
        new RandomFeatureConfiguration(
            List.of(
                new WeightedPlacedFeature(
                    Holder.direct(createFloatingTree().build().value()), 0.5F)),
            Holder.direct(createFloatingTree().build().value())));

    // Bioluminescent Forest
    register(context, BIOLUMINESCENT_FOREST, Feature.TREE, createBioluminescentTree().build());
  }

  // Helper method to create a configured feature
  private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
      BootstapContext<ConfiguredFeature<?, ?>> context,
      ResourceKey<ConfiguredFeature<?, ?>> key,
      F feature,
      FC config) {
    context.register(key, new ConfiguredFeature<>(feature, config));
  }

  // Create a floating tree configuration
  private static TreeConfiguration.TreeConfigurationBuilder createFloatingTree() {
    return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(CHEXBlocks.FLOATING_LOG.get()),
            new FancyTrunkPlacer(3, 11, 0),
            BlockStateProvider.simple(CHEXBlocks.FLOATING_LEAVES.get()),
            new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
            new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))
        .ignoreVines();
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
