package com.netroaki.chex.world.feature.inferno;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InfernoFeatures {
  public static final DeferredRegister<Feature<?>> FEATURES =
      DeferredRegister.create(ForgeRegistries.FEATURES, CosmicHorizonsExpanded.MOD_ID);

  // Register features
  public static final RegistryObject<Feature<NoneFeatureConfiguration>> MAGMA_GEYSER_FEATURE =
      FEATURES.register(
          "magma_geyser", () -> new MagmaGeyserFeature(NoneFeatureConfiguration.CODEC));

  public static final RegistryObject<Feature<NoneFeatureConfiguration>> BASALT_PILLAR_FEATURE =
      FEATURES.register(
          "basalt_pillar", () -> new BasaltPillarFeature(NoneFeatureConfiguration.CODEC));

  public static final RegistryObject<Feature<NoneFeatureConfiguration>> OBSIDIAN_FLORA_FEATURE =
      FEATURES.register(
          "obsidian_flora", () -> new ObsidianFloraFeature(NoneFeatureConfiguration.CODEC));

  public static final RegistryObject<Feature<NoneFeatureConfiguration>> ASH_DUNES_FEATURE =
      FEATURES.register("ash_dunes", () -> new AshDunesFeature(NoneFeatureConfiguration.CODEC));

  // Configured features
  public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_MAGMA_GEYSER =
      registerConfiguredFeature(
          "magma_geyser", MAGMA_GEYSER_FEATURE.get(), NoneFeatureConfiguration.INSTANCE);

  public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_BASALT_PILLAR =
      registerConfiguredFeature(
          "basalt_pillar", BASALT_PILLAR_FEATURE.get(), NoneFeatureConfiguration.INSTANCE);

  public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_OBSIDIAN_FLORA =
      registerConfiguredFeature(
          "obsidian_flora", OBSIDIAN_FLORA_FEATURE.get(), NoneFeatureConfiguration.INSTANCE);

  public static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_ASH_DUNES =
      registerConfiguredFeature(
          "ash_dunes", ASH_DUNES_FEATURE.get(), NoneFeatureConfiguration.INSTANCE);

  // Placed features
  public static final RegistryObject<PlacedFeature> PLACED_MAGMA_GEYSER =
      registerPlacedFeature(
          "magma_geyser",
          CONFIGURED_MAGMA_GEYSER,
          RarityFilter.onAverageOnceEvery(16),
          InSquarePlacement.spread(),
          PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
          BiomeFilter.biome());

  public static final RegistryObject<PlacedFeature> PLACED_BASALT_PILLAR =
      registerPlacedFeature(
          "basalt_pillar",
          CONFIGURED_BASALT_PILLAR,
          RarityFilter.onAverageOnceEvery(24),
          InSquarePlacement.spread(),
          PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
          BiomeFilter.biome());

  public static final RegistryObject<PlacedFeature> PLACED_OBSIDIAN_FLORA =
      registerPlacedFeature(
          "obsidian_flora",
          CONFIGURED_OBSIDIAN_FLORA,
          RarityFilter.onAverageOnceEvery(32),
          InSquarePlacement.spread(),
          PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
          BiomeFilter.biome());

  public static final RegistryObject<PlacedFeature> PLACED_ASH_DUNES =
      registerPlacedFeature(
          "ash_dunes",
          CONFIGURED_ASH_DUNES,
          RarityFilter.onAverageOnceEvery(20),
          InSquarePlacement.spread(),
          PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
          BiomeFilter.biome());

  // Helper methods
  private static <FC extends FeatureConfiguration, F extends Feature<FC>>
      RegistryObject<ConfiguredFeature<?, ?>> registerConfiguredFeature(
          String name, F feature, FC config) {
    return RegistryObject.create(
        CosmicHorizonsExpanded.location(name), Registry.CONFIGURED_FEATURE_REGISTRY);
  }

  private static RegistryObject<PlacedFeature> registerPlacedFeature(
      String name,
      RegistryObject<ConfiguredFeature<?, ?>> configuredFeature,
      PlacementModifier... placementModifiers) {
    return RegistryObject.create(
        CosmicHorizonsExpanded.location(name), Registry.PLACED_FEATURE_REGISTRY);
  }

  public static void register(IEventBus eventBus) {
    FEATURES.register(eventBus);
  }
}
