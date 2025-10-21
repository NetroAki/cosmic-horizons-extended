package com.netroaki.chex.world.feature;

import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;

public class CHEXPlacedFeatures {
  public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
      DeferredRegister.create(Registries.PLACED_FEATURE, "chex");

  // Sky Islands
  public static final ResourceKey<PlacedFeature> SKY_ISLANDS = registerKey("sky_islands");
  public static final ResourceKey<PlacedFeature> FLOATING_VEGETATION =
      registerKey("floating_vegetation");

  // Industrial Zone
  public static final ResourceKey<PlacedFeature> INDUSTRIAL_STRUCTURES =
      registerKey("industrial_structures");

  // Nuclear Wasteland
  public static final ResourceKey<PlacedFeature> NUCLEAR_CRATERS = registerKey("nuclear_craters");
  public static final ResourceKey<PlacedFeature> DEAD_TREES = registerKey("dead_trees");

  // Underground City
  public static final ResourceKey<PlacedFeature> UNDERGROUND_CITY_STRUCTURES =
      registerKey("underground_city_structures");
  public static final ResourceKey<PlacedFeature> UNDERGROUND_CITY_DECORATIONS =
      registerKey("underground_city_decorations");

  // Helper method to create resource keys
  private static ResourceKey<PlacedFeature> registerKey(String name) {
    return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("chex", name));
  }

  // Register all placed features
  public static void bootstrap(BootstapContext<PlacedFeature> context) {
    // Sky Islands
    register(
        context,
        SKY_ISLANDS,
        CHEXConfiguredFeatures.SKY_ISLAND,
        List.of(
            RarityFilter.onAverageOnceEvery(10),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()));

    register(
        context,
        FLOATING_VEGETATION,
        CHEXConfiguredFeatures.FLOATING_VEGETATION,
        List.of(
            RarityFilter.onAverageOnceEvery(2),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()));

    // Industrial Zone
    register(
        context,
        INDUSTRIAL_STRUCTURES,
        CHEXConfiguredFeatures.INDUSTRIAL_STRUCTURE,
        List.of(
            RarityFilter.onAverageOnceEvery(15),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()));

    // Nuclear Wasteland
    register(
        context,
        NUCLEAR_CRATERS,
        CHEXConfiguredFeatures.NUCLEAR_CRATER,
        List.of(
            RarityFilter.onAverageOnceEvery(25),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()));

    register(
        context,
        DEAD_TREES,
        CHEXConfiguredFeatures.DEAD_TREE,
        List.of(
            RarityFilter.onAverageOnceEvery(5),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()));

    // Underground City
    register(
        context,
        UNDERGROUND_CITY_STRUCTURES,
        CHEXConfiguredFeatures.UNDERGROUND_CITY_STRUCTURE,
        List.of(
            RarityFilter.onAverageOnceEvery(50),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(40)),
            BiomeFilter.biome()));

    register(
        context,
        UNDERGROUND_CITY_DECORATIONS,
        CHEXConfiguredFeatures.UNDERGROUND_CITY_DECORATION,
        List.of(
            RarityFilter.onAverageOnceEvery(10),
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(40)),
            BiomeFilter.biome()));
  }

  private static void register(
      BootstapContext<PlacedFeature> context,
      ResourceKey<PlacedFeature> key,
      ResourceKey<ConfiguredFeature<?, ?>> configuration,
      List<PlacementModifier> modifiers) {
    context.register(
        key,
        new PlacedFeature(
            context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuration), modifiers));
  }
}
