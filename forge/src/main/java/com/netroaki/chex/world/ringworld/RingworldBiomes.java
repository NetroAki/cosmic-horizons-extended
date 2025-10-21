package com.netroaki.chex.world.ringworld;

import java.util.function.BiConsumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;

public class RingworldBiomes {
  // Biome Keys
  public static final ResourceKey<Biome> RING_PLAINS = createKey("ring_plains");
  public static final ResourceKey<Biome> RING_FOREST = createKey("ring_forest");
  public static final ResourceKey<Biome> RING_MOUNTAINS = createKey("ring_mountains");
  public static final ResourceKey<Biome> RING_RIVER = createKey("ring_river");
  public static final ResourceKey<Biome> RING_EDGE = createKey("ring_edge");
  public static final ResourceKey<Biome> RING_STRUCTURAL = createKey("ring_structural");

  private static ResourceKey<Biome> createKey(String name) {
    return ResourceKey.create(Registries.BIOME, new ResourceLocation("chex", name));
  }

  public static void bootstrap(BootstapContext<Biome> context) {
    // Register all biomes
    context.register(RING_PLAINS, createRingPlains());
    context.register(RING_FOREST, createRingForest());
    context.register(RING_MOUNTAINS, createRingMountains());
    context.register(RING_RIVER, createRingRiver());
    context.register(RING_EDGE, createRingEdge());
    context.register(RING_STRUCTURAL, createRingStructural());
  }

  public static void registerBiomeModifiers(BiConsumer<String, BiomeModifier> consumer) {
    // Add features to biomes
    addModifier(consumer, "add_features_plains", RING_PLAINS);
    addModifier(consumer, "add_features_forest", RING_FOREST);
    addModifier(consumer, "add_features_mountains", RING_MOUNTAINS);
    addModifier(consumer, "add_features_river", RING_RIVER);
    addModifier(consumer, "add_features_edge", RING_EDGE);
    addModifier(consumer, "add_features_structural", RING_STRUCTURAL);
  }

  private static void addModifier(
      BiConsumer<String, BiomeModifier> consumer, String name, ResourceKey<Biome>... biomes) {
    consumer.accept(
        name,
        new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            context -> context.hasTag(createBiomeTag(biomes[0].location())),
            context -> context.is(createPlacedFeatureTag("chex:" + name + "_features")),
            GenerationStep.Decoration.VEGETAL_DECORATION));
  }

  private static ResourceKey<PlacedFeature> createPlacedFeatureTag(String name) {
    return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(name));
  }

  private static ResourceKey<net.minecraft.tags.TagKey<Biome>> createBiomeTag(
      ResourceLocation name) {
    return ResourceKey.create(Registries.BIOME, name);
  }

  // Biome Definitions
  private static Biome createRingPlains() {
    BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder();
    MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .temperature(0.8f)
        .downfall(0.4f)
        .specialEffects(
            new BiomeSpecialEffects.Builder()
                .skyColor(0x7BA4FF)
                .fogColor(0xC0D8FF)
                .waterColor(0x3F76E4)
                .waterFogColor(0x1E3B73)
                .build())
        .mobSpawnSettings(spawns.build())
        .generationSettings(generation.build())
        .build();
  }

  private static Biome createRingForest() {
    BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder();
    MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .temperature(0.7f)
        .downfall(0.6f)
        .specialEffects(
            new BiomeSpecialEffects.Builder()
                .skyColor(0x7BA4FF)
                .fogColor(0xC0D8FF)
                .waterColor(0x3F76E4)
                .waterFogColor(0x1E3B73)
                .foliageColorOverride(0x44A041)
                .grassColorOverride(0x5CA64B)
                .build())
        .mobSpawnSettings(spawns.build())
        .generationSettings(generation.build())
        .build();
  }

  private static Biome createRingMountains() {
    BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder();
    MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .temperature(0.5f)
        .downfall(0.3f)
        .specialEffects(
            new BiomeSpecialEffects.Builder()
                .skyColor(0x7BA4FF)
                .fogColor(0xC0D8FF)
                .waterColor(0x3F76E4)
                .waterFogColor(0x1E3B73)
                .build())
        .mobSpawnSettings(spawns.build())
        .generationSettings(generation.build())
        .build();
  }

  private static Biome createRingRiver() {
    BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder();
    MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .temperature(0.8f)
        .downfall(0.9f)
        .specialEffects(
            new BiomeSpecialEffects.Builder()
                .skyColor(0x7BA4FF)
                .fogColor(0xC0D8FF)
                .waterColor(0x3F76E4)
                .waterFogColor(0x1E3B73)
                .build())
        .mobSpawnSettings(spawns.build())
        .generationSettings(generation.build())
        .build();
  }

  private static Biome createRingEdge() {
    BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder();
    MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .temperature(0.6f)
        .downfall(0.1f)
        .specialEffects(
            new BiomeSpecialEffects.Builder()
                .skyColor(0x4A6BFF)
                .fogColor(0x8BA8FF)
                .waterColor(0x3F76E4)
                .waterFogColor(0x1E3B73)
                .build())
        .mobSpawnSettings(spawns.build())
        .generationSettings(generation.build())
        .build();
  }

  private static Biome createRingStructural() {
    BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder();
    MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

    return new Biome.BiomeBuilder()
        .hasPrecipitation(false)
        .temperature(0.5f)
        .downfall(0.1f)
        .specialEffects(
            new BiomeSpecialEffects.Builder()
                .skyColor(0x5A7BFF)
                .fogColor(0x9BB8FF)
                .waterColor(0x3F76E4)
                .waterFogColor(0x1E3B73)
                .build())
        .mobSpawnSettings(spawns.build())
        .generationSettings(generation.build())
        .build();
  }

  // Helper method to get the resource location for a biome
  public static ResourceLocation getBiomeLocation(ResourceKey<Biome> biomeKey) {
    return biomeKey.location();
  }
}
