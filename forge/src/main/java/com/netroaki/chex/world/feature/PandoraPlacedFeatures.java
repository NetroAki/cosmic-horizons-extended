package com.netroaki.chex.world.feature;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class PandoraPlacedFeatures {
  // Feature keys
  public static final ResourceKey<PlacedFeature> FLOATING_ISLAND = createKey("floating_island");
  public static final ResourceKey<PlacedFeature> FLOATING_CRYSTALS = createKey("floating_crystals");
  public static final ResourceKey<PlacedFeature> FLOATING_VEGETATION =
      createKey("floating_vegetation");
  public static final ResourceKey<PlacedFeature> BIOLUMINESCENT_FOREST =
      createKey("bioluminescent_forest");

  // Helper method to create resource keys
  private static ResourceKey<PlacedFeature> createKey(String name) {
    return ResourceKey.create(
        Registries.PLACED_FEATURE, new net.minecraft.resources.ResourceLocation("chex", name));
  }

  // Register all placed features
  public static void bootstrap(BootstapContext<PlacedFeature> context) {
    HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures =
        context.lookup(Registries.CONFIGURED_FEATURE);

    // Floating Island
    register(
        context,
        FLOATING_ISLAND,
        configuredFeatures.getOrThrow(PandoraConfiguredFeatures.FLOATING_ISLAND),
        RarityFilter.onAverageOnceEvery(10),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_TOP_SOLID,
        BiomeFilter.biome());

    // Floating Crystals
    register(
        context,
        FLOATING_CRYSTALS,
        configuredFeatures.getOrThrow(PandoraConfiguredFeatures.FLOATING_CRYSTALS),
        RarityFilter.onAverageOnceEvery(15),
        InSquarePlacement.spread(),
        HeightRangePlacement.uniform(
            net.minecraft.world.level.levelgen.HeightRange.Types.MOTION_BLOCKING,
            net.minecraft.world.level.levelgen.structure.HeightMap.Types.MOTION_BLOCKING),
        BiomeFilter.biome());

    // Floating Vegetation
    register(
        context,
        FLOATING_VEGETATION,
        configuredFeatures.getOrThrow(PandoraConfiguredFeatures.FLOATING_VEGETATION),
        RarityFilter.onAverageOnceEvery(8),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_TOP_SOLID,
        BiomeFilter.biome());

    // Bioluminescent Forest
    register(
        context,
        BIOLUMINESCENT_FOREST,
        configuredFeatures.getOrThrow(PandoraConfiguredFeatures.BIOLUMINESCENT_FOREST),
        NoiseBasedCountPlacement.of(10, 0.8, 0),
        InSquarePlacement.spread(),
        SurfaceWaterDepthFilter.forMaxDepth(0),
        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
        BiomeFilter.biome());
  }

  // Helper method to register a placed feature
  private static void register(
      BootstapContext<PlacedFeature> context,
      ResourceKey<PlacedFeature> key,
      Holder<ConfiguredFeature<?, ?>> configuration,
      PlacementModifier... modifiers) {
    context.register(key, new PlacedFeature(configuration, List.of(modifiers)));
  }

  // Helper method to register a placed feature with a list of modifiers
  private static void register(
      BootstapContext<PlacedFeature> context,
      ResourceKey<PlacedFeature> key,
      Holder<ConfiguredFeature<?, ?>> configuration,
      List<PlacementModifier> modifiers) {
    context.register(key, new PlacedFeature(configuration, modifiers));
  }
}
