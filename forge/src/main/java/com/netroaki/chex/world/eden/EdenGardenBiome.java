package com.netroaki.chex.world.eden;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

public class EdenGardenBiome {
  public static final float DEFAULT_TEMPERATURE = 0.8F;
  public static final float DEFAULT_DOWNFALL = 0.9F;

  public static Biome create(BootstapContext<Biome> context) {
    MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
    BiomeGenerationSettings.Builder biomeBuilder =
        new BiomeGenerationSettings.Builder(
            context.lookup(Registries.PLACED_FEATURE),
            context.lookup(Registries.CONFIGURED_CARVER));

    // Base biome settings
    globalOverworldGeneration(biomeBuilder);
    BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
    BiomeDefaultFeatures.addDefaultSoftDisks(biomeBuilder);

    // Add Eden's Garden specific features
    addEdenFlowers(biomeBuilder);
    addEdenVegetation(biomeBuilder);
    addEdenWaterFeatures(biomeBuilder);

    // Configure mob spawns
    spawnBuilder.addSpawn(
        MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 10, 2, 3));
    spawnBuilder.addSpawn(
        MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 1, 2));
    spawnBuilder.addSpawn(
        MobCategory.WATER_AMBIENT,
        new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8));

    return new Biome.BiomeBuilder()
        .hasPrecipitation(true)
        .downfall(DEFAULT_DOWNFALL)
        .temperature(DEFAULT_TEMPERATURE)
        .temperatureAdjustment(Biome.TemperatureModifier.NONE)
        .specialEffects(
            (new BiomeSpecialEffects.Builder())
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(12638463)
                .skyColor(calculateSkyColor(DEFAULT_TEMPERATURE))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_LUSH_CAVES))
                .build())
        .mobSpawnSettings(spawnBuilder.build())
        .generationSettings(biomeBuilder.build())
        .build();
  }

  private static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
    BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
    BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
    BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
    BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
    BiomeDefaultFeatures.addDefaultSprings(builder);
    BiomeDefaultFeatures.addSurfaceFreezing(builder);
  }

  private static void addEdenFlowers(BiomeGenerationSettings.Builder builder) {
    // Add Celestial Blooms to the biome
    builder.addFeature(
        GenerationStep.Decoration.VEGETAL_DECORATION,
        net.minecraft.data.worldgen.placement.VegetationPlacements.TREES_PLAINS);

    // Add more custom plant features here
    builder.addFeature(
        GenerationStep.Decoration.VEGETAL_DECORATION,
        net.minecraft.data.worldgen.placement.VegetationPlacements.FLOWER_FLOWER_FOREST);

    // Add grass patches
    builder.addFeature(
        GenerationStep.Decoration.VEGETAL_DECORATION,
        net.minecraft.data.worldgen.placement.VegetationPlacements.PATCH_GRASS_PLAIN);
  }

  private static void addEdenVegetation(BiomeGenerationSettings.Builder builder) {
    // TODO: Add custom vegetation features
    BiomeDefaultFeatures.addForestFlowers(builder);
    BiomeDefaultFeatures.addDefaultFlowers(builder);
    BiomeDefaultFeatures.addDefaultMushrooms(builder);
  }

  private static void addEdenWaterFeatures(BiomeGenerationSettings.Builder builder) {
    // TODO: Add custom water features
    BiomeDefaultFeatures.addDefaultLakes(builder);
    BiomeDefaultFeatures.addDefaultSprings(builder);
  }

  private static int calculateSkyColor(float temperature) {
    float f = temperature / 3.0F;
    f = Mth.clamp(f, -1.0F, 1.0F);
    return Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
  }
}
